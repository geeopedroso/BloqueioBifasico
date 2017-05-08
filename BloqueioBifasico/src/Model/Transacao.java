/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.LinkedList;

/**
 *
 * @author geovani
 */
public class Transacao {

    private Integer meuIndice;
    private LinkedList<Operacao> filaOperacoes;

    public Transacao(Integer indice) {
        filaOperacoes = new LinkedList<>();
        this.meuIndice = indice;
    }

    public static void preencherTransacoes(LinkedList<Operacao> operacoes, Transacao t) {
        for(Operacao op: operacoes){
            if(t.meuIndice.equals(op.getIndice())){
                t.filaOperacoes.add(op);
            }
        }
    }

    public Integer getMeuIndice() {
        return meuIndice;
    }

    public void setMeuIndice(Integer meuIndice) {
        this.meuIndice = meuIndice;
    }

    public LinkedList<Operacao> getFilaOperacoes() {
        return filaOperacoes;
    }

    public void setFilaOperacoes(LinkedList<Operacao> filaOperacoes) {
        this.filaOperacoes = filaOperacoes;
    }

}
