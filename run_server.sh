#!/bin/bash

# Define the main class and the directory for compiled classes
MAIN_CLASS="Main"
SRC_DIR="src/main/java"
TARGET_DIR="target"

# Clean up the target directory
if [ -d "$TARGET_DIR" ]; then
  rm -rf "$TARGET_DIR"
fi
mkdir -p "$TARGET_DIR"

# Compile the Java classes
echo "Compiling Java classes..."
javac -d "$TARGET_DIR" $SRC_DIR/*.java

if [ $? -ne 0 ]; then
  echo "Compilation failed!"
  exit 1
fi

# Run the server with provided arguments
echo "Running the server..."
java -cp "$TARGET_DIR" $MAIN_CLASS "$@"

# End of script
