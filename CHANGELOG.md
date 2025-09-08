# Changelog do Projeto Jumpman

Este arquivo documenta todas as alterações significativas feitas no projeto Jumpman.

## [1.2.0] - Setembro 2025

### Adicionado
- Sistema completo de interação com objetos
- Elevadores funcionais com ativação por chave
- Portas que podem ser abertas com chaves específicas
- Inventário visual com limite de 3 slots
- Sistema de chaves coloridas com funções específicas
- Arquivo de configuração `config.properties`
- Arquivo de instruções detalhadas `INSTRUCTIONS.md`

### Alterado
- Reorganização completa da estrutura do código em pacotes por funcionalidade
- Extraído enum `TipoMovimento` para o pacote `data.enums`
- Extraído enum `StatusMovimento` para o pacote `data.enums`
- Extraído enum `Period` para o pacote `data.enums`
- Convertido classes internas em classes independentes:
  - `Key` (de InteractTestStage)
  - `Door` (de InteractTestStage)
  - `Elevator` (de InteractTestStage)
  - `Inventory` (de InteractTestStage)
- Movido classes para pacotes adequados:
  - `Player` → `entities`
  - `PlataformaSimples` → `platforms`
  - `PlataformaDinamica` → `platforms`
- Implementado inicialização modular com `GameLauncher` e modos de execução

### Corrigido
- Problemas de codificação de caracteres especiais
- Estrutura de pacotes inconsistente
- Problemas de encapsulamento em várias classes
- Código duplicado em várias partes do projeto

## [1.1.0] - Agosto 2025

### Adicionado
- Sistema de clima com chuva e raios
- Efeitos visuais como partículas e gradientes
- Diferentes períodos do dia (manhã, tarde, noite)
- Sistema de verificação de colisão aprimorado
- Sandbox para teste do sistema climático

### Alterado
- Melhorias no sistema de pulo do jogador
- Refatoração da lógica de renderização
- Performance aprimorada no ciclo de jogo

## [1.0.0] - Julho 2025

### Adicionado
- Primeira versão do jogo
- Sistema básico de plataformas
- Personagem com movimentação e pulo
- Telas principais (splash, menu, jogo)
- Estrutura base do projeto
