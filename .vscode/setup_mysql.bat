@echo off
echo Setting up MySQL Connector/J for AgriTech Smart Irrigation System
echo ===============================================================

REM Create lib directory if it doesn't exist
if not exist lib mkdir lib

REM Check if file already exists
if exist lib\mysql-connector-j-8.3.0.jar (
    echo MySQL Connector already downloaded.
) else (
    echo Downloading MySQL Connector/J...
    powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar' -OutFile 'lib\mysql-connector-j-8.3.0.jar'}"
    
    if exist lib\mysql-connector-j-8.3.0.jar (
        echo Download completed successfully.
    ) else (
        echo Failed to download MySQL Connector.
        exit /b 1
    )
)

echo.
echo MySQL Connector setup completed.
echo.
echo To compile the application with MySQL support:
echo javac --module-path "lib" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics -cp "lib\mysql-connector-j-8.3.0.jar" .vscode\*.java
echo.
echo To run the application:
echo java --module-path "lib" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics -cp ".vscode;lib\mysql-connector-j-8.3.0.jar" MainApplication
echo.

pause 