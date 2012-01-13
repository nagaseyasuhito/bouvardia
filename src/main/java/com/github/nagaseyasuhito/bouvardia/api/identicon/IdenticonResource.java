package com.github.nagaseyasuhito.bouvardia.api.identicon;

import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.docuverse.identicon.IdenticonRenderer;
import com.docuverse.identicon.NineBlockIdenticonRenderer2;
import com.github.nagaseyasuhito.bouvardia.BouvardiaApplication;
import com.github.nagaseyasuhito.bouvardia.BouvardiaException;
import com.google.common.collect.Ranges;

/**
 * Identiconを生成する。
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Identicon>Identicon</a>
 */
@Path("identicon")
public class IdenticonResource {

	public static final int DEFAULT_SIZE = 24;

	public static final int MAXIMUM_SIZE = 512;

	public static final String MESSAGE_DIGEST_ALGORITHM = "SHA1";

	public static final int MINIMUM_SIZE = 1;

	private static final ThreadLocal<MessageDigest> MESSAGE_DIGEST = new ThreadLocal<MessageDigest>();

	private IdenticonRenderer renderer = new NineBlockIdenticonRenderer2();

	/**
	 * 一辺のサイズが24pxのIdenticonを生成する。
	 * 
	 * @param value
	 *            生成対象の文字列
	 * @return Identiconの画像
	 */
	@GET
	@Path("{value}")
	@Produces(BouvardiaApplication.IMAGE_PNG)
	public BufferedImage build(@PathParam("value") String value) {
		return this.build(value, IdenticonResource.DEFAULT_SIZE);
	}

	/**
	 * サイズを指定してIdenticonを生成する。
	 * 
	 * @param value
	 *            生成対象の文字列
	 * @param size
	 *            一辺のサイズ(px)
	 * @throws IllegalArgumentException
	 *             一辺のサイズが1～512以外の場合
	 * @return Identiconの画像
	 */
	@GET
	@Path("{value}/{size}")
	@Produces(BouvardiaApplication.IMAGE_PNG)
	public BufferedImage build(@PathParam("value") String value, @PathParam("size") Integer size) {
		if (!Ranges.closed(IdenticonResource.MINIMUM_SIZE, IdenticonResource.MAXIMUM_SIZE).contains(size)) {
			throw new BouvardiaException(new IllegalArgumentException("invalid size"));
		}

		return this.renderer.render(this.buildHashedCode(value), size);
	}

	private int buildHashedCode(String value) {
		if (IdenticonResource.MESSAGE_DIGEST.get() == null) {
			try {
				IdenticonResource.MESSAGE_DIGEST.set(MessageDigest.getInstance(IdenticonResource.MESSAGE_DIGEST_ALGORITHM));
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}

		byte[] hashedCode = IdenticonResource.MESSAGE_DIGEST.get().digest(value.getBytes(Charset.forName("utf-8")));

		return ((hashedCode[0] & 0xFF) << 24) | ((hashedCode[1] & 0xFF) << 16) | ((hashedCode[2] & 0xFF) << 8) | (hashedCode[3] & 0xFF);
	}
}
