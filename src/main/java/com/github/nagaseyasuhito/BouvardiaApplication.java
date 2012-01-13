package com.github.nagaseyasuhito;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import com.github.nagaseyasuhito.api.identicon.impl.IdenticonResourceImpl;
import com.github.nagaseyasuhito.api.textimage.impl.TextImageResourceImpl;
import com.google.common.collect.Sets;

@ApplicationPath("/api")
public class BouvardiaApplication extends Application {
    /**
     * charsetにutf-8が指定された{@code @Produces}で利用するcontent-type。
     */
    public static final String APPLICATION_JSON_CHARSET_UTF_8 = MediaType.APPLICATION_JSON + ";charset=utf-8";

    /**
     * charsetにutf-8が指定された{@code @Produces}で利用するcontent-type。
     */
    public static final String APPLICATION_XML_CHARSET_UTF_8 = MediaType.APPLICATION_XML + ";charset=utf-8";

    /**
     * {@code @Produces}で利用するcontent-type。
     */
    public static final String IMAGE_PNG = "image/png";

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = Sets.newHashSet();
        classes.add(BouvardiaExceptionMapper.class);

        classes.add(IdenticonResourceImpl.class);
        classes.add(TextImageResourceImpl.class);

        return classes;
    }

}
