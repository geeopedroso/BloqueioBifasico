/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author geovani
 */
public class Schedule {
    private LinkedList<Operacao> scheduleInList;

    public Schedule(LinkedList<Transacao> transacoes) {
        scheduleInList = new LinkedList<>();
        
    }

    

    public LinkedList<Operacao> getScheduleInList() {
        return scheduleInList;
    }

    public void setScheduleInList(LinkedList<Operacao> scheduleInList) {
        this.scheduleInList = scheduleInList;
    }
    
    public void addRetorno(LinkedList<Operacao> retorn){
        
        retorn.addAll(this.getScheduleInList());
        this.scheduleInList = new LinkedList<Operacao>();
        this.scheduleInList.addAll(retorn);
    };

    
}
