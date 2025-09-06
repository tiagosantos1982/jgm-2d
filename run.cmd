@echo off
REM Forca code page UTF-8
chcp 65001 > nul
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8
set SRC_DIR=src
set BIN_DIR=bin
set PKG=br/com/jumpman
set MAIN_CLASS=br.com.jumpman.App

:menu
cls

echo 1. Build projeto
echo 2. Executar app
echo 3. Sair
echo 4. Converter fontes para UTF-8
set /p op=Escolha uma opcao: 
if "%op%"=="1" goto build
if "%op%"=="2" goto run
if "%op%"=="3" exit
if "%op%"=="4" goto convert

goto menu

:convert
echo Limpando BOM e convertendo todos os .java para UTF-8 (sem BOM)...
powershell -NoLogo -NoProfile -Command "Add-Type -AssemblyName 'System.Text'; Add-Type -AssemblyName 'System.IO'; $enc = New-Object System.Text.UTF8Encoding($false); Get-ChildItem -Path '%SRC_DIR%' -Filter *.java -Recurse | ForEach-Object { $p=$_.FullName; $bytes=[System.IO.File]::ReadAllBytes($p); if ($bytes.Length -ge 3 -and $bytes[0] -eq 0xEF -and $bytes[1] -eq 0xBB -and $bytes[2] -eq 0xBF) { $bytes = $bytes[3..($bytes.Length-1)] }; $text = [System.Text.Encoding]::UTF8.GetString($bytes); $sw = New-Object System.IO.StreamWriter($p,$false,$enc); $sw.Write($text); $sw.Close() }"
echo Conversao e limpeza concluidas.
pause
goto menu

:build
if not exist %BIN_DIR% mkdir %BIN_DIR%
javac -encoding UTF-8 -d %BIN_DIR% ^
    %SRC_DIR%\%PKG%\*.java ^
    %SRC_DIR%\%PKG%\fx\*.java ^
    %SRC_DIR%\%PKG%\timer\*.java ^
    %SRC_DIR%\%PKG%\screens\*.java
if %errorlevel% neq 0 (
    echo Erro na compilacao!
    pause
    goto menu
)
echo Build concluido!
pause
goto menu

:run
java -cp %BIN_DIR% %MAIN_CLASS%
pause
goto menu
