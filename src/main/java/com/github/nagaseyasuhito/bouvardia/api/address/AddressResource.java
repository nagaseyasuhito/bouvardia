package com.github.nagaseyasuhito.bouvardia.api.address;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.github.nagaseyasuhito.bouvardia.BouvardiaConstants;
import com.github.nagaseyasuhito.bouvardia.entity.Address;
import com.github.nagaseyasuhito.bouvardia.entity.AddressSearchResult;
import com.github.nagaseyasuhito.bouvardia.service.AddressService;
import com.google.inject.persist.Transactional;

@Path("address")
public class AddressResource {

	@Inject
	private AddressService addressService;

	@GET
	@Path("search")
	@Produces({ BouvardiaConstants.APPLICATION_XML_CHARSET_UTF_8, BouvardiaConstants.APPLICATION_JSON_CHARSET_UTF_8 })
	@Transactional
	public AddressSearchResult searchByQuery(@QueryParam("query") String query) {
		return this.addressService.findByQuery(query);
	}

	@GET
	@Path("{code}")
	@Produces({ BouvardiaConstants.APPLICATION_XML_CHARSET_UTF_8, BouvardiaConstants.APPLICATION_JSON_CHARSET_UTF_8 })
	@Transactional
	public Address searchByCode(@PathParam("code") Long code) {
		return this.addressService.findByCode(code);
	}
}
