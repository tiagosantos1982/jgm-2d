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

echo =======================================================
echo          JUMPMAN - Sistema de Build e Execucao
echo =======================================================
echo 1. Build projeto (com opcoes de log)
echo 2. Executar aplicacao
echo 3. Sair
echo 4. Converter arquivos fonte para UTF-8
echo =======================================================
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
echo Compilando o projeto...

echo Opcoes de compilacao:
echo 1. Compilar normalmente (sem log)
echo 2. Compilar com log detalhado na tela
echo 3. Compilar e salvar log em arquivo
set /p opcao_build=Escolha uma opcao: 

if "%opcao_build%"=="1" (
    REM Compilar normalmente sem log
    echo Compilando sem log detalhado...
    javac -encoding UTF-8 -d %BIN_DIR% -sourcepath %SRC_DIR% %SRC_DIR%\%PKG%\App.java
) else if "%opcao_build%"=="2" (
    REM Compilar com log detalhado na tela
    echo Compilando com log detalhado na tela...
    javac -verbose -encoding UTF-8 -d %BIN_DIR% -sourcepath %SRC_DIR% %SRC_DIR%\%PKG%\App.java
) else if "%opcao_build%"=="3" (
    REM Compilar normalmente e salvar log em arquivo
    echo Compilando e salvando log em build_log.txt...
    javac -verbose -encoding UTF-8 -d %BIN_DIR% -sourcepath %SRC_DIR% %SRC_DIR%\%PKG%\App.java > build_log.txt 2>&1
    echo Log salvo em build_log.txt
) else (
    REM Opção inválida - compilar normalmente
    echo Opcao invalida! Compilando sem log...
    javac -encoding UTF-8 -d %BIN_DIR% -sourcepath %SRC_DIR% %SRC_DIR%\%PKG%\App.java
)

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
