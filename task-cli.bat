@echo off
setlocal
set JAR=%~dp0target\task-cli.jar
if not exist "%JAR%" (
  echo task-cli.jar not found. Run mvn package first.
  exit /b 1
)
java -jar "%JAR%" %*
