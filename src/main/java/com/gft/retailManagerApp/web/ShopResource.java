package com.gft.retailManagerApp.web;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;
import com.gft.retailManagerApp.domain.Shop;

/**
 * It represents a HATEOAS resource of a {@link Shop}
 *
 * @author Vanessa Cantalapiedra
 * @version 1.0
 * @since 20-04-2017
 */

@Relation(value = "", collectionRelation = "shops")
public class ShopResource extends ResourceSupport {

	Shop shop;

	public ShopResource() {
	}

	public ShopResource(Shop newshop) {
		shop = newshop;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

}
