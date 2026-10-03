@echo off
cd /d "%~dp0"
REM Run the application with SQLite driver
echo Starting application...
java -cp "target/classes;lib/sqlite-jdbc-3.41.2.1.jar" com.school.view.LoginGUI
pause
