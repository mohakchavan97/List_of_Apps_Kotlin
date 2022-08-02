@echo off
adb devices -l
set /p ids="Enter Trasport IDs (comma separated):"

call :parse "%ids%"
goto :eof

:parse
setlocal
set list=%~1
for /F "tokens=1* delims=," %%f in ("%list%") do (
    rem if the item exist
    if not "%%f" == "" call :getLineNumber %%f
    rem if next item exist
    if not "%%g" == "" call :parse "%%g"
)
endlocal
goto :eof

:getLineNumber
setlocal
echo;
echo Running for %1
set id=%1
echo Stopping...
adb -t %id% shell am force-stop com.mohakchavan.applists
echo Clearing...
adb -t %id% shell pm clear com.mohakchavan.applists
echo Uninstalling...
adb -t %id% uninstall com.mohakchavan.applists
goto :eof