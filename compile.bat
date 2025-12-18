@echo off
REM Compile script for E-commerce System (Windows)

echo ===================================
echo Compiling E-commerce System...
echo ===================================

REM Create bin directory if it doesn't exist
if not exist bin mkdir bin

REM Compile all Java files
echo Compiling Java source files...
javac -d bin -sourcepath src ^
  src/main/Main.java ^
  src/main/gui/MainFrame.java ^
  src/models/*.java ^
  src/patterns/singleton/*.java ^
  src/patterns/factory/*.java ^
  src/patterns/prototype/*.java ^
  src/patterns/builder/*.java ^
  src/patterns/proxy/*.java ^
  src/services/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ===================================
    echo Compilation successful!
    echo ===================================
    echo.
    echo To run the application, use: run.bat
    echo Or run: java -cp bin main.Main
) else (
    echo.
    echo ===================================
    echo Compilation failed!
    echo ===================================
    exit /b 1
)
