package com.gft.retailManagerApp.domain;

import java.util.concurrent.atomic.AtomicInteger;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * It represents the entity to be stored in the repository. It contains all the
 * information to manage shops
 *
 * @author Vanessa Cantalapiedra
 * @version 1.0
 * @since 20-04-2017
 */

public class Shop {

	@NotNull
	@Size(min = 1, max = 20)
	private String name;

	private Address address;

	@JsonIgnore
	private AtomicInteger version = new AtomicInteger(0); // It is used to
															// control version
															// between instances

	public AtomicInteger getVersion() {
		return version;
	}

	public Shop() {
		address = new Address();
	}

	public Shop(Shop shop) {
		this.name = shop.name;
		this.address = new Address(shop.address);
		this.version = shop.version;
	}

	public Shop(String name, Address address) {
		this.name = name;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public Address getAddress() {
		return address;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * Gets the next version of the instance.
	 */
	public int nextVersion() {
		return version.get() + 1;
	}

	/**
	 * Atomically adds +1 to the current version number
	 */
	public int autoIncrement() {
		return version.addAndGet(1);
	}

	/**
	 * Atomically updates current version number
	 */
	public void updateVersion(int newversion) {
		version.set(newversion);
	}

	@Override
	public String toString() {

		return "Version: " + this.version + ", name : " + name + " ," + address.toString();

	}

}
