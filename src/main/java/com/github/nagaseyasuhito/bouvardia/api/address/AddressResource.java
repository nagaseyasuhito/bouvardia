package com.github.nagaseyasuhito.bouvardia.api.address;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.github.nagaseyasuhito.bouvardia.entity.AddressSearchResult;
import com.github.nagaseyasuhito.bouvardia.service.AddressService;
import com.google.inject.persist.Transactional;

@Path("address")
public class AddressResource {

	@Inject
	private AddressService addressService;

	@GET
	@Path("search")
	@Produces
	@Transactional
	public AddressSearchResult searchBy(@QueryParam("query") String query) {
		this.addressService.findByCode(10L);
		return null;
	}
}
