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
import java.util.List;

/**
 *
 * @author geovani
 */
public class Escalonador {

    public static void escalonar() throws InterruptedException {
        Schedule executar = new Schedule(); //fila de execucao
        executar.setScheduleInList(TransacaoDAO.buscarTransacoes());
        Integer indice = TransacaoDAO.pegarUltimoIndice();
        Busca busca = new Busca();

//        for(Operacao p: executar.getScheduleInList()){
//            System.out.println("T"+p.getTransacao().getMeuIndice()+" "+ p.getAcesso().toString()+"("+ p.getDado().getNome()+")");
//        }
        Schedule emEspera = new Schedule(); //fila de espera das operacoes
        Schedule terminado = new Schedule();//fila de operacoes ja escalonadas
        LinkedList<BloqueioDado> bloqueados = new LinkedList<>();

        while (!executar.getScheduleInList().isEmpty()) {//enquanto tiver algo na lista, escalone

//            System.out.println("indice: " + indice);
//            System.out.println("executar: " + executar.getScheduleInList().size());

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
                            if (Escalonador.deadlock(emEspera.getScheduleInList(), bloqueados, executar.getScheduleInList().get(0))) {
                                    System.out.println("\n\n DEAD LOCK \n\n");
                                    System.exit(0);
                            }
                            emEspera.getScheduleInList().add(executar.getScheduleInList().remove(0));
                        } else {
                            terminado.getScheduleInList().add(executar.getScheduleInList().remove(0));
                            System.out.println("T" + transacao.getMeuIndice() + " " + acesso.toString() + "(" + dado.getNome() + ")");
                        }

                    } else {
                        bloqueados.add(new BloqueioDado(transacao, dado, Bloqueio.COMPARTILHADO));
                        terminado.getScheduleInList().add(executar.getScheduleInList().remove(0));
                        System.out.println("T" + transacao.getMeuIndice() + " " + acesso.toString() + "(" + dado.getNome() + ")");
                    }

                    break;

                case WRITE:
                    if (BloqueioDado.estaBloqueado(bloqueados, dado)) {
                        if (!BloqueioDado.donoDoBloqueio(bloqueados, dado, transacao)) {
                            if (Escalonador.deadlock(emEspera.getScheduleInList(), bloqueados, executar.getScheduleInList().get(0))) {
                                    System.out.println("\n\n DEAD LOCK \n\n");
                                    System.exit(0);
                            }
                            emEspera.getScheduleInList().add(executar.getScheduleInList().remove(0));
                        } else {
                            BloqueioDado.setaExclusivo(bloqueados, dado);
                            terminado.getScheduleInList().add(executar.getScheduleInList().remove(0));
                            System.out.println("T" + transacao.getMeuIndice() + " " + acesso.toString() + "(" + dado.getNome() + ")");
                        }

                    } else {
                        bloqueados.add(new BloqueioDado(transacao, dado, Bloqueio.EXCLUSIVO));
                        terminado.getScheduleInList().add(executar.getScheduleInList().remove(0));
                        System.out.println("T" + transacao.getMeuIndice() + " " + acesso.toString() + "(" + dado.getNome() + ")");
                    }
                    break;
                case END:
                    List<Integer> contem = Schedule.Contem(emEspera.getScheduleInList(), transacao);
                    if (!contem.isEmpty()) {
                        emEspera.getScheduleInList().add((contem.get(contem.size() - 1) + 1),
                                executar.getScheduleInList().remove(0));
                    } else {

                        LinkedList<Dado> dadosDesbloqueados = BloqueioDado.desbloqueia(bloqueados, transacao.getMeuIndice());

                        executar.getScheduleInList().get(0).setAcesso(Acesso.COMMIT);
                        terminado.getScheduleInList().add(executar.getScheduleInList().remove(0));
                        System.out.println("T" + transacao.getMeuIndice() + " " + acesso.toString() + "(" + dado.getNome() + ")");
                        BloqueioDado.acorda(emEspera.getScheduleInList(), dadosDesbloqueados, executar.getScheduleInList());

                    }
                    break;

            }
            /* enquando escalona, caso aparecer algo novo no banco
                insere oque chegou no final da lista em execuçao
            */
            indice = busca.run(executar.getScheduleInList(), indice);

        }
//        System.out.println("\n terminados \n");
//        for (Operacao p : terminado.getScheduleInList()) {
//            System.out.println("T" + p.getTransacao().getMeuIndice() + " " + p.getAcesso().toString() + "(" + p.getDado().getNome() + ")");
//        }
        TransacaoDAO.gravarTransacoes(terminado);
    }

    public static boolean deadlock(LinkedList<Operacao> emEspera, LinkedList<BloqueioDado> bloqueados,
            Operacao operacao) {

        Transacao t = BloqueioDado.donoDoBloqueio(bloqueados, operacao.getDado());
        LinkedList<Operacao> operacoes = Schedule.Está(emEspera, t);
        for (Operacao p : operacoes) {

            Transacao t2 = BloqueioDado.donoDoBloqueio(bloqueados, p.getDado());
            if (t2.getMeuIndice().equals(operacao.getTransacao().getMeuIndice())) {
                return true;
            }
        }

        return false;
    }
}
