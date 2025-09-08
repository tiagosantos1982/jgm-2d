# Instruções para Execução e Desenvolvimento do Projeto Jumpman

## Requisitos do Sistema
- Java 8 ou superior
- Sistema operacional Windows, macOS ou Linux

## Estrutura do Projeto
```
jumpman/
├── bin/                   # Arquivos compilados
├── lib/                   # Bibliotecas externas
├── src/                   # Código fonte
│   └── br/com/jumpman/    # Pacote principal
│       ├── core/          # Classes principais de inicialização
│       ├── data/enums/    # Enumerações
│       ├── entities/      # Entidades do jogo
│       ├── fx/            # Efeitos visuais
│       ├── platforms/     # Sistema de plataformas
│       ├── screens/       # Interfaces gráficas
│       ├── stages/        # Estágios do jogo
│       ├── timer/         # Sistema de temporização
│       └── utils/         # Classes utilitárias
└── resources/             # Recursos (imagens, sons, etc.)
```

Para uma descrição detalhada da estrutura do projeto, consulte [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md).

## Como Compilar o Projeto
Utilize o comando a seguir para compilar todos os arquivos:

```bash
javac -d bin -cp lib/*;bin src/br/com/jumpman/App.java
```

Ou para compilar arquivos específicos:

```bash
javac -d bin -cp lib/*;bin src/br/com/jumpman/screens/InteractTestStage.java
```

## Como Executar o Projeto
Para executar o jogo após compilado:

```bash
java -cp "bin;lib/*" br.com.jumpman.App
```

## Modos de Execução

O projeto possui três modos de execução configuráveis no arquivo `App.java` através da classe `GameLauncher`:

1. **Modo de Interação** (atual):
   - Teste do sistema de interação com elevador, portas e chaves
   - Utilize `GameLauncher.launch(LaunchMode.INTERACTION_TEST);`

2. **Modo de Clima**:
   - Teste do sistema climático com chuva, relâmpagos e efeitos visuais
   - Utilize `GameLauncher.launch(LaunchMode.WEATHER_TEST);`

3. **Modo Original**:
   - Jogo completo com todas as funcionalidades
   - Utilize `GameLauncher.launch(LaunchMode.NORMAL);`

## Controles do Jogo

### Modo de Interação:
- **Setas esquerda/direita**: Mover o jogador
- **Espaço**: Pular
- **E**: Interagir com objetos (chaves, portas, elevador)

### Modo de Clima:
- **Setas esquerda/direita**: Mover o jogador
- **Espaço**: Pular
- **R**: Ativar/desativar chuva
- **L**: Disparar relâmpago
- **N/D/T**: Alternar período (Noite/Dia/Tarde)

## Desenvolvimento de Novas Funcionalidades

### Adicionando Novas Plataformas:
1. Para plataformas simples, use a classe `platforms.PlataformaSimples`:
   ```java
   import br.com.jumpman.platforms.PlataformaSimples;
   
   PlataformaSimples platform = new PlataformaSimples(x, y, width, height);
   ```

2. Para plataformas dinâmicas/elevadores, use a classe `platforms.PlataformaDinamica`:
   ```java
   import br.com.jumpman.platforms.PlataformaDinamica;
   import java.util.ArrayList;
   
   // Para elevador:
   ArrayList<PlataformaDinamica.Ponto> pontos = new ArrayList<>();
   pontos.add(new PlataformaDinamica.Ponto(x1, y1, delaySegundos));
   pontos.add(new PlataformaDinamica.Ponto(x2, y2, delaySegundos));
   PlataformaDinamica elevador = new PlataformaDinamica(x, y, width, height, pontos, true, velocidade);
   
   // Para plataforma que cai:
   PlataformaDinamica plataforma = new PlataformaDinamica(x, y, width, height, tempoParaCair, visibleAfterFall);
   ```

### Adicionando Novos Objetos Interativos:
1. Crie uma nova classe no pacote `entities` implementando as interfaces `AbstractEntity` e `Interactable`
2. Implemente os métodos obrigatórios: `update()`, `draw()`, `getBounds()`, `interact()`, `canInteract()`
3. Adicione a lógica de interação específica do seu objeto
4. Integre o novo objeto no estágio apropriado

## Resolução de Problemas Comuns

### Caracteres Especiais Corrompidos:
- Certifique-se de salvar todos os arquivos em UTF-8 sem BOM
- Evite usar acentos em comentários e strings

### Erro "Cannot find symbol":
- Verifique se o arquivo está compilado no diretório bin
- Verifique os imports necessários
- Compile na ordem correta (primeiro as classes base)

### Controles Não Respondem:
- Verifique se o foco está na janela do jogo
- Confirme que o KeyBinding está configurado corretamente
- Use `requestFocusInWindow()` após inicializar componentes

## Boas Práticas de Código

1. **Organização**: Manter classes em arquivos separados
2. **Indentação**: 4 espaços (sem tabs)
3. **Comentários**: Documentar métodos e lógicas complexas
4. **Encapsulamento**: Usar modificadores de acesso adequados
5. **Nomeação**: Usar convenções Java (camelCase para métodos e variáveis)
6. **Modularidade**: Separar responsabilidades em classes distintas

## Recursos Adicionais
- Para mais detalhes sobre o design do jogo, consulte `GDD.md`
- Para documentação técnica do sistema de interação, consulte `INTERACTION_SYSTEM.md`
- Para relatório de melhorias implementadas, consulte `RELATORIO_V1.0.md`

---

© 2025 Jumpman - Todos os direitos reservados
