## Prevent permission to op sync

andorid 29+

### patcher

patch the service.jar

### magiskModule
make magisk module

###  patcher source
```shell
cd app/source
adb pull /system/framework/services.jar
```

> magisk-module template 
>
> https://github.com/ikas-mc/prevent-permission-to-op-sync

> dexpatcher-tool
>
> https://github.com/DexPatcher/dexpatcher-tool