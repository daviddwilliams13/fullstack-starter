package com.starter.fullstack.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starter.fullstack.api.Inventory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Inventory Controller Test.
 */
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class InventoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  private Inventory inventory;

  private static final String INVENTORY_URL = "/inventory";
  private static final String NAME = "TEST";
  private static final String PRODUCT_TYPE = "productType";
  private static final String NEWPRODUCT_TYPE = "NEWproductType";
  private static final String NEWNAME = "NEWTEST";

  @Before
  public void setup() throws Throwable {
    this.inventory = new Inventory();
    this.inventory.setName(NAME);
    this.inventory.setProductType(PRODUCT_TYPE);
    this.inventory = this.mongoTemplate.save(this.inventory);
  }

  @After
  public void teardown() {
    this.mongoTemplate.dropCollection(Inventory.class);
  }

  /**
   * Test findAll endpoint.
   * @throws Throwable see MockMvc
   */
  @Test
  public void findAll() throws Throwable {
    this.mockMvc.perform(get(INVENTORY_URL)
    .accept(MediaType.APPLICATION_JSON))
    .andExpect(status().isOk())
    .andExpect(content().json("[" + this.objectMapper.writeValueAsString(inventory) + "]"));
  }

  /**
   * Test create endpoint.
   * @throws Throwable see MockMvc
   */
  @Test
  public void create() throws Throwable {
    Inventory newInventory = new Inventory();
    newInventory.setName(NEWNAME);
    newInventory.setProductType(NEWPRODUCT_TYPE);
    this.mockMvc.perform(post(INVENTORY_URL)
    .accept(MediaType.APPLICATION_JSON)
    .contentType(MediaType.APPLICATION_JSON)
    .content(this.objectMapper.writeValueAsString(newInventory)))
    .andExpect(status().isOk());

    Assert.assertEquals(2, this.mongoTemplate.findAll(Inventory.class).size());
  }

}