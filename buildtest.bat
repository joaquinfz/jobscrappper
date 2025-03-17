@echo off
SETLOCAL

REM Parse command-line arguments
SET SKIP_FRONTEND=0
SET SKIP_BACKEND=0
SET SKIP_DB=0

FOR %%A IN (%*) DO (
    IF "%%A"=="nfront" SET SKIP_FRONTEND=1
    IF "%%A"=="nback" SET SKIP_BACKEND=1
    IF "%%A"=="ndb" SET SKIP_DB=1
)

REM Step 1: Navigate to Angular project and build it (if not skipped)
IF %SKIP_FRONTEND%==0 (
    cd angular
    echo Building Angular project...
    call ng build --output-path ../src/main/resources/static
    if %ERRORLEVEL% NEQ 0 (
        echo Angular build failed!
        exit /b %ERRORLEVEL%
    )
    cd ..
) ELSE (
    echo Skipping Angular build...
)

REM Step 2: Clear SQLite database tables (if not skipped)
IF %SKIP_DB%==0 (
    echo Clearing SQLite database tables...
    sqlite3 database.db "DELETE FROM skills; DELETE FROM job_offers; DELETE FROM job_skills;"
    if %ERRORLEVEL% NEQ 0 (
        echo SQLite cleanup failed!
        exit /b %ERRORLEVEL%
    )
) ELSE (
    echo Skipping SQLite database cleanup...
)

REM Step 3: Run Maven clean install (if not skipped)
IF %SKIP_BACKEND%==0 (
    echo Running Maven clean install...
    call mvn clean install
    if %ERRORLEVEL% NEQ 0 (
        echo Maven build failed!
        exit /b %ERRORLEVEL%
    )
) ELSE (
    echo Skipping Maven build...
)



REM Step 4: Start Spring Boot application
echo Starting Spring Boot application...
call mvn spring-boot:run

ENDLOCAL