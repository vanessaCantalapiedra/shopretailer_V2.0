package com.gft.retailManagerApp.web;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gft.retailManagerApp.domain.Shop;
import com.gft.retailManagerApp.service.ShopService;

/**
 * It contains all the methods available in our service It is the API class of
 * the service
 *
 * @author Vanessa Cantalapiedra
 * @version 1.0
 * @since 20-04-2017
 */

@RestController
@RequestMapping("/shops")
@ExposesResourceFor(ShopResource.class) // It enables EntityLinks generating
										// singular and plural links.

public class ShopController {

	@Autowired
	private ShopService shopService;

	@Autowired
	private ShopResourceAssembler shopResourceAssembler;

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public ResponseEntity<?> getShop(@PathVariable("name") String name) {
		Shop shop = shopService.getShop(name);
		if (shop != null) {
			return new ResponseEntity<ShopResource>(shopResourceAssembler.toResource(shop), HttpStatus.OK);

		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(method = RequestMethod.GET) // by default the response will
												// be hal+json
	public ResponseEntity<?> getShops(@RequestParam(value = "latitude", required = false) Double lat,
			@RequestParam(value = "longitude", required = false) Double longitude) {

		// if latitude and longitude are empty , then it displays all the shops
		if (lat != null && longitude != null) {

			Shop nearestShop = shopService.getNearestShop(lat, longitude);

			if (nearestShop != null)
				return new ResponseEntity<ShopResource>(shopResourceAssembler.toResource(nearestShop), HttpStatus.OK);
			else
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			List<Shop> shoplist = shopService.listShops();
			return new ResponseEntity<Resources<ShopResource>>(shopResourceAssembler.toResourcesHATEOAS(shoplist),
					HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addShop(@Valid @RequestBody Shop newshop) {

		Shop savedShop = shopService.saveShop(newshop);

		// check the previous version to modify the answer
		ResponseEntity<ShopResource> myResponseEntity;

		if (savedShop != null) {
			myResponseEntity = new ResponseEntity<ShopResource>(shopResourceAssembler.toResource(savedShop),
					HttpStatus.OK);

		} else {
			// creates a status response with the URI in the location header
			return ResponseEntity
					.created(URI.create(shopResourceAssembler.toResource(newshop).getLink("self").getHref())).build();
		}

		return myResponseEntity;
	}
}