package com.gft.retailManagerApp.repository;

import java.util.List;

import com.gft.retailManagerApp.domain.Shop;

/**
 * Repository Interface for data model Shop
 *
 * @author Vanessa Cantalapiedra
 * @version 1.0
 * @since 20-04-2017
 */

public interface ShopRepository {

	Shop getByName(String name);

	Shop save(Shop shop);

	List<Shop> findAll();

	boolean update(Shop shop);

	void deleteAll();
}
