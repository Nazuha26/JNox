@echo off
setlocal

cd /d "%~dp0.."

set "JAR=target\video2reword-1.0.0.jar"
set "OUT=nox-jre"

if not exist "%JAR%" (
    echo Error: JAR not found: %JAR%
    echo Run mvn clean package first.
    pause
    exit /b 1
)

for /f "tokens=*" %%i in ('jdeps --ignore-missing-deps -q -recursive --multi-release 21 --print-module-deps "%JAR%"') do set "MODS=%%i"

if "%MODS%"=="" (
    echo Error: Could not determine modules.
    pause
    exit /b 1
)

echo Found modules: %MODS%

if exist "%OUT%" rmdir /s /q "%OUT%"

jlink --verbose ^
 --add-modules %MODS% ^
 --output "%OUT%" ^
 --strip-debug ^
 --no-man-pages ^
 --no-header-files ^
 --compress=2

if errorlevel 1 (
    echo Error: jlink failed.
    pause
    exit /b 1
)

echo.
echo Cleaning up unnecessary files...

for %%f in ("%OUT%\bin\*.exe") do (
    if /I not "%%~nxf"=="javaw.exe" if /I not "%%~nxf"=="java.exe" del /q "%%f"
)

if exist "%OUT%\legal" rmdir /s /q "%OUT%\legal"

echo.
echo JRE build and optimization completed!
pause