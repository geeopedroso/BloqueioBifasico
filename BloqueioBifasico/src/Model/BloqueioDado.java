/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author geovani
 */
public class BloqueioDado {

    private Transacao transacao;
    private Dado dado;
    private Bloqueio tipoBloqueio;

    public BloqueioDado(Transacao transacao, Dado dado, Bloqueio bloqueio) {
        this.dado = dado;
        this.transacao = transacao;
        this.tipoBloqueio = bloqueio;
    }

    /*
    Função que verifica se o dado fornecido está bloqueado.
     */
    public static boolean estaBloqueado(LinkedList<BloqueioDado> bloqueados, Dado d) {

        for (BloqueioDado b : bloqueados) {
            if (b.getDado().getNome().equals(d.getNome())) {
                return true;
            }
        }
        return false;
    }

    public static boolean estaBloqueadoCompartilhado(LinkedList<BloqueioDado> bloqueados, Dado d) {

        for (BloqueioDado b : bloqueados) {
            if (b.getDado().getNome().equals(d.getNome()) && b.tipoBloqueio.equals(Bloqueio.COMPARTILHADO)) {
                return true;
            }
        }
        return false;
    }

    public static boolean estaBloqueadoExclusivo(LinkedList<BloqueioDado> bloqueados, Dado d) {

        for (BloqueioDado b : bloqueados) {
            if (b.getDado().getNome().equals(d.getNome()) && b.tipoBloqueio.equals(Bloqueio.EXCLUSIVO)) {
                return true;
            }
        }
        return false;
    }

    public static void setaExclusivo(LinkedList<BloqueioDado> bloqueados, Dado d) {

        for (BloqueioDado b : bloqueados) {
            if (b.getDado().getNome().equals(d.getNome()) && b.tipoBloqueio.equals(Bloqueio.COMPARTILHADO)) {
                b.tipoBloqueio = Bloqueio.EXCLUSIVO;
            }
        }
    }

    /*
    Função que verifica se a transação informada é dono do tipoBloqueio do dado também informado.
     */
    public static boolean donoDoBloqueio(LinkedList<BloqueioDado> bloqueados, Dado d, Transacao t) {

        for (BloqueioDado b : bloqueados) {
            if (b.getDado().getNome().equals(d.getNome())) {
                if (t.getMeuIndice().equals(b.getTransacao().getMeuIndice())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /*
    Funcao que retorna trançacao dona do bloqueio
    */
    public static Transacao donoDoBloqueio(LinkedList<BloqueioDado> bloqueados, Dado d) {
        Transacao t = null;
        for (BloqueioDado b : bloqueados) {
            if (b.getDado().getNome().equals(d.getNome())) {
                t =  b.getTransacao();
                
                
            }
        }
        return t;
    }

    /*
    Função que retira da lista de bloqueados, os dados que foram bloqueados por uma transação.
     */
    public static LinkedList<Dado> desbloqueia(LinkedList<BloqueioDado> bloqueados, Integer indiceTransacao) {
        LinkedList<Dado> dados = new LinkedList<>();
        Iterator<BloqueioDado> bloqueioIterator = bloqueados.iterator();
        while (bloqueioIterator.hasNext()) {
            BloqueioDado b = bloqueioIterator.next();
            if (b.getTransacao().getMeuIndice().equals(indiceTransacao)) {
                Dado d = new Dado(b.getDado().getNome());
                dados.add(d);
                bloqueioIterator.remove();

            }
        }
        return dados;
    }

    public static boolean Contem(LinkedList<Dado> dados, Dado dado) {

        for (Dado d : dados) {
            if (d.getNome().equals(dado.getNome())) {
                return true;
            }
        }
        return false;
    }

    /*
    Função que devolve as transações que estava em espera para a lista de executando, a partir do dado desbloqueado
    recentemente.
     */
    public static void acorda(LinkedList<Operacao> emEspera, LinkedList<Dado> dadosDesbloqueados, LinkedList<Operacao> executando) {

        Collections.reverse(emEspera);

        Iterator<Operacao> iteratorEmEspera = emEspera.iterator();
        while (iteratorEmEspera.hasNext()) {
            Operacao operacao = iteratorEmEspera.next();
            if (Contem(dadosDesbloqueados, operacao.getDado()) || operacao.getAcesso().equals(Acesso.END)) {
                Transacao t = new Transacao(operacao.getTransacao().getMeuIndice());
                Dado dado = new Dado(operacao.getDado().getNome());
                Acesso acesso = operacao.getAcesso();
                executando.add(0, new Operacao(acesso, t, dado));
                iteratorEmEspera.remove();
            }
        }

        Collections.reverse(emEspera);
    }

    public Dado getDado() {
        return dado;
    }

    public void setDado(Dado dado) {
        this.dado = dado;
    }

    public Transacao getTransacao() {
        return transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }

    public Bloqueio getBloqueio() {
        return tipoBloqueio;
    }

    public void setBloqueio(Bloqueio bloqueio) {
        this.tipoBloqueio = bloqueio;
    }

}
