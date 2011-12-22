package com.github.nagaseyasuhito.api.textimage;

import java.awt.image.BufferedImage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.github.nagaseyasuhito.BouvardiaApplication;

@Path("textimage")
public interface TextImageResource {
    @GET
    @Path("{value}")
    @Produces(BouvardiaApplication.IMAGE_PNG)
    BufferedImage build(@PathParam("value") String value);

}
