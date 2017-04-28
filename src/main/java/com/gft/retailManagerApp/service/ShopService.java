package com.gft.retailManagerApp.service;

import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gft.retailManagerApp.domain.Address;
import com.gft.retailManagerApp.domain.Shop;
import com.gft.retailManagerApp.repository.ShopRepository;
import com.gft.retailManagerApp.support.GeoCoordinate;
import com.gft.retailManagerApp.support.GeocodeService;

/**
 * Shop service manages all the calls from the @ShopController
 * 
 * @author Vanessa Cantalapiedra
 * @version 1.0
 * @since 20-04-2017
 */

@Service
public class ShopService {

	@Autowired
	private ShopRepository repository;

	@Autowired
	private GeocodeService geocodeService;

	private static final Logger logger = LoggerFactory.getLogger(ShopService.class);

	/**
	 * It retrieves the name of the @Shop given the name, its identifier
	 * 
	 * @param name
	 *            The name of the @Shop to be retrieved
	 * 
	 * @return Shop null if the shop is not present
	 */
	public Shop getShop(String name) {
		return repository.getByName(name);
	}

	/**
	 * Stores the given @Shop
	 * 
	 * @param newshop
	 * @Shop to be saved
	 * 
	 * @return Shop null if it is a new @Shop
	 */
	public Shop saveShop(Shop newshop) {
		Shop savedShop = repository.save(newshop);
		// call for google service to update longitude and latitude
		// this is done in an asynchronous way with CompletableFuture
		geocodeService.generateCoordenates(newshop.getAddress()).thenAccept(coordinates -> {
			updateShopGeoCoordinates(newshop, coordinates);
		});
		return savedShop;
	}

	/**
	 * It orders the list of existing shops according to the distance given by
	 * latitude and longitude parameters In case we need to return more values,
	 * we already have the list sorted.
	 * 
	 * @param lat
	 *            latitude of the origin location
	 * @param longitude
	 *            longitude of the origin location
	 * 
	 * @return Shop returns the first element of the list, that is the nearest
	 *         one. Null if the the list is empty
	 */
	public Shop getNearestShop(Double lat, Double longitude) {
		Shop selectedShop = null;

		List<Shop> currentShops = (List<Shop>) repository.findAll();

		// we only check if the given parameters, at least one, is different
		// from zero
		if ((lat != 0 || longitude != 0) && currentShops.size() > 0) {
			currentShops.sort(new Comparator<Shop>() {

				@Override
				public int compare(Shop arg0, Shop arg1) {
					// the order is ascending
					if (arg0.getAddress().validValues() && arg1.getAddress().validValues()) {
						double arg0Distance = GeocodeService.getDistanceHaversine(lat, longitude,
								arg0.getAddress().getLatitude(), arg0.getAddress().getLongitude());
						double arg1Distance = GeocodeService.getDistanceHaversine(lat, longitude,
								arg1.getAddress().getLatitude(), arg1.getAddress().getLongitude());
						double comp = arg0Distance - arg1Distance;
						if (comp >= 0)
							return 1;
						else if (comp == 0)

							return 0;
						else
							return -1;

					} else if (arg0.getAddress().validValues() && !arg1.getAddress().validValues())
						return -1;
					else if (!arg0.getAddress().validValues() && !arg1.getAddress().validValues())
						return 0;
					else
						return 1;
				}
			});

			selectedShop = currentShops.get(0);
		}

		return selectedShop;
	}

	/**
	 * Returns a list with all the stored shops
	 * 
	 * @return List<Shop> returns a list with all the existing shops
	 */
	public List<Shop> listShops() {
		List<Shop> shops = (List<Shop>) repository.findAll();
		return shops;
	}

	/**
	 * Updates the geocoordenates of a given shop
	 * 
	 * @param shop
	 * @param geocoordenate
	 */
	private void updateShopGeoCoordinates(Shop shop, GeoCoordinate geocoordinate) {
		Shop updatedShop = new Shop(shop);
		if (geocoordinate != null) {
			Address prevAddress = updatedShop.getAddress();
			prevAddress.setLatitude(geocoordinate.getLatitude());
			prevAddress.setLongitude(geocoordinate.getLongitude());

			if (!repository.update(updatedShop)) {
				logger.warn("Old version of shop " + updatedShop.toString() + " : Coordenates not updated");
			}

		} else {
			logger.error("No geocoordenates for shop:" + updatedShop.getName());
		}

	}
}
