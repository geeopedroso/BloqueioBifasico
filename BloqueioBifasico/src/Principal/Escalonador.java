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
        Schedule executar = new Schedule();
        executar.setScheduleInList(TransacaoDAO.buscarTransacoes());
        Integer indice = TransacaoDAO.pegarUltimoIndice();
        Busca busca = new Busca();

//        for(Operacao p: executar.getScheduleInList()){
//            System.out.println("T"+p.getTransacao().getMeuIndice()+" "+ p.getAcesso().toString()+"("+ p.getDado().getNome()+")");
//        }
        Schedule emEspera = new Schedule();
        Schedule terminado = new Schedule();
        LinkedList<BloqueioDado> bloqueados = new LinkedList<>();

        while (!executar.getScheduleInList().isEmpty()) {

            System.out.println("indice: " + indice);
            System.out.println("executar: " + executar.getScheduleInList().size());

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
                    List<Integer> contem = Schedule.Contem(emEspera.getScheduleInList(), transacao);
                    if (!contem.isEmpty()) {
                        emEspera.getScheduleInList().add((contem.get(contem.size() - 1) + 1),
                                executar.getScheduleInList().remove(0));
                    } else {

                        LinkedList<Dado> dadosDesbloqueados = BloqueioDado.desbloqueia(bloqueados, transacao.getMeuIndice());

                        executar.getScheduleInList().get(0).setAcesso(Acesso.COMMIT);
                        terminado.getScheduleInList().add(executar.getScheduleInList().remove(0));

                        BloqueioDado.acorda(emEspera.getScheduleInList(), dadosDesbloqueados, executar.getScheduleInList());

                    }
                    break;

            }
            indice = busca.run(executar.getScheduleInList(), indice);

        }
        System.out.println("\n terminados \n");
        for (Operacao p : terminado.getScheduleInList()) {
            System.out.println("T" + p.getTransacao().getMeuIndice() + " " + p.getAcesso().toString() + "(" + p.getDado().getNome() + ")");
        }
        //TransacaoDAO.gravarTransacoes(terminado);
    }
}
