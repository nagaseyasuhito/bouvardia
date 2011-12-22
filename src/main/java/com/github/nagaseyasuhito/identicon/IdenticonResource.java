package com.github.nagaseyasuhito.identicon;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.github.nagaseyasuhito.BouvardiaApplication;


@Path("identicon")
public interface IdenticonResource {

    @GET
    @Path("{value}")
    @Produces(BouvardiaApplication.IMAGE_PNG)
    Response get(@PathParam("value") String value);

    @GET
    @Path("{value}/{size}")
    @Produces(BouvardiaApplication.IMAGE_PNG)
    Response get(@PathParam("value") String value, @PathParam("size") Integer size);
}
