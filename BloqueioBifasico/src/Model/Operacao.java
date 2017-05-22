/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author geovani
 */
public class Operacao {
    
	private Acesso acesso;
	private Transacao transacao;
	private Dado dado;

    public Operacao(Acesso acesso, Transacao transacao, Dado dado) {
        this.acesso = acesso;
        this.transacao = transacao;
        this.dado = dado;
    }

    

    public Acesso getAcesso() {
        return acesso;
    }

    public void setAcesso(Acesso acesso) {
        this.acesso = acesso;
    }

    public Transacao getTransacao() {
        return transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }

    public Dado getDado() {
        return dado;
    }

    public void setDado(Dado dado) {
        this.dado = dado;
    }
	
        
        
	
	
    
}
