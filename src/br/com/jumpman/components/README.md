# Components Package

Este pacote contém componentes reutilizáveis para a interface do jogo.

## Componentes disponíveis

### TimerDisplay

`TimerDisplay` é um componente que exibe um temporizador regressivo com um ícone. 
O timer inicia com um valor em segundos e vai decrementando até chegar a zero.

#### Características:

- Exibe um ícone de relógio (carrega de arquivo ou gera um padrão)
- Mostra o tempo no formato mm:ss
- O texto pisca em vermelho quando o tempo está acabando (menos de 10 segundos)
- Pode ser pausado, retomado e reiniciado

#### Como usar:

```java
// Criar o timer (60 segundos) na posição (x=650, y=30) com ícone de 32x32
TimerDisplay timerDisplay = new TimerDisplay(
    "caminho/para/icone.png", // Caminho do ícone
    60,                       // Tempo inicial em segundos
    650,                      // Posição X
    30,                       // Posição Y
    32                        // Tamanho do ícone
);

// No método paintComponent
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    
    // Desenhar outros elementos...
    
    // Desenhar o timer
    timerDisplay.draw(g2);
}

// Ao fechar/remover o painel
@Override
public void removeNotify() {
    if (timerDisplay != null) {
        timerDisplay.dispose(); // Liberar recursos
    }
    super.removeNotify();
}
```

#### Métodos principais:

- `draw(Graphics g)`: Desenha o componente
- `resetTimer(int seconds)`: Reinicia o timer com um novo valor
- `pauseTimer()`: Pausa o timer
- `resumeTimer()`: Retoma o timer
- `getTimeSeconds()`: Retorna o tempo atual em segundos
- `dispose()`: Libera os recursos quando o componente não é mais necessário
