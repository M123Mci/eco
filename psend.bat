@echo off
setlocal

set "ROOT=%~dp0.."
set "SCRIPT=%ROOT%\.psend\distribute.mjs"
set "CONFIG=%ROOT%\.psend\plugins\eco.json"

if not exist "%SCRIPT%" (
    echo Distribution script %SCRIPT% missing.
    exit /b 1
)

node "%SCRIPT%" --config "%CONFIG%" %*
exit /b %ERRORLEVEL%
