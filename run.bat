@echo off
REM Run script for E-commerce System (Windows)

echo ===================================
echo Running E-commerce System...
echo ===================================
echo.

REM Check if bin directory exists
if not exist bin (
    echo Error: Compiled classes not found!
    echo Please run compile.bat first
    exit /b 1
)

REM Run the application
java -cp bin main.Main
