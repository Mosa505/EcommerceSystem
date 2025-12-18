#!/bin/bash
# Compile script for E-commerce System

echo "==================================="
echo "Compiling E-commerce System..."
echo "==================================="

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all Java files
echo "Compiling Java source files..."
javac -d bin -sourcepath src \
  src/main/Main.java \
  src/main/gui/MainFrame.java \
  src/models/*.java \
  src/patterns/singleton/*.java \
  src/patterns/factory/*.java \
  src/patterns/prototype/*.java \
  src/patterns/builder/*.java \
  src/patterns/proxy/*.java \
  src/services/*.java

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo ""
    echo "==================================="
    echo "Compilation successful!"
    echo "==================================="
    echo ""
    echo "To run the application, use: ./run.sh"
    echo "Or run: java -cp bin main.Main"
else
    echo ""
    echo "==================================="
    echo "Compilation failed!"
    echo "==================================="
    exit 1
fi
