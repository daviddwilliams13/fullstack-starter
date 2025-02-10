package com.starter.fullstack.dao;

import com.starter.fullstack.api.Inventory;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Test Inventory DAO.
 */
@DataMongoTest
@RunWith(SpringRunner.class)
public class InventoryDAOTest {
  @ClassRule
  public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

  @Resource
  private MongoTemplate mongoTemplate;
  private InventoryDAO inventoryDAO;
  private static final String NAME = "Amber";
  private static final String PRODUCT_TYPE = "hops";

  @Before
  public void setup() {
    this.inventoryDAO = new InventoryDAO(this.mongoTemplate);
  }

  @After
  public void tearDown() {
    this.mongoTemplate.dropCollection(Inventory.class);
  }

  /**
   * Test Find All method.
   */
  @Test
  public void findAll() {
    Inventory inventory = new Inventory();
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);
    this.mongoTemplate.save(inventory);
    List<Inventory> actualInventory = this.inventoryDAO.findAll();
    Assert.assertFalse(actualInventory.isEmpty());
  }

  @Test
	public void create() {
    Assert.assertNotNull(inventoryDAO);
		Inventory inventory = new Inventory(); 
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);
    Inventory savedInventory = inventoryDAO.create(inventory);
    Assert.assertNotNull(savedInventory.getId());
    Assert.assertEquals(NAME, savedInventory.getName());
    Assert.assertEquals(PRODUCT_TYPE, savedInventory.getProductType());
	}

  @Test
  public void delete() {
    Inventory inventory = new Inventory(); 
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);
    Inventory savedInventory = inventoryDAO.create(inventory);
    Assert.assertNotNull(savedInventory.getId());
    Optional<Inventory> deletedInventory = this.inventoryDAO.delete(savedInventory.getId());
    Assert.assertTrue(deletedInventory.isPresent());
    Assert.assertEquals(savedInventory.getId(), deletedInventory.get().getId());

  }
}
