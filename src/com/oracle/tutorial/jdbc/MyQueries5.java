package com.oracle.tutorial.jdbc;

import java.sql.*;
import java.util.Scanner;

public class MyQueries5 {

    Connection con;
    JDBCUtilities settings;

    public MyQueries5(Connection connArg, JDBCUtilities settingsArg) {
        this.con = connArg;
        this.settings = settingsArg;
    }
/*
    EXERCICIO 2:

    public static void getMyData(Connection con) throws SQLException {
        Statement stmt = null;
        String query = "SELECT * FROM CONTA";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Contas da Instituicao Bancaria: ");
            while (rs.next()) {
                Integer conta = rs.getInt(1);
                String agencia = rs.getString(2);
                System.out.println(conta.toString() +", " + agencia);
            }
        } catch (SQLException e) {
            JDBCUtilities.printSQLException(e);
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }
*/
    public static void getMyData(Connection con) throws SQLException {
        Statement stmt = null;
        String query = "select d.nome_cliente, d.nome_agencia, d.numero_conta, " +
                "SUM(dp.saldo_deposito) as soma_deposito, " +
                "SUM(e.valor_emprestimo) as soma_emprestimo " +
                "FROM debito d " +
                "LEFT JOIN deposito dp ON d.nome_cliente = dp.nome_cliente AND d.nome_agencia = dp.nome_agencia AND d.numero_conta = dp.numero_conta " +
                "LEFT JOIN emprestimo e ON d.nome_cliente = e.nome_cliente AND d.nome_agencia = e.nome_agencia AND d.numero_conta = e.numero_conta " +
                "GROUP BY d.nome_cliente, d.nome_agencia, d.numero_conta;";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Contas da Instituicao Bancaria: ");
            while (rs.next()) {
                /*
                -- indices numericos
                String nome = rs.getString(1);
                String agencia = rs.getString(2);
                String conta = rs.getString(3);
                Double soma_deposito = rs.getDouble(4);
                Double soma_emprestimo = rs.getDouble(5);

                --alias
                String nome = rs.getString("nome");
                String agencia = rs.getString("agencia");
                String conta = rs.getString("conta");
                Double soma_deposito = rs.getDouble("soma_deposito");
                Double soma_emprestimo = rs.getDouble("soma_emprestimo");

                --nomes dos campos
                */
                String nome = rs.getString("nome_cliente");
                String agencia = rs.getString("nome_agencia");
                String conta = rs.getString("numero_conta");
                Double soma_deposito = rs.getDouble("soma_deposito");
                Double soma_emprestimo = rs.getDouble("soma_emprestimo");
                System.out.println(nome +", " + agencia + ", " + conta + ", " + soma_deposito + ", " + soma_emprestimo);
            }
        } catch (SQLException e) {
            JDBCUtilities.printSQLException(e);
        } finally {
            if (stmt != null) { stmt.close(); }
        }

    }

    public static void cursorHoldabilitySupport(Connection conn)
            throws SQLException {
        DatabaseMetaData dbMetaData = conn.getMetaData();
        System.out.println("ResultSet.HOLD_CURSORS_OVER_COMMIT = " +
                ResultSet.HOLD_CURSORS_OVER_COMMIT);
        System.out.println("ResultSet.CLOSE_CURSORS_AT_COMMIT = " +
                ResultSet.CLOSE_CURSORS_AT_COMMIT);
        System.out.println("Default cursor holdability: " +
                dbMetaData.getResultSetHoldability());
        System.out.println("Supports HOLD_CURSORS_OVER_COMMIT? " +
                dbMetaData.supportsResultSetHoldability(
                        ResultSet.HOLD_CURSORS_OVER_COMMIT));
        System.out.println("Supports ResultSet CONCUR_READ_ONLY? " +
                dbMetaData.supportsResultSetConcurrency(
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY));
        System.out.println("Supports ResultSet CONCUR_UPDATABLE? " +
                dbMetaData.supportsResultSetConcurrency(
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE));
    }

    // REFERENTE A QUESTÃO 7
    public static void modifyPrices1(Connection con) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM deposito");
            while (uprs.next()) {
                float f = uprs.getFloat("saldo_deposito");
                uprs.updateFloat("saldo_deposito", (float) (f * 1.05));
                uprs.updateRow();
            }
        } catch (SQLException e ) {
            JDBCTutorialUtilities.printSQLException(e);
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }

    // REFERENTE A QUESTÃO 8
    public static void modifyPrices2(Connection con) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM deposito");
            System.out.println("Digite o multiplicador como um numero real (Ex.: 5% = 1,05):");
            Scanner in = new Scanner(System.in);
            double percentage = in.nextDouble();
            while (uprs.next()) {
                float f = uprs.getFloat("saldo_deposito");
                uprs.updateFloat("saldo_deposito", (float) (f * percentage));
                uprs.updateRow();
            }
        } catch (SQLException e ) {
            JDBCTutorialUtilities.printSQLException(e);
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }

    public static void insertRow(Connection con) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet uprs = stmt.executeQuery("SELECT * FROM debito");
            uprs.moveToInsertRow(); //posiciona no ponto de inserção da tabela
            uprs.updateInt(1, 2000);
            uprs.updateFloat(2, 150);
            uprs.updateInt(3, 1);
            uprs.updateDate(4, Date.valueOf("2014-01-23"));
            uprs.updateInt(5, 46248);
            uprs.updateString(6, "UFU");
            uprs.updateString(7, "Carla Soares Sousa");
            uprs.insertRow(); //insere a linha na tabela

            uprs.moveToInsertRow(); //posiciona no ponto de inserção da tabela
            uprs.updateInt(1, 2001);
            uprs.updateFloat(2, 200);
            uprs.updateInt(3, 2);
            uprs.updateDate(4, Date.valueOf("2014-01-23"));
            uprs.updateInt(5, 26892);
            uprs.updateString(6, "Glória");
            uprs.updateString(7, "Carolina Soares Souza");
            uprs.insertRow();

            uprs.moveToInsertRow(); //posiciona no ponto de inserção da tabela
            uprs.updateInt(1, 2002);
            uprs.updateFloat(2, 500);
            uprs.updateInt(3, 3);
            uprs.updateDate(4, Date.valueOf("2014-01-23"));
            uprs.updateInt(5, 70044);
            uprs.updateString(6, "Cidade Jardim");
            uprs.updateString(7, "Eurides Alves da Silva");
            uprs.insertRow();

            uprs.beforeFirst(); //posiciona-se novamente na posição anterior ao primeiro registro
        } catch (SQLException e) {
            JDBCTutorialUtilities.printSQLException(e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }


    public static void main(String[] args) {
        JDBCUtilities myJDBCUtilities;
        Connection myConnection = null;
        if (args[0] == null) {
            System.err.println("Properties file not specified at command line");
            return;
        } else {
            try {
                myJDBCUtilities = new JDBCUtilities(args[0]);
            } catch (Exception e) {
                System.err.println("Problem reading properties file " + args[0]);
                e.printStackTrace();
                return;
            }
        }

        try {
            myConnection = myJDBCUtilities.getConnection();

            //MyQueries5.getMyData(myConnection);
            //MyQueries5.cursorHoldabilitySupport(myConnection);
            //MyQueries5.modifyPrices1(myConnection);
            //MyQueries5.modifyPrices2(myConnection);
            MyQueries5.insertRow(myConnection);

        } catch (SQLException e) {
            JDBCUtilities.printSQLException(e);
        } finally {
            JDBCUtilities.closeConnection(myConnection);
        }

    }
}
