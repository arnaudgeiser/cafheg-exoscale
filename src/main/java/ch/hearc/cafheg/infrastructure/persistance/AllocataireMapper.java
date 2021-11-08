package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.NoAVS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllocataireMapper extends Mapper {

  private static final Logger logger = LoggerFactory.getLogger(AllocataireMapper.class);
  private static final String QUERY_FIND_ALL = "SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES";
  private static final String QUERY_FIND_WHERE_NOM_LIKE = "SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES WHERE NOM LIKE ?";
  private static final String QUERY_FIND_WHERE_NUMERO = "SELECT NO_AVS, NOM, PRENOM FROM ALLOCATAIRES WHERE NUMERO=?";
  private static final String QUERY_DELETE_BY_NOAVS = "DELETE FROM ALLOCATAIRES WHERE NO_AVS=?";
  private static final String QUERY_HAS_VERSEMENT = "SELECT COUNT(V.NUMERO) FROM VERSEMENTS V " +
      "INNER JOIN ALLOCATAIRES A ON A.NUMERO = FK_ALLOCATAIRES " +
      "WHERE A.NO_AVS=?";
  private static final String QUERY_UPDATE = "UPDATE ALLOCATAIRES SET PRENOM=?, NOM=? WHERE NO_AVS=?";
  private static final String QUERY_GET_NOM_PRENOM_BY_NOAVS = "SELECT PRENOM, NOM FROM ALLOCATAIRES WHERE NO_AVS=?";

  private static final String ALLOCATAIRE_MAPPING = "Allocataire mapping";

  public List<Allocataire> findAll(String likeNom) {
    logger.debug("findAll() " + likeNom);
    Connection connection = activeJDBCConnection();
    try {
      PreparedStatement preparedStatement;
      if (likeNom == null) {
        logger.debug("SQL: " + QUERY_FIND_ALL);
        preparedStatement = connection
            .prepareStatement(QUERY_FIND_ALL);
      } else {
        logger.debug("SQL: " + QUERY_FIND_WHERE_NOM_LIKE);
        preparedStatement = connection
            .prepareStatement(QUERY_FIND_WHERE_NOM_LIKE);
        preparedStatement.setString(1, likeNom + "%");
      }
      logger.debug("Allocation d'un nouveau tableau");
      List<Allocataire> allocataires = new ArrayList<>();

      logger.debug("Exécution de la requête");
      try (ResultSet resultSet = preparedStatement.executeQuery()) {

        logger.debug(ALLOCATAIRE_MAPPING);
        while (resultSet.next()) {
          logger.debug(RESULT_SET);
          allocataires
              .add(new Allocataire(new NoAVS(resultSet.getString(3)), resultSet.getString(1),
                  resultSet.getString(2)));
        }
      }
      logger.debug("Allocataires trouvés " + allocataires.size());
      return allocataires;
    } catch (SQLException e) {
      logger.error(SQL_ERROR, e);
      throw new RuntimeException(e);
    }
  }

  public Allocataire findById(long id) {
    logger.debug("findById() " + id);
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        QUERY_FIND_WHERE_NUMERO)) {
      logger.debug("SQL:" + QUERY_FIND_WHERE_NUMERO);
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      logger.debug(RESULT_SET);
      resultSet.next();
      logger.debug(ALLOCATAIRE_MAPPING);
      return new Allocataire(new NoAVS(resultSet.getString(1)),
          resultSet.getString(2), resultSet.getString(3));
    } catch (SQLException e) {
      logger.error(SQL_ERROR, e);
      throw new RuntimeException(e);
    }
  }

  public Allocataire findByNoAVS(String noAVS) {
    logger.debug("findByNoAVS() " + noAVS);
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        QUERY_GET_NOM_PRENOM_BY_NOAVS)) {
      logger.debug("SQL:" + QUERY_GET_NOM_PRENOM_BY_NOAVS);
      preparedStatement.setString(1, noAVS);
      ResultSet resultSet = preparedStatement.executeQuery();
      logger.debug(RESULT_SET);
      resultSet.next();
      logger.debug(ALLOCATAIRE_MAPPING);
      return new Allocataire(new NoAVS(noAVS), resultSet.getString(2), resultSet.getString(1));
    } catch (SQLException e) {
      logger.error(SQL_ERROR, e);
      return null;
    }
  }

  public void deleteByNoAVS(String noAVS) {
    logger.debug("deleteByNoAVS()" + noAVS);
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_DELETE_BY_NOAVS)) {
      logger.debug("SQL:" + QUERY_DELETE_BY_NOAVS);
      preparedStatement.setString(1, noAVS);
      preparedStatement.execute();
      logger.debug("Allocataire supprimé");
    } catch (SQLException e) {
      logger.error(SQL_ERROR, e);
      throw new RuntimeException(e);
    }
  }

  public boolean allocataireHasVersement(String noAVS) {
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_HAS_VERSEMENT)) {
      preparedStatement.setString(1, noAVS);
      ResultSet resultSet = preparedStatement.executeQuery();
      resultSet.next();
      return resultSet.getInt(1) > 0;
    } catch (SQLException e) {
      logger.error(SQL_ERROR, e);
      throw new RuntimeException(e);
    }
  }

  public void updateNomOuPrenom(String noAVS, String prenom, String nom) {
    logger.debug("updateNomOuPrenom()" + noAVS);
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_UPDATE)) {
      logger.debug("SQL:" + QUERY_UPDATE);
      preparedStatement.setString(1, prenom);
      preparedStatement.setString(2, nom);
      preparedStatement.setString(3, noAVS);
      preparedStatement.executeUpdate();
      logger.debug("Allocataire modifié");
    } catch (SQLException e) {
      logger.error(SQL_ERROR, e);
      throw new RuntimeException(e);
    }
  }

  public boolean nomOuPrenomChanged(String noAVS, String prenom, String nom) {
    Connection connection = activeJDBCConnection();
    String prenomDB = "";
    String nomDB = "";
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        QUERY_GET_NOM_PRENOM_BY_NOAVS)) {
      preparedStatement.setString(1, noAVS);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        prenomDB = resultSet.getString("PRENOM");
        nomDB = resultSet.getString("NOM");
      }
      return !prenom.equals(prenomDB) || !nom.equals(nomDB);
    } catch (SQLException e) {
      logger.error(SQL_ERROR, e);
      throw new RuntimeException(e);
    }
  }
}
