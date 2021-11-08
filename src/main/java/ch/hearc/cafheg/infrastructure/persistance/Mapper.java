package ch.hearc.cafheg.infrastructure.persistance;

import java.sql.Connection;

/**
 * Classe abstraite permettant à chaque implémentation de Mapper de recupérer la connection JDBC
 * active.
 */
public class Mapper {

  protected static final String RESULT_SET = "ResultSet#next";
  protected static final String SQL_ERROR = "SQL error ";

  protected Connection activeJDBCConnection() {
    return Database.activeJDBCConnection();
  }
}
