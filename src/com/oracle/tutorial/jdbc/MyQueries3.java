package com.oracle.tutorial.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyQueries3 {

  Connection con;
  JDBCUtilities settings;

  public MyQueries3(Connection connArg, JDBCUtilities settingsArg) {
    this.con = connArg;
    this.settings = settingsArg;
  }

  //test

  public static void getMyData(Connection con) throws SQLException {
    Statement stmt = null;
    String query =
            "SELECT nome_cliente, SUM(saldo_deposito) " +
                    "AS soma_deposito, SUM(valor_emprestimo) " +
                    "AS soma_emprestimo " +
                    "FROM((SELECT d.nome_cliente, saldo_deposito, 0 AS valor_emprestimo FROM deposito d) UNION ALL (SELECT e.nome_cliente, 0 AS saldo_deposito, valor_emprestimo FROM emprestimo e)) consulta " +
                    "GROUP BY nome_cliente HAVING SUM(saldo_deposito) > 0 AND SUM(valor_emprestimo) > 0";

    try {
      stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      System.out.println("\nNome - Saldo Depósito - Valor Empréstimo");
      while (rs.next()) {
        String getValues = rs.getString(1);
        Double saldo_deposito = rs.getDouble(2);
        Double valor_emprestimo = rs.getDouble(3);

        System.out.println(getValues + " - " + saldo_deposito + " - " + valor_emprestimo);
      }
    } catch (SQLException e) {
      JDBCUtilities.printSQLException(e);
    } finally {
      if (stmt != null) { stmt.close(); }
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

 	MyQueries3.getMyData(myConnection);

    } catch (SQLException e) {
      JDBCUtilities.printSQLException(e);
    } finally {
      JDBCUtilities.closeConnection(myConnection);
    }

  }
}
