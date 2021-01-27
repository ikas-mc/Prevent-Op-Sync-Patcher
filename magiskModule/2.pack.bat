@echo off

copy /Y services.jar template\system\framework\
cd template
jar cvfM0 ..\PreventOpSync.zip *
cd ..