package com.gft.retailManagerApp;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.gft.retailManagerApp.domain.Address;
import com.gft.retailManagerApp.domain.Shop;
import com.gft.retailManagerApp.repository.ShopRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Before each test , the mock data will be populated
	 */
	@Before
	public void setUp() throws Exception {
		shopRepository.save(new Shop("shop1", new Address(1, "28002")));
		shopRepository.save(new Shop("shop2", new Address(1, "28020")));
		shopRepository.save(new Shop("shop3", new Address(1, "48006")));
		shopRepository.save(new Shop("shop4", new Address(1, "13600")));
		shopRepository.save(new Shop("shop5", new Address(1, "48004")));
	}

	/**
	 * After each test , the mock data will be deleted
	 */
	@After
	public void tearDown() throws Exception {
		shopRepository.deleteAll();
	}

	// =========================================== Get All Shops
	// ==========================================
	@Test
	public void getShops_OK() throws Exception {
		// convert to an arraylist to keep the order
		ArrayList<Shop> list = (ArrayList<Shop>) shopRepository.findAll();

		this.mockMvc.perform(get("/shops")).andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.shops.[0].shop.name", is(list.get(0).getName())))
				.andExpect(jsonPath("$._embedded.shops.[1].shop.name", is(list.get(1).getName())))
				.andExpect(jsonPath("$._embedded.shops.[2].shop.name", is(list.get(2).getName())))
				.andExpect(jsonPath("$._embedded.shops.[3].shop.name", is(list.get(3).getName())))
				.andExpect(jsonPath("$._embedded.shops.[4].shop.name", is(list.get(4).getName())))
				.andExpect((jsonPath("$._embedded.shops", Matchers.hasSize(5))));
	}

	// =========================================== Saves Shops
	// ==========================================
	@Test
	public void saveInvalidShop() throws Exception {
		this.mockMvc.perform(post("/shops/").contentType(MediaType.APPLICATION_JSON_UTF8).content("{}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void saveNewShop() throws Exception {
		String shopStr = "{\"name\":\"shop8\",\"address\":{\"number\":\"1\",\"postcode\":\"48004\"}}";
		this.mockMvc.perform(post("/shops/").contentType(MediaType.APPLICATION_JSON_UTF8).content(shopStr))
				.andExpect(status().isCreated()).andExpect(header().string("location", "http://localhost/shops/shop8"))
				.andExpect(header().string("location", "http://localhost/shops/shop8")).andExpect(content().string(""));
	}

	@Test
	public void updateShop() throws Exception {
		String shopStr = "{\"name\":\"shop5\",\"address\":{\"number\":\"8\",\"postcode\":\"25000\"}}";
		this.mockMvc.perform(post("/shops/").contentType(MediaType.APPLICATION_JSON_UTF8).content(shopStr))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().json(
						"{\"shop\":{\"name\":\"shop5\",\"address\":{\"number\":1,\"postcode\":\"48004\"}},\"_links\":{\"self\":{\"href\":\"http://localhost/shops/shop5\"}}}"));
	}

	// =========================================== Get Shops
	// ==========================================

	@Test
	public void getNonExistantShop() throws Exception {
		this.mockMvc.perform(get("/shops/shop10")).andExpect(status().isNotFound());
	}

	@Test
	public void getExistantShop() throws Exception {
		this.mockMvc.perform(get("/shops/shop5")).andExpect(status().isOk()).andDo(print()).andExpect(content().json(
				"{\"shop\":{\"name\":\"shop5\",\"address\":{\"number\":1,\"postcode\":\"48004\"}},\"_links\":{\"self\":{\"href\":\"http://localhost/shops/shop5\"}}}"));
	}

}