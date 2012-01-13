package com.github.nagaseyasuhito.entity;

import java.util.List;

@SuppressWarnings("serial")
public class AddressSearchResult extends SearchResult {

	private List<Address> addresses;

	public List<Address> getAddresses() {
		return this.addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
}
