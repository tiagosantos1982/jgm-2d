# Game Design Document (GDD) — Reino das Justas

## Visão Geral

**Título:** Reino das Justas
**Gênero:** Competição medieval tática
**Plataforma:** PC (protótipo Java)

## Contexto

No reino das Justas, cavaleiros de diferentes regiões competem em times para conquistar o título de melhor cavaleiro. Cada região possui times com habilidades únicas, e o caminho do jogador passa por todas as regiões do reino, enfrentando NPCs e times rivais.

## Objetivo

Roubar a bandeira do time adversário. O jogo termina se a bandeira for tomada à força ou se todos os cavaleiros de um time forem derrotados.

## Estrutura de Times
- Cada time possui de 3 a 5 cavaleiros.
- Cavaleiros têm habilidades diferentes (força, agilidade, habilidades especiais).
- 1 cavaleiro é o líder, com habilidade única.
- Habilidades podem ser adquiridas ou evoluídas.

## Regiões do Reino
- O jogador avança por diferentes regiões, cada uma com times rivais e desafios únicos.
- NPCs podem se tornar jogáveis futuramente.

## Sistema de Combate
- Cada rodada é composta por 3 ações:
  1. **Escolha do personagem:** O vencedor pode ser substituído ou permanecer. Substituição só acontece para o vencedor e o personagem substituído só pode ser selecionado após todos os outros lutarem.
  2. **Setup inicial:** Escolha de ações/habilidades (mínimo 2, máximo 3). Nos níveis iniciais começa com 1, novas ações são adquiridas com pontos de habilidade.
  3. **Combate:** Os cavaleiros se enfrentam, e o resultado depende de força, agilidade e habilidades.
- O combate pode ser vencido por:
  - Redução dos pontos de vida do adversário a zero.
  - Forçar o adversário a fugir (habilidade especial).

## Bandeira
- Roubar a bandeira é uma ação especial:
  - Só pode ser tentada na primeira rodada (Dead Match).
  - Se falhar, o jogo segue normalmente.
  - Se bem-sucedida, o jogo termina imediatamente.
  - Pode ser oferecida como rodada bônus em situações especiais.

## Progressão
- O jogador avança por regiões, enfrentando times cada vez mais fortes.
- Pontos de habilidade são ganhos por vitórias e usados para adquirir novas ações/habilidades.
- Cavaleiros podem ser evoluídos ou substituídos.

## Situações Especiais
- Se todos os cavaleiros de um time forem derrotados, o outro time vence.
- Se a bandeira for tomada, o jogo termina, independentemente dos cavaleiros restantes.

## Ideias para Tornar a Gameplay Divertida
- **Habilidades únicas e combos:** Incentive o uso estratégico de habilidades e combinações entre cavaleiros.
- **Eventos dinâmicos:** Clima, terreno e eventos aleatórios podem influenciar o combate.
- **Sistema de moral:** Cavaleiros podem ficar desmotivados ou inspirados, afetando desempenho. Um lider forte eleva a moral dos seus liderados. Isso pode ser usado no inicio de cada rodada onde um lider provoca o outro com o objetivo de fazer ele se sentir humilhado. faz perder ponto de energia, não de vida de todos os cavaleiros.
- **Desafios de região:** Cada região pode ter regras ou obstáculos próprios.
- **Personalização:** Permita ao jogador customizar e evoluir seus cavaleiros.
- **Modo história:** Adicione narrativa e rivalidades entre regiões.
- **Ranking:** Sistema de pontuação e títulos para os melhores cavaleiros.

## Loop de Gameplay
1. Escolha do time e cavaleiros.
2. Avanço para a próxima região.
3. Rodada de combate:
   - Escolha de personagem
   - Setup de ações
   - Combate
   - Tentativa de roubo de bandeira (se aplicável)
4. Pontuação e evolução.
5. Repetir até conquistar todas as regiões ou perder.

## Mockup de Tela
- **Mapa do reino:** Mostra progresso pelas regiões.
- **Tela de combate:** Exibe times, cavaleiros, habilidades e bandeira.
- **Tela de evolução:** Permite gastar pontos de habilidade.

## Fluxos de Tela

### 1. Tela Inicial
- Exibe logo do jogo e opções: Iniciar, Carregar, Configurações, Sair.
- Botão para começar nova campanha ou continuar.

### 2. Seleção de Time
- Jogador escolhe sua região e monta o time de cavaleiros.
- Exibe atributos, habilidades e aparência dos cavaleiros.
- Permite customização inicial.
- Botão: Avançar para Mapa.

### 3. Mapa do Reino
- Mostra todas as regiões do reino e progresso do jogador.
- Regiões desbloqueadas ficam destacadas.
- Botão: Entrar na região (inicia combate).
- Acesso à tela de evolução e inventário.

### 4. Tela de Evolução
- Exibe cavaleiros do time, pontos de habilidade disponíveis.
- Permite gastar pontos para evoluir atributos ou comprar habilidades.
- Botão: Voltar ao Mapa.

### 5. Tela de Combate
- Exibe times adversários, cavaleiros, bandeira e terreno.
- Fases do combate:
  1. Escolha do personagem para a rodada.
  2. Setup de ações/habilidades.
  3. Execução do combate (animações, resultados).
  4. Tentativa de roubo de bandeira (se aplicável).
- Exibe barra de vida, energia, moral e habilidades ativas.
- Botão: Substituir cavaleiro (se permitido).
- Botão: Tentar roubar bandeira (se permitido).
- Botão: Avançar para próxima rodada.

### 6. Tela de Resultado
- Exibe vencedor da rodada, status dos cavaleiros, pontos ganhos.
- Botão: Voltar ao Mapa ou Avançar para próxima região.

### 7. Tela de Configurações
- Ajustes de áudio, vídeo, controles e idioma.
- Botão: Voltar à tela inicial.

### 8. Tela de História/Narrativa
- Exibe diálogos, rivalidades e eventos especiais entre regiões.
- Botão: Avançar narrativa ou pular.

## Futuro
- NPCs jogáveis
- Multiplayer
- Novas habilidades e modos de jogo

---
**Surpreenda-se! O Reino das Justas está só começando.**
