@echo off
echo Jumpman - Iniciando o jogo...
echo.
echo Lendo as instrucoes...
echo.
type INSTRUCTIONS.md
echo.
echo.
echo Pressione qualquer tecla para compilar e iniciar o jogo...
pause > nul

echo Compilando...
javac -d bin -cp "lib/*;bin" src\br\com\jumpman\App.java

echo.
echo Executando...
java -cp "bin;lib/*" br.com.jumpman.App

echo.
echo Jogo encerrado.
pause
