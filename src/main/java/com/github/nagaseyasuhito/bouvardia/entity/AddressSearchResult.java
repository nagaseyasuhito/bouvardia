package com.github.nagaseyasuhito.bouvardia.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement
public class AddressSearchResult extends SearchResult {

	@XmlElementWrapper
	@XmlElement(name = "address")
	private List<Address> addresses;

	public List<Address> getAddresses() {
		return this.addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
}
