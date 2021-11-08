import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.AllocationService;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import ch.hearc.cafheg.infrastructure.persistance.Database;
import ch.hearc.cafheg.infrastructure.persistance.Migrations;
import ch.hearc.cafheg.infrastructure.transaction.AllocationServiceTransactional;
import java.util.List;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AllocationServiceIT {

  private AllocationServiceTransactional allocationServiceTransactional;
  private AllocataireMapper allocataireMapper;
  private AllocationMapper allocationMapper;

  @BeforeEach
  void setUp() {
    allocataireMapper = new AllocataireMapper();
    allocationMapper = new AllocationMapper();
    allocationServiceTransactional = new AllocationServiceTransactional(
        new AllocationService(allocataireMapper, allocationMapper));
  }

  @Test
  public void deleteAllocataire_Given4Allocataires_ShouldHave3AllocatairesAfterRemoval()
      throws Exception {

    // Create a DBUnit Database connection from an existing JDBC Connection
    Database db = new Database();
    Database.start();
    Migrations migrations = new Migrations(db, true);
    migrations.start();

    // Load a DataSet into memory
    IDatabaseConnection connection = new DatabaseConnection(db.dataSource().getConnection());

    IDataSet dataset = new FlatXmlDataSetBuilder()
        .build(getClass().getClassLoader().getResourceAsStream("dataset.xml"));

    DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);

    List<Allocataire> allocataires = allocationServiceTransactional.findAllAllocataires(null);
    assertThat(allocataires.size() == 4);

    allocationServiceTransactional.deleteAllocataire("756.1558.5343.53");

    List<Allocataire> allocatairesAfterRemoval = allocationServiceTransactional.findAllAllocataires(
        null);
    assertThat(allocatairesAfterRemoval.size() == 3);
  }

  @Test
  public void updateNomOuPrenom_GivenTienMaclin_ShouldHaveKevinSoaresAfterUpdate()
      throws Exception {

    // Create a DBUnit Database connection from an existing JDBC Connection
    Database db = new Database();
    Database.start();
    Migrations migrations = new Migrations(db, true);
    migrations.start();

    // Load a DataSet into memory
    IDatabaseConnection connection = new DatabaseConnection(db.dataSource().getConnection());

    IDataSet dataset = new FlatXmlDataSetBuilder()
        .build(getClass().getClassLoader().getResourceAsStream("dataset.xml"));

    DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);

    allocationServiceTransactional.updateNomOuPrenom("756.4284.5291.11", "Kevin", "Soares");

    Allocataire allocataireAfterUpdate = allocationServiceTransactional.findAllocataireByNoAVS(
        "756.4284.5291.11");
    assertThat(allocataireAfterUpdate.getPrenom() == "Kevin"
        && allocataireAfterUpdate.getNom() == "Soares");
  }

}