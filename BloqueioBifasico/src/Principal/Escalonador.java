/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import DAO.TransacaoDAO;
import Model.Acesso;
import Model.Bloqueio;
import Model.BloqueioDado;
import Model.Dado;
import Model.Operacao;
import Model.Schedule;
import Model.Transacao;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author geovani
 */
public class Escalonador {

    public static void escalonar() {
        Schedule executar = new Schedule();
        executar.setScheduleInList(TransacaoDAO.buscarTransacoes());

//        for(Operacao p: executar.getScheduleInList()){
//            System.out.println("T"+p.getTransacao().getMeuIndice()+" "+ p.getAcesso().toString()+"("+ p.getDado().getNome()+")");
//        }
        Schedule emEspera = new Schedule();
        Schedule terminado = new Schedule();
        LinkedList<BloqueioDado> bloqueados = new LinkedList<>();

        while (!executar.getScheduleInList().isEmpty()) {
            Transacao transacao = executar.getScheduleInList().get(0).getTransacao();
            Dado dado = executar.getScheduleInList().get(0).getDado();
            Acesso acesso = executar.getScheduleInList().get(0).getAcesso();

            switch (acesso) {
                case BEGIN:
                    executar.getScheduleInList().remove(0);

                    break;
                case READ:
                    if (BloqueioDado.estaBloqueado(bloqueados, dado)) {

                        if (!BloqueioDado.donoDoBloqueio(bloqueados, dado, transacao)
                                && BloqueioDado.estaBloqueadoExclusivo(bloqueados, dado)) {
                            System.out.println("nao sou dono do bloqueio" + "\n meu nome: " + executar.getScheduleInList().get(0).getTransacao().getMeuIndice());
                            emEspera.getScheduleInList().add(executar.getScheduleInList().remove(0));
                        } else {
                            terminado.getScheduleInList().add(executar.getScheduleInList().remove(0));
                        }

                    } else {
                        bloqueados.add(new BloqueioDado(transacao, dado, Bloqueio.COMPARTILHADO));
                        terminado.getScheduleInList().add(executar.getScheduleInList().remove(0));

                    }

                    break;

                case WRITE:
                    if (BloqueioDado.estaBloqueado(bloqueados, dado)) {
                        if (!BloqueioDado.donoDoBloqueio(bloqueados, dado, transacao)) {
                            emEspera.getScheduleInList().add(executar.getScheduleInList().remove(0));
                        } else {
                            BloqueioDado.setaExclusivo(bloqueados, dado);
                            terminado.getScheduleInList().add(executar.getScheduleInList().remove(0));
                        }

                    } else {
                        bloqueados.add(new BloqueioDado(transacao, dado, Bloqueio.EXCLUSIVO));
                        terminado.getScheduleInList().add(executar.getScheduleInList().remove(0));
                    }
                    break;
                case END:
                    System.out.println("Acabei :  " + transacao.getMeuIndice());
                    LinkedList<Dado> dadosDesbloqueados = BloqueioDado.desbloqueia(bloqueados, transacao.getMeuIndice());

                    executar.getScheduleInList().get(0).setAcesso(Acesso.COMMIT);
                    terminado.getScheduleInList().add(executar.getScheduleInList().remove(0));

                    System.out.println("\n executando antes do acorda");
                    for (Operacao p : executar.getScheduleInList()) {
                        System.out.println("T" + p.getTransacao().getMeuIndice() + " " + p.getAcesso().toString() + "(" + p.getDado().getNome() + ")");
                    }

                    System.out.println("\n em espera antes do acorda ");
                    for (Operacao p : emEspera.getScheduleInList()) {
                        System.out.println("T" + p.getTransacao().getMeuIndice() + " " + p.getAcesso().toString() + "(" + p.getDado().getNome() + ")");
                    }

                    BloqueioDado.acorda(emEspera.getScheduleInList(), dadosDesbloqueados, executar.getScheduleInList());

                    System.out.println("\n executando depois do acorda");
                    for (Operacao p : executar.getScheduleInList()) {
                        System.out.println("T" + p.getTransacao().getMeuIndice() + " " + p.getAcesso().toString() + "(" + p.getDado().getNome() + ")");
                    }

                    System.out.println("\n em espera depois do acorda");
                    for (Operacao p : emEspera.getScheduleInList()) {
                        System.out.println("T" + p.getTransacao().getMeuIndice() + " " + p.getAcesso().toString() + "(" + p.getDado().getNome() + ")");
                    }
                    break;

            }
        }
        System.out.println("\n terminado");
        for (Operacao p : terminado.getScheduleInList()) {
            System.out.println("T" + p.getTransacao().getMeuIndice() + " " + p.getAcesso().toString() + "(" + p.getDado().getNome() + ")");
        }
        System.out.println("\n em espera");
        for (Operacao p : emEspera.getScheduleInList()) {
            System.out.println("T" + p.getTransacao().getMeuIndice() + " " + p.getAcesso().toString() + "(" + p.getDado().getNome() + ")");
        }

        System.out.println("\n dados bloqueados");
        for (BloqueioDado b : bloqueados) {
            System.out.println("T" + b.getTransacao().getMeuIndice() + " "
                    + "(" + b.getDado().getNome() + ")" + b.getBloqueio().toString());
        }

    }
}
