/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Acesso;
import Model.Dado;
import Model.Operacao;
import Model.Schedule;
import Model.Transacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author geovani
 */
public class TransacaoDAO {

    private static Conexao minhaConexao;

    public TransacaoDAO() {
        minhaConexao = new Conexao();
        Conexao.getCabecalho();
    }

    public static LinkedList<Operacao> gravarTransacoes(Schedule schedule) {
        Operacao operacao = null;
        Conexao minhaConexao = new Conexao();
        Connection conn = minhaConexao.getConnection();

        String sql = "INSERT INTO saida(indiceTransacao, operacao, itemDado) VALUES (?, ?, ?)";
        
        
        PreparedStatement stmt = null;
        
        
        
        while ((!schedule.getScheduleInList().isEmpty())) {
            operacao = schedule.getScheduleInList().removeFirst();
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, operacao.getTransacao().getMeuIndice());
                stmt.setString(2, operacao.getAcesso().toString());
                stmt.setString(3, operacao.getDado().getNome());
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Erro na insercao da transacao");
                e.printStackTrace();
            }
        }
        try {
            minhaConexao.releaseAll(stmt, conn);
        } catch (SQLException e) {
            System.out.println("Erro ao encerrar conexo");
            e.printStackTrace();
        }
        return schedule.getScheduleInList();
    }
    
    

    public static LinkedList<Operacao> buscarTransacoes() {
        LinkedList<Operacao> operacoes = new LinkedList<>();
        Conexao minhaConexao = new Conexao();
        Connection conn = minhaConexao.getConnection();
        String sql = "select * from  schedule";

        try {
            PreparedStatement stm = conn.prepareStatement(sql);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {

                String nome = rs.getString("itemDado");
                Integer id = rs.getInt("indicetransacao");

                String ace = rs.getString("operacao");
                Acesso acesso = null;
                if (ace.equals("B")) {
                    acesso = Acesso.BEGIN;
                } else if (ace.equals("R")) {
                    acesso = Acesso.READ;
                }
                if (ace.equals("W")) {
                    acesso = Acesso.WRITE;
                } else if (ace.equals("E")) {
                    acesso = Acesso.END;
                }
                if (ace.equals("C")) {
                    acesso = Acesso.COMMIT;
                } else if (ace.equals("A")) {
                    acesso = Acesso.ABORT;
                }
                Operacao operacao = new Operacao(acesso,new Transacao(id),new Dado(nome));
                operacoes.add(operacao);

            }
            minhaConexao.releaseAll(stm, conn);
        } catch (Exception e) {
            System.out.println("erro busca dados");
        }
        return operacoes;
    }
    
    
    public static void buscarTransacoes(LinkedList<Operacao> operacoes, Integer indice) {
        Conexao minhaConexao = new Conexao();
        Connection conn = minhaConexao.getConnection();
        String sql = "select * from  schedule where idoperacao > ?";

        try {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, indice);
            
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {

                String nome = rs.getString("itemDado");
                Integer id = rs.getInt("indicetransacao");

                String ace = rs.getString("operacao");
                Acesso acesso = null;
                if (ace.equals("B")) {
                    acesso = Acesso.BEGIN;
                } else if (ace.equals("R")) {
                    acesso = Acesso.READ;
                }
                if (ace.equals("W")) {
                    acesso = Acesso.WRITE;
                } else if (ace.equals("E")) {
                    acesso = Acesso.END;
                }
                if (ace.equals("C")) {
                    acesso = Acesso.COMMIT;
                } else if (ace.equals("A")) {
                    acesso = Acesso.ABORT;
                }
                Operacao operacao = new Operacao(acesso,new Transacao(id),new Dado(nome));
                operacoes.add(operacao);

            }
            minhaConexao.releaseAll(stm, conn);
        } catch (Exception e) {
            System.out.println("erro busca dados");
        }
        
    }

    public static Integer pegarUltimoIndice() {
        Integer ultimoIndice = 0;
        minhaConexao = new Conexao();
        Connection conn = minhaConexao.getConnection();
        String sql = "SELECT MAX(idoperacao) FROM schedule;";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            ultimoIndice = rs.getInt(1);

            minhaConexao.releaseAll(stmt, conn);
        } catch (SQLException e) {
            System.err.println("Erro na consulta ao ultimo indice");
            e.printStackTrace();
        }
        return ultimoIndice;
    }

}
