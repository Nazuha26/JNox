@echo on

for /f "tokens=*" %%i in ('jdeps --ignore-missing-deps -q -recursive --multi-release 21 --print-module-deps client/target/client-SNAPSHOT.jar') do set MODS=%%i

if "%MODS%"=="" (
    echo Error: Could not determine modules.
    pause
    exit /b
)

echo Found modules: %MODS%

:: delete old "nox-jre"
if exist "nox-jre" rmdir /s /q "nox-jre"

jlink --verbose ^
 --add-modules %MODS% ^
 --output nox-jre ^
 --strip-debug ^
 --no-man-pages ^
 --no-header-files ^
 --compress zip-9


:: cleaning
echo.
echo Cleaning up unnecessary files...

:: 1. delete all *.exe from "bin" folder, except javaw.exe & java.exe
for %%f in ("nox-jre\bin\*.exe") do (
    if /I not "%%~nxf"=="javaw.exe" if /I not "%%~nxf"=="java.exe" del /q "%%f"
)

:: 2. delete "legal" folder (licenses)
if exist "nox-jre\legal" rmdir /s /q "nox-jre\legal"

echo.
echo JRE build and optimization completed!
pause