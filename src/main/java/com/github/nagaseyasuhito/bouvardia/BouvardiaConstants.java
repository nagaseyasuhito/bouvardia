package com.github.nagaseyasuhito.bouvardia;

import javax.ws.rs.core.MediaType;

public class BouvardiaConstants {
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
}
