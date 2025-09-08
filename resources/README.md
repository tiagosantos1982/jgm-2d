# Recursos do Jogo

Este diretório contém todos os recursos usados pelo jogo, organizados por tipo.

## Estrutura:

- `images/`: Contém imagens, texturas e sprites do jogo
  - `timer_icon.txt`: Arquivo placeholder para o ícone do timer. Em uma implementação completa, seria substituído por uma imagem real.

## Carregamento de Recursos:

Os recursos são carregados utilizando a classe `br.com.jumpman.utils.ResourceLoader` que:

1. Tenta carregar o recurso a partir do sistema de arquivos
2. Se falhar, tenta carregar do classpath de recursos
3. Em caso de falha em ambas as tentativas, retorna null ou gera um recurso padrão
