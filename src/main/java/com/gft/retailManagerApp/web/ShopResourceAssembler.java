package com.gft.retailManagerApp.web;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.gft.retailManagerApp.domain.Shop;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resources;

/**
 * Class to manage resources by the controller
 * 
 * @author Vanessa Cantalapiedra
 * @version 1.0
 * @since 20-04-2017
 */

@Component
public class ShopResourceAssembler extends ResourceAssemblerSupport<Shop, ShopResource> {

	@Autowired
	private RepositoryEntityLinks entityLinks;

	public ShopResourceAssembler() {
		super(ShopController.class, ShopResource.class);
	}

	/**
	 * Creates the default representation of shop resource (in this case with
	 * Address as links)
	 */
	public ShopResource toResource(Shop shop) {
		ShopResource resource = instantiateResource(shop);
		resource.shop = shop;
		resource.add(linkTo(ShopController.class).slash(shop.getName()).withSelfRel());
		return resource;
	}

	/**
	 * Generates the list of resources adding the link to the list itself
	 * 
	 * @param shops
	 *            the list with shops to be converted
	 * @return Resources<ShopResource> the collection of shopresources
	 */
	public Resources<ShopResource> toResourcesHATEOAS(List<Shop> shops) {
		List<ShopResource> shopsResources = toResources(shops);
		Resources<ShopResource> resources = new Resources<>(shopsResources);
		resources.add(linkTo(ShopController.class).withSelfRel());
		return resources;
	}
}
