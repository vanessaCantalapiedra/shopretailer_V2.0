package com.gft.retailManagerApp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.gft.retailManagerApp.domain.Shop;

/**
 * Implementation of ShopRepository Provides functionality to manage shops
 * 
 * @author Vanessa Cantalapiedra
 * @version 1.0
 * @since 20-04-2017
 */

@Component
public class ShopRepositoryImpl implements ShopRepository {

	// In the constructor parameters such as capacity, load factor and the
	// concurrenty level can be set
	// to optimize the use of the hashmap. In this case we don't have enough
	// information to set up these
	// parameters properly so the default constructor has been used.
	private ConcurrentHashMap<String, Shop> shopHashMap = new ConcurrentHashMap<String, Shop>();

	private static final Logger logger = LoggerFactory.getLogger(ShopRepositoryImpl.class);

	@Override
	public Shop getByName(String name) {
		return shopHashMap.get(name);
	}

	/**
	 * It saves a new or an existing @Shop, increasing by one its version
	 */
	@Override
	public Shop save(Shop shop) {
		Shop savedShop = shopHashMap.putIfAbsent(shop.getName(), shop); // thread-safe
		if (savedShop != null) {
			// some race conditions could happen , but the shops are always
			// updated no matter what and return the shop that was replaced
			// thanks to the properties of the concurrenthashmap
			shop.updateVersion(savedShop.nextVersion());
			savedShop = new Shop(shopHashMap.put(shop.getName(), shop)); // thread-safe
			logger.debug("SAVED SHOP" + shopHashMap.get(shop.getName()).toString());
		}

		return savedShop;
	}

	@Override
	public List<Shop> findAll() {
		return new ArrayList<Shop>(shopHashMap.values());
	}

	/**
	 * Updates a @Shop only if the version of both instances are the same
	 * 
	 * @param shop
	 * @Shop shop with the new info to be updated
	 * @return boolean true if there has been an update
	 */
	@Override
	public boolean update(Shop shop) {
		if (shopHashMap.get(shop.getName()).getVersion().equals(shop.getVersion())) {
			shopHashMap.put(shop.getName(), shop);
			return true;
		}
		return false;
	}

	@Override
	public void deleteAll() {
		shopHashMap.clear();
	}

}
