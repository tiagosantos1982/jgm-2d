package br.com.jumpman.entities;

/**
 * Interface para objetos com os quais o jogador pode interagir
 * Define o contrato para entidades que suportam intera��o do jogador
 */
public interface Interactable {
    
    /**
     * M�todo chamado quando o jogador interage com este objeto
     * Implementa a l�gica espec�fica de intera��o para cada tipo de objeto
     * 
     * @param player O jogador que est� interagindo
     * @return true se a intera��o foi bem-sucedida, false caso contr�rio
     */
    boolean interact(AbstractEntity player);
    
    /**
     * Verifica se o objeto pode ser interagido no momento atual
     * Utilizado para determinar se a intera��o � poss�vel antes de tentar execut�-la
     * 
     * @param player O jogador que deseja interagir
     * @return true se o objeto pode ser interagido, false caso contr�rio
     */
    boolean canInteract(AbstractEntity player);
}
