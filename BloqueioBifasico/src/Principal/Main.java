/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import DAO.TransacaoDAO;
import static DAO.TransacaoDAO.buscarTransacoes;
import Model.Operacao;
import java.util.LinkedList;

/**
 *
 * @author geovani
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Escalonador.escalonar();
    }
}
