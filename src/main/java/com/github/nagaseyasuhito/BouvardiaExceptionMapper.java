package com.github.nagaseyasuhito;

import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.github.nagaseyasuhito.entity.Error;
import com.google.common.collect.ImmutableList;

@Provider
public class BouvardiaExceptionMapper implements ExceptionMapper<BouvardiaException> {
	@Context
	private HttpHeaders headers;

	private MediaType getResponseMediaType() {
		List<MediaType> mediaTypes = ImmutableList.<MediaType> of(MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_JSON_TYPE);

		for (MediaType a : this.headers.getAcceptableMediaTypes()) {
			if (a.getType().equals(MediaType.MEDIA_TYPE_WILDCARD)) {
				return mediaTypes.get(0);
			}

			for (MediaType m : mediaTypes) {
				if (m.isCompatible(a) && !m.isWildcardType() && !m.isWildcardSubtype()) {
					return m;
				}
			}
		}
		return mediaTypes.get(0);
	}

	@Override
	public Response toResponse(BouvardiaException exception) {
		Error error = new Error();
		error.setMessage(exception.getCause().getMessage());
		error.setType(exception.getCause().getClass());
		return Response.status(Status.BAD_REQUEST).type(this.getResponseMediaType()).entity(error).build();
	}
}
