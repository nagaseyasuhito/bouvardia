package com.github.nagaseyasuhito.bouvardia;

import java.util.Map;

import javax.servlet.annotation.WebListener;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

@WebListener
public class BouvardiaListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new JerseyServletModule() {
			@Override
			protected void configureServlets() {
				Map<String, String> params = ImmutableMap.<String, String> of(PackagesResourceConfig.PROPERTY_PACKAGES, BouvardiaListener.class.getPackage().getName());

				this.serve("/api/*").with(GuiceContainer.class, params);
			}
		});
	}

}
