package com.github.nagaseyasuhito.bouvardia;

import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebListener;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.nagaseyasuhito.bouvardia.api.address.AddressResource;
import com.github.nagaseyasuhito.bouvardia.api.development.InitializeResource;
import com.github.nagaseyasuhito.bouvardia.api.identicon.IdenticonResource;
import com.github.nagaseyasuhito.bouvardia.api.textimage.TextImageResource;
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
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);

        this.bridgeJulToSlf4J();
    }

    /**
     * @see {@linkplain http://blog.cn-consult.dk/2009/03/bridging-javautillogging-to-slf4j.html}
     */
    public void bridgeJulToSlf4J() {
        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        SLF4JBridgeHandler.install();
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {

            @Override
            protected void configureServlets() {
                // FIXME: 特定パッケージ以下の特定アノテーションが付加されたクラスを自動バインドする
                this.bind(AddressResource.class);
                this.bind(InitializeResource.class);
                this.bind(IdenticonResource.class);
                this.bind(TextImageResource.class);

                this.install(new JerseyServletModule());
                this.install(new JpaPersistModule("default"));

                this.bind(FullTextEntityManager.class).toProvider(FullTextEntityManagerProvider.class);

                String target = BouvardiaListener.class.getPackage().getName() + ".api";
                Map<String, String> params = ImmutableMap.<String, String> of(PackagesResourceConfig.PROPERTY_PACKAGES, target);

                this.serve("/api/*").with(GuiceContainer.class, params);
                this.filter("/api/*").through(PersistFilter.class);
            }
        });
    }
}
