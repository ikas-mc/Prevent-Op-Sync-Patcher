package com.android.server.appop;

import android.app.AppOpsManager;
import android.util.Log;
import android.util.SparseIntArray;

import com.android.internal.app.IAppOpsCallback;

import lanchon.dexpatcher.annotation.DexAdd;
import lanchon.dexpatcher.annotation.DexEdit;
import lanchon.dexpatcher.annotation.DexIgnore;
import lanchon.dexpatcher.annotation.DexWrap;

@DexEdit(contentOnly = true)
public class AppOpsService {
    @DexAdd
    private static final int ENHANCED_MODE_OP_START = 100000;

    @DexAdd
    private static final int[] ALLOW_OP_LIST = new int[]{AppOpsManager.OP_CAMERA, AppOpsManager.OP_RECORD_AUDIO};

    @DexAdd
    private int getUidModeCustom(int uid, int code) {
        synchronized (this) {
            UidState uidState = getUidStateLocked(uid, false);
            if (null != uidState) {
                SparseIntArray opModes = uidState.opModes;
                if (null != opModes) {
                    int mode = opModes.get(code, -1220);
                    return mode;
                }
            }
        }
        return -1221;
    }

    @DexWrap
    private void setUidMode(int op, int uid, int mode, IAppOpsCallback permissionPolicyCallback) {
        boolean enhancedMode = false;
        if (op >= ENHANCED_MODE_OP_START) {
            enhancedMode = true;
            //realOp
            op = op - ENHANCED_MODE_OP_START;
        }

        //设置为IGNORED,允许调用
        if (mode == AppOpsManager.MODE_IGNORED) {
            setUidMode(op, uid, mode, permissionPolicyCallback);
            return;
        }

        //允许
        for (int item : ALLOW_OP_LIST) {
            if (item == op) {
                setUidMode(op, uid, mode, permissionPolicyCallback);
                return;
            }
        }

        //增强模式,允许调用
        if (enhancedMode) {
            setUidMode(op, uid, mode, permissionPolicyCallback);
            return;
        }

        //检查当前uid mode
        int switchOp = (int) AppOpsManager.opToSwitch(op);
        int currentMode = getUidModeCustom(uid, switchOp);

        //如果当前不是IGNORED,允许调用
        if (currentMode != AppOpsManager.MODE_IGNORED) {
            setUidMode(op, uid, mode, permissionPolicyCallback);
        } else {
            //其他情况不允许
            Log.w("AppOpsService-hook", String.format("skip setSidMode,uid=%s,op=%s,toSetMode=%s", uid, op, mode));
        }

    }

    @DexIgnore
    private UidState getUidStateLocked(int uid, boolean edit) {
        return null;
    }

    @DexIgnore
    static final class UidState {
        public SparseIntArray opModes;
    }
}
