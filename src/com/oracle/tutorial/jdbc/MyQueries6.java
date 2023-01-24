package com.oracle.tutorial.jdbc;

import java.sql.*;

public class MyQueries6 {

    Connection con;
    JDBCUtilities settings;

    public MyQueries6(Connection connArg, JDBCUtilities settingsArg) {
        this.con = connArg;
        this.settings = settingsArg;
    }

    public static void insertMyData3000(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        long startTime = System.currentTimeMillis();
        long endTime;
        String query = null;
        query  = "insert into debito (numero_debito, valor_debito, motivo_debito, data_debito, numero_conta, nome_agencia, nome_cliente) " +"values (?,?,?,?,?,?,?);" ;
        try {
            con.setAutoCommit(false);
            stmt = con.prepareStatement(query);
        } catch (SQLException e) {
            JDBCUtilities.printSQLException(e);
        }

        for(int numdeb = 5008; numdeb < 6008; numdeb++){
            try {
                stmt.setInt(1, numdeb);
                stmt.setDouble(2, numdeb);
                stmt.setInt(3, 4);
                stmt.setDate(4, Date.valueOf("2014-02-06") ); stmt.setInt(5, 36593);
                stmt.setString(6, "UFU"); stmt.setString(7, "Pedro Alvares Sousa");
                stmt.executeUpdate();

                if((numdeb%50)==0){
                    endTime = System.currentTimeMillis();
                    System.out.println("BATCH"+(numdeb-5000) + "\t" + (endTime - startTime)); }
            } catch (SQLException e) {
                JDBCUtilities.printSQLException(e);
            }
        }
        try {
            con.commit();
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            JDBCUtilities.printSQLException(e);
        }
    }
    public static void insertMyData2000(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        long startTime = System.currentTimeMillis();
        String query = null;
        query = "insert into debito (numero_debito, valor_debito, motivo_debito, data_debito, numero_conta, nome_agencia, nome_cliente)" +
                "values (?, ?, ?, ?, ?, ?, ?);";
        for(int numdeb = 4005; numdeb < 5005; numdeb++) {
            try {
                stmt = con.prepareStatement(query);
                stmt.setInt(1, numdeb);
                stmt.setDouble(2, numdeb);
                stmt.setInt(3, 4);
                stmt.setDate(4, Date.valueOf("2014-02-06"));
                stmt.setInt(5, 36593);
                stmt.setString(6, "UFU");
                stmt.setString(7, "Pedro Alvares Sousa");

                stmt.executeUpdate();
                if (stmt != null) {
                    stmt.close();
                }
                if ((numdeb % 50) == 0) {
                    long endTime = System.currentTimeMillis();
                    System.out.println("PreparedStatement"+ (numdeb - 5000) + "\t" + (endTime - startTime));
                }
            } catch (SQLException e) {
                JDBCUtilities.printSQLException(e);
            }
        }
    }
    public static void insertMyData1000(Connection con) throws SQLException {
        Statement stmt = null;
        long startTime = System.currentTimeMillis();
        String query = null;
        for(int numdeb = 3004; numdeb < 4004; numdeb++) {
            query = "insert into debito (numero_debito, valor_debito, motivo_debito, data_debito, numero_conta,nome_agencia, nome_cliente) " +
                    "values (" + Integer.toString(numdeb) + "," + Integer.toString(numdeb) + ",5,'2014-02-06',36593,'UFU','Pedro Alvares Sousa');";
            if ((numdeb % 50) == 0) {
                long endTime = System.currentTimeMillis();
                System.out.println("Statement"+ (numdeb - 3000) + "\t" + (endTime - startTime));
            }
            try {
                stmt = con.createStatement();
                stmt.executeUpdate(query);
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                JDBCUtilities.printSQLException(e);
            }
        }
    }

    public static void insertMyData2(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        long startTime = System.currentTimeMillis();
        String query = null;
        query = "insert into debito (numero_debito, valor_debito, motivo_debito, data_debito, numero_conta, nome_agencia, nome_cliente)" +
                "values (?, ?, ?, ?, ?, ?, ?);";
        try {
            stmt = con.prepareStatement(query);
            stmt.setInt(1, 3002);
            stmt.setDouble(2, 3002);
            stmt.setInt(3, 4);
            stmt.setDate(4, Date.valueOf("2014-02-06") );
            stmt.setInt(5, 36593);
            stmt.setString(6, "UFU");
            stmt.setString(7, "Pedro Alvares Sousa");

            stmt.executeUpdate();
        } catch (SQLException e) {
            JDBCUtilities.printSQLException(e);
        } finally {
            long endTime = System.currentTimeMillis();
            System.out.println("Um debito em IB2 inserido com PreparedStatement em " + (endTime - startTime) + " milisegundos");
        }
    }

    public static void insertMyData1(Connection con) throws SQLException {
        Statement stmt = null;
        long startTime = System.currentTimeMillis();
        String query = null;
        query  = "insert into debito (numero_debito, valor_debito, motivo_debito, data_debito, numero_conta, nome_agencia, nome_cliente) " +
                "values (3003,3003,5,'2014-02-06',36593,'UFU','Pedro Alvares Sousa');" ;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
            if (stmt != null) {
                stmt.close();
            }
            System.out.println("Debitos da Instituicao Bancaria atualizados.");
        } catch (SQLException e) {
            JDBCUtilities.printSQLException(e);
        } finally {
            long endTime = System.currentTimeMillis();
            System.out.println("Um debito em IB2 inserido com Statement em " + (endTime - startTime) + " milisegundos");
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

            MyQueries6.insertMyData1000(myConnection);
            MyQueries6.insertMyData2000(myConnection);
            MyQueries6.insertMyData3000(myConnection);
            //MyQueries6.insertMyData1(myConnection);
            //MyQueries6.insertMyData2(myConnection);

        } catch (SQLException e) {
            JDBCUtilities.printSQLException(e);
        } finally {
            JDBCUtilities.closeConnection(myConnection);
        }

    }
}
