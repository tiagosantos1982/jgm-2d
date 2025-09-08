#!/bin/bash

echo "Jumpman - Iniciando o jogo..."
echo
echo "Lendo as instruções..."
echo
cat INSTRUCTIONS.md
echo
echo
echo "Pressione Enter para compilar e iniciar o jogo..."
read

echo "Compilando..."
javac -d bin -cp "lib/*:bin" src/br/com/jumpman/App.java

echo
echo "Executando..."
java -cp "bin:lib/*" br.com.jumpman.App

echo
echo "Jogo encerrado."
read -p "Pressione Enter para sair..."
