# Estrutura do Projeto Jumpman

## Visão Geral

O projeto Jumpman segue uma arquitetura modular e organizada por responsabilidades, seguindo os princípios SOLID e boas práticas de programação Java.

## Estrutura de Diretórios

```
jumpman/
├── bin/                   # Arquivos compilados
├── lib/                   # Bibliotecas externas
├── src/                   # Código fonte
│   └── br/com/jumpman/    # Pacote principal
│       ├── core/          # Classes principais de inicialização e configuração
│       ├── data/          # Dados e constantes
│       │   └── enums/     # Tipos enumerados
│       ├── entities/      # Entidades do jogo (jogador, itens, etc.)
│       ├── fx/            # Efeitos visuais e gráficos
│       ├── platforms/     # Sistema de plataformas
│       ├── screens/       # Interfaces gráficas
│       ├── stages/        # Estágios do jogo
│       ├── timer/         # Sistema de temporização
│       └── utils/         # Classes utilitárias
├── resources/             # Recursos (imagens, sons, etc.)
├── config.properties      # Arquivo de configurações
├── .github/               # Configurações e documentação do GitHub
└── *.md                   # Arquivos de documentação
```

## Componentes Principais

### 1. Core (br.com.jumpman.core)

Classes responsáveis pela inicialização e configuração do jogo.

| Classe | Descrição |
|--------|-----------|
| `GameConfig` | Gerenciador de configurações do jogo |
| `GameEngine` | Motor principal do jogo |
| `GameLauncher` | Inicializador de diferentes modos do jogo |

### 2. Data (br.com.jumpman.data)

Classes para armazenamento e manipulação de dados do jogo.

| Subpacote/Classe | Descrição |
|------------------|-----------|
| `enums` | Tipos enumerados (TipoMovimento, StatusMovimento, Period) |
| `Constants` | Constantes globais do jogo |

### 3. Entities (br.com.jumpman.entities)

Entidades do jogo como personagens, objetos interativos, etc.

| Classe | Descrição |
|--------|-----------|
| `AbstractEntity` | Interface base para todas as entidades |
| `Interactable` | Interface para objetos interativos |
| `Player` | Jogador controlado pelo usuário |
| `Key` | Chave coletável |
| `Door` | Porta que pode ser aberta com chave |
| `Elevator` | Sistema de elevador |
| `Inventory` | Inventário do jogador |

### 4. FX (br.com.jumpman.fx)

Efeitos visuais e componentes gráficos.

| Classe | Descrição |
|--------|-----------|
| `GameWeatherControl` | Controle de efeitos climáticos |
| `RainParticle` | Partículas de chuva |
| `LightningEffect` | Efeito de raio |

### 5. Platforms (br.com.jumpman.platforms)

Sistema de plataformas do jogo.

| Classe | Descrição |
|--------|-----------|
| `AbstractPlatform` | Classe base para plataformas |
| `PlataformaSimples` | Plataforma estática simples |
| `PlataformaDinamica` | Plataforma dinâmica (móvel ou que cai) |

### 6. Screens (br.com.jumpman.screens)

Interfaces gráficas do jogo.

| Classe | Descrição |
|--------|-----------|
| `AbstractGameScreen` | Interface base para todas as telas |
| `MainFrame` | Janela principal do jogo |
| `ScreenInitial` | Tela inicial |
| `ScreenSplash` | Tela de splash |
| `ScreenConfig` | Tela de configurações |
| `ScreenCombat` | Tela de combate |
| (outras telas) | Diversas telas do jogo |

### 7. Stages (br.com.jumpman.stages)

Estágios jogáveis do jogo.

| Classe | Descrição |
|--------|-----------|
| `AbstractStage` | Classe base para estágios |
| `InteractionStage` | Estágio para teste de interações |
| `WeatherTestStage` | Estágio para teste de clima |

### 8. Timer (br.com.jumpman.timer)

Sistema de temporização do jogo.

| Classe | Descrição |
|--------|-----------|
| `GameTimerCountdown` | Temporizador de contagem regressiva |
| `GameTimerManager` | Gerenciador de temporizadores |
| `GameTimerTrigger` | Gatilho para eventos temporizados |
| `PeriodTimer` | Temporizador para ciclo dia/tarde/noite |

### 9. Utils (br.com.jumpman.utils)

Classes utilitárias.

| Classe | Descrição |
|--------|-----------|
| `CollisionDetector` | Detector de colisões |
| `AssetManager` | Gerenciador de recursos |

## Arquivos de Configuração

| Arquivo | Descrição |
|---------|-----------|
| `config.properties` | Configurações do jogo |

## Documentação

| Arquivo | Descrição |
|---------|-----------|
| `README.md` | Visão geral do projeto |
| `INSTRUCTIONS.md` | Instruções para compilação e execução |
| `GDD.md` | Documento de design do jogo |
| `INTERACTION_SYSTEM.md` | Documentação do sistema de interação |
| `PROJECT_STRUCTURE.md` | Este arquivo - documentação da estrutura do projeto |
| `TODO.md` | Lista de tarefas pendentes |

## Padrões de Projeto Utilizados

1. **Singleton**: Utilizado em classes como GameConfig e GameEngine.
2. **Strategy**: Implementado para diferentes comportamentos de entidades.
3. **Factory**: Para criação de objetos em contextos específicos.
4. **Observer**: Para notificação de eventos entre componentes.
5. **State**: Para gerenciar os diferentes estados do jogo.

## Convenções de Código

- Indentação com 4 espaços (sem tabs)
- Uso de aspas duplas para strings
- Chaves de métodos e condicionais em linha separada
- Comentários Javadoc para classes e métodos públicos
- Padrões de nomenclatura Java (camelCase para métodos e variáveis)
- Enums em pacote dedicado data.enums
