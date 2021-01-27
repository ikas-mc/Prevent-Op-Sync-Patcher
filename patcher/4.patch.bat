@echo off

rd /S /Q out 
rd /S /Q patched 
mkdir out
mkdir patched
java -jar dexpatcher-1.8.0-beta1.jar -m --output .\out  services.jar patch.dex
copy services.jar patched\services.jar
cd out 
jar uvf0 ..\patched\services.jar *.dex
cd ..