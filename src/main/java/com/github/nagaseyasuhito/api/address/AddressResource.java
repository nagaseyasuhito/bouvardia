package com.github.nagaseyasuhito.api.address;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.github.nagaseyasuhito.entity.AddressSearchResult;
import com.google.inject.persist.Transactional;

@Path("address")
public class AddressResource {

	@GET
	@Path("search")
	@Produces
	@Transactional
	public AddressSearchResult searchBy(@QueryParam("query") String query) {
		return null;
	}
}
