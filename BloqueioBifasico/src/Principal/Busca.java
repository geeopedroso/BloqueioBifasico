/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import DAO.TransacaoDAO;
import Model.Operacao;
import java.util.LinkedList;

/**
 *
 * @author geovani
 */
public class Busca extends Thread {
    public Integer run(LinkedList<Operacao> operacoes, Integer indice) throws InterruptedException{
        //System.out.println("sleep");
        Thread.sleep(50);
        
        TransacaoDAO.buscarTransacoes(operacoes, indice);
        indice = TransacaoDAO.pegarUltimoIndice();
       // System.out.println("Indice do thread: " + indice);
        
         return indice;   
        }
    
     
   
    
    
}
