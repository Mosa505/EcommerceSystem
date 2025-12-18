#!/bin/bash
# Run script for E-commerce System

echo "==================================="
echo "Running E-commerce System..."
echo "==================================="
echo ""

# Check if bin directory exists
if [ ! -d "bin" ]; then
    echo "Error: Compiled classes not found!"
    echo "Please run ./compile.sh first"
    exit 1
fi

# Run the application
java -cp bin main.Main
