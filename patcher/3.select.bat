@echo off

del /Q patch.dex
for /f "delims=" %%i in ( 'adb shell getprop ro.build.version.sdk') do set ANDROID_VERSION=%%i
echo %ANDROID_VERSION%
set PATCH_DEX=patch-%ANDROID_VERSION%.dex
copy %PATCH_DEX% patch.dex