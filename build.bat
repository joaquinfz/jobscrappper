@echo off
SETLOCAL

REM Step 1: Navigate to Angular project and build it
cd angular
echo Building Angular project...
call ng build --output-path ../src/main/resources/static
if %ERRORLEVEL% NEQ 0 (
    echo Angular build failed!
    exit /b %ERRORLEVEL%
)
cd ..

REM Step 2: Run Maven clean install and start Spring Boot
echo Running Maven clean install...
call mvn clean install
if %ERRORLEVEL% NEQ 0 (
    echo Maven build failed!
    exit /b %ERRORLEVEL%
)

echo Starting Spring Boot application...
call mvn spring-boot:run

ENDLOCAL
