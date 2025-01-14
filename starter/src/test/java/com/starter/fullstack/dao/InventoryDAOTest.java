package com.starter.fullstack.dao;

import com.starter.fullstack.api.Inventory;
import java.util.List;
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

	/**
		* Test Save Inventory method
		*/
	@Test
	public void create() {
    Assert.assertNotNull("InventoryDAO should not be null", inventoryDAO);
		Inventory inventory = new Inventory(); 
    inventory.setName("Testing Testing Product");
    inventory.setProductType("Product Type 1");
    
		Inventory savedInventory = inventoryDAO.create(inventory);
	

      // Checks Saving inventory correctly and not null
     Assert.assertNotNull("Inventory ID should not be null after save", savedInventory.getId());

     // Outputs
     System.out.println("Saved Inventory ID: " + savedInventory.getId());
     System.out.println("Saved Inventory Name: " + savedInventory.getName());
     System.out.println("Saved Inventory Product Type: " + savedInventory.getProductType());
	}
		

}
