/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author geovani
 */
public class Schedule {

    private LinkedList<Operacao> scheduleInList;

    public Schedule() {
        scheduleInList = new LinkedList<>();

    }

    public static List<Integer> Contem(List<Operacao> lista, Transacao t) {
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getTransacao().getMeuIndice().equals(t.getMeuIndice())) {
                indices.add(i);

            }

        }
        return indices;
    }
    
    public static LinkedList<Operacao> Está(List<Operacao> lista, Transacao t) {
        LinkedList<Operacao> operacoes = new LinkedList<>();

        for (Operacao operacao: lista) {
            if (operacao.getTransacao().getMeuIndice().equals(t.getMeuIndice())) {
                operacoes.add(operacao); 
            }

        }
        return operacoes;
    }

    public LinkedList<Operacao> getScheduleInList() {
        return scheduleInList;
    }

    public void setScheduleInList(LinkedList<Operacao> scheduleInList) {
        this.scheduleInList = scheduleInList;
    }

    public void addRetorno(LinkedList<Operacao> retorn) {

        retorn.addAll(this.getScheduleInList());
        this.scheduleInList = new LinkedList<Operacao>();
        this.scheduleInList.addAll(retorn);
    }
;

}
