# JumpMan Game - Documentação Técnica

## Visão Geral
JumpMan é um jogo de plataforma 2D em Java com sistema de puzzles, clima dinâmico e mecânicas de interação.

## 🎮 Sandboxes de Teste Implementados

### 1. **ScreenTestStage** - Teste de Sistema Climático
- **Localização**: `src/br/com/jumpman/screens/ScreenTestStage.java`
- **Funcionalidades**: 
  - Sistema de clima dinâmico com gradientes de céu
  - Partículas de chuva com splash effects
  - Fireflies animados para cenas noturnas
  - Raios com efeitos visuais
  - Controles em tempo real para debugging

**Controles:**
- `R`: Alternar chuva ON/OFF
- `L`: Disparar raio
- `N/D/T`: Mudar período (Noite/Dia/Tarde)
- `1-6`: Ajustar intensidade, densidade e nível do chão

### 2. **InteractTestStage** - Sistema de Interação ✨ ATUAL
- **Localização**: `src/br/com/jumpman/screens/InteractTestStage.java`
- **Funcionalidades**:
  - Sistema de coleta de objetos
  - Inventário visual com 3 slots máximos
  - Mecânicas de puzzle em plataformas
  - Detecção de proximidade para interação
  - Elevador controlado manualmente
  - Portas com desbloqueio por chaves

**Controles (KeyBindings):**
- `←→`: Mover player
- `Espaço`: Pular
- `E`: Interagir com objetos próximos (chaves, portas, elevador)

**Cenário de Teste:**
- Chão completo de uma ponta à outra
- Plataformas em alturas diferentes
- Chaves coloridas (azul, verde, amarela) espalhadas pelo cenário
- Elevador funcional que requer chave azul para ativação
- Porta que requer chave amarela para desbloqueio

## 🔧 Implementações Técnicas

### Sistema de Controles
- **Arquitetura**: KeyBindings com `WHEN_IN_FOCUSED_WINDOW`
- **Vantagem**: Mais robusto que KeyListener, não perde foco
- **Localização**: Método `setupKeyBindings()` em InteractTestStage

### Sistema de Colisão
- **Detecção**: Rectangle.intersects() com verificação de direção
- **Física**: Integração com classe Player existente
- **Precisão**: Distinção entre colisão superior e lateral

### Sistema de Inventário
- **Limite**: 3 slots máximos
- **Visual**: Interface gráfica com slots desenhados
- **Lógica**: Validação de espaço antes de adicionar itens

## 🚀 Como Alternar Entre Testes

### Para Testar Interação (Atual):
```java
// App.java - linha ativa
SwingUtilities.invokeLater(App::showInteractTest);
```

### Para Testar Clima:
```java
// App.java - comentar linha atual e ativar:
SwingUtilities.invokeLater(App::showTestStage);
```

### Para Jogo Original:
```java
// App.java - descomentar bloco original:
SwingUtilities.invokeLater(() -> {
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
    } catch (Exception e) {
        e.printStackTrace();
    }
    new MainFrame().setVisible(true);
});
```

## 📁 Estrutura do Projeto

### Folders
- `src`: Código fonte principal
- `lib`: Dependências
- `bin`: Arquivos compilados

### Classes Principais
- `App.java`: Ponto de entrada com alternância de testes
- `Player.java`: Mecânicas de movimento e física
- `PlataformaSimples.java`: Plataformas básicas para colisão
- `PlataformaDinamica.java`: Plataformas móveis e elevadores
- `GameWeatherControl.java`: Sistema climático completo
- `InteractTestStage.java`: Sandbox de interação atual

## 🎯 Próximos Desenvolvimentos
- Expansão do sistema de inventário
- Novos tipos de objetos interativos
- Mecânicas de puzzle mais complexas
- Integração dos sistemas clima + interação

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
