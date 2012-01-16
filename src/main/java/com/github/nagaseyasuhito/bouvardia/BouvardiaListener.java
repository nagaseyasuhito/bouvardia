package com.github.nagaseyasuhito.bouvardia;

import java.util.Map;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebListener;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

@WebListener
public class BouvardiaListener extends GuiceServletContextListener {

    @WebFilter("/*")
    public static class BouvardiaFilter extends GuiceFilter {
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {

            @Override
            protected void configureServlets() {
                this.install(new JerseyServletModule());
                this.install(new JpaPersistModule("default"));
                Map<String, String> params = ImmutableMap.<String, String> of(PackagesResourceConfig.PROPERTY_PACKAGES, BouvardiaListener.class.getPackage().getName() + ".api");

                this.serve("/api/*").with(GuiceContainer.class, params);
                this.filter("/api/*").through(PersistFilter.class);
            }
        });
    }
}
