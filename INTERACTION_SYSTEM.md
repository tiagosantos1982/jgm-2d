# Sistema de Interação - Documentação Técnica

## 📍 **Sandbox Atual: InteractTestStage**
- **Arquivo**: `src/br/com/jumpman/screens/InteractTestStage.java`
- **Status**: ✅ FUNCIONAL - Implementação completa
- **Última atualização**: Setembro 2025

## 🎮 **Controles Implementados**

### KeyBindings (Robusto)
```java
// Arquitetura utilizada
getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
getActionMap()
```

**Vantagens sobre KeyListener:**
- ✅ Não perde foco da janela
- ✅ Funciona mesmo com múltiplos componentes
- ✅ Mais responsivo e confiável

### Mapeamento de Teclas
| Tecla | Ação | Implementação |
|-------|------|---------------|
| `←` | Mover esquerda | `left = true` |
| `→` | Mover direita | `right = true` |
| `Espaço` | Pular | `player.jump()` |
| `E` | Interagir | `tryInteract()` |

## 🗺️ **Layout do Cenário**

### Elementos Posicionados
```
Chave (630,230) ⭐ ← Objetivo
├─ Plataforma 3 (600,250) - NÃO alcançável direto
├─ Plataforma 2 (400,350) - Serve de apoio  
├─ Plataforma 1 (200,450) - Alcançável pulando
├─ Elevador (150,450) - Movimenta entre níveis
└─ Chão (0,550) - Base completa
   Player inicial (100,450)
```

### Puzzle Design
1. **Desafio**: Chave está muito alta para pulo direto
2. **Solução**: Usar plataformas como escada ou o elevador
3. **Sequência**: Plat1 → Plat2 → Plat3 → Chave ou Elevador → Plat3 → Chave
4. **Mecânica**: Aproximar-se + pressionar E

## 🎒 **Sistema de Inventário**

### Especificações
- **Slots máximos**: 3 itens
- **Validação**: Verifica espaço antes de adicionar
- **Feedback**: Mensagem quando inventário cheio
- **Visual**: Interface gráfica com slots desenhados

### Implementação
```java
private static class Inventory {
    private final ArrayList<String> items = new ArrayList<>();
    private static final int MAX_SLOTS = 3;
    
    public boolean addItem(String item) {
        if (items.size() < MAX_SLOTS) {
            items.add(item);
            return true;
        }
        return false;
    }
}
```

## 🔍 **Sistema de Detecção**

### Área de Interação
```java
Rectangle interactionArea = new Rectangle(
    playerBounds.x - 10, playerBounds.y - 10,
    playerBounds.width + 20, playerBounds.height + 20
);
```

**Características:**
- ✅ Área expandida 10px em todas direções
- ✅ Detecção baseada em intersecção de retângulos
- ✅ Validação de visibilidade do objeto

## 🎨 **Objetos Interativos**

### Chave Colorida
```java
private static class Key {
    // Visual: Círculo colorido (azul/amarelo/verde)
    // Efeito: Brilho animado pulsante
    // Estado: visible/invisible
}
```

### Elevador
```java
private static class Elevator {
    // Visual: Plataforma móvel + caixa de controle
    // Estados: activated/deactivated
    // Controle: Botões direcionais (setas cima/baixo)
}
```

**Animação:**
- Brilho senoidal baseado em `System.currentTimeMillis()`
- Alpha variável de 128 a 255
- Círculo expandido para efeito de aura

## 🔧 **Correções Técnicas Aplicadas**

### Problema: Controles não funcionavam
**Causa**: KeyListener perdendo foco
**Solução**: KeyBindings com WHEN_IN_FOCUSED_WINDOW

### Problema: Colisão com plataformas
**Causa**: PlataformaSimples sem método getY()
**Solução**: Uso direto de getBounds().y

### Problema: Player dentro do chão
**Causa**: Posição inicial Y=500 vs chão Y=550  
**Solução**: Player inicial Y=450

## 🚀 **Como Executar o Teste**

### 1. Compilação
```bash
javac -d bin -cp lib/* src/br/com/jumpman/screens/InteractTestStage.java src/br/com/jumpman/App.java
```

### 2. Execução
```bash
java -cp bin br.com.jumpman.App
```

### 3. Teste da Funcionalidade
1. ✅ Mover com setas (←→)
2. ✅ Pular com espaço
3. ✅ Escalar plataformas em sequência
4. ✅ Aproximar da chave dourada
5. ✅ Pressionar E para coletar
6. ✅ Verificar inventário visual

## 📊 **Status de Desenvolvimento**

| Funcionalidade | Status | Observações |
|----------------|--------|-------------|
| Movimento básico | ✅ | KeyBindings funcionais |
| Sistema de pulo | ✅ | Física integrada |
| Colisão com plataformas | ✅ | Detecção precisa |
| Coleta de objetos | ✅ | Chaves coloridas testadas |
| Inventário visual | ✅ | 3 slots com interface |
| Feedback de interação | ✅ | Mensagens funcionais |
| Sistema de elevador | ✅ | Controle manual via tecla E |
| Portas com chaves | ✅ | Sistema de desbloqueio com itens |

## 🎯 **Próximas Expansões**
- [x] Mais tipos de objetos (portas, elevadores)
- [x] Sistema de chaves e fechaduras
- [ ] Múltiplos níveis de puzzle
- [ ] Objetos que requerem sequência específica
- [ ] Integração com sistema climático
- [ ] Mais tipos de plataformas móveis (horizontais, penduradas)
