#!/bin/bash

echo "Setting up MySQL Connector/J for AgriTech Smart Irrigation System"
echo "==============================================================="

# Create lib directory if it doesn't exist
mkdir -p lib

# Check if file already exists
if [ -f "lib/mysql-connector-j-8.3.0.jar" ]; then
    echo "MySQL Connector already downloaded."
else
    echo "Downloading MySQL Connector/J..."
    if command -v curl &> /dev/null; then
        curl -L https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar -o lib/mysql-connector-j-8.3.0.jar
    elif command -v wget &> /dev/null; then
        wget https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar -O lib/mysql-connector-j-8.3.0.jar
    else
        echo "Error: Neither curl nor wget are available. Please install one of them."
        exit 1
    fi
    
    if [ -f "lib/mysql-connector-j-8.3.0.jar" ]; then
        echo "Download completed successfully."
    else
        echo "Failed to download MySQL Connector."
        exit 1
    fi
fi

echo ""
echo "MySQL Connector setup completed."
echo ""
echo "To compile the application with MySQL support:"
echo "javac --module-path \"lib\" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics -cp \"lib/mysql-connector-j-8.3.0.jar\" .vscode/*.java"
echo ""
echo "To run the application:"
echo "java --module-path \"lib\" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics -cp \".vscode:lib/mysql-connector-j-8.3.0.jar\" MainApplication"
echo "" 