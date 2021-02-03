package com.android.server.policy;

import android.app.AppOpsManager;

import lanchon.dexpatcher.annotation.DexEdit;
import lanchon.dexpatcher.annotation.DexIgnore;
import lanchon.dexpatcher.annotation.DexWrap;

@DexEdit(contentOnly = true)
public class PermissionPolicyService {
    @DexEdit(contentOnly = true)
    public static class PermissionToOpSynchroniser {
        @DexIgnore
        private AppOpsManager mAppOpsManager;

        @DexWrap
        private void setUidMode(int opCode, int uid, int mode, String packageName) {
            if (mode == AppOpsManager.MODE_IGNORED) {
                setUidMode(opCode, uid, mode, packageName);
                return;
            }
            int unsafeCheckOpRaw = mAppOpsManager.unsafeCheckOpRaw(AppOpsManager.opToPublicName(opCode), uid, packageName);
            if (unsafeCheckOpRaw != mode && unsafeCheckOpRaw != AppOpsManager.MODE_IGNORED) {
                setUidMode(opCode, uid, mode, packageName);
            }
        }
    }
}
