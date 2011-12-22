package com.github.nagaseyasuhito.api.identicon;

import java.awt.image.BufferedImage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.github.nagaseyasuhito.BouvardiaApplication;

/**
 * Identiconを生成する。
 * @see <a href="http://en.wikipedia.org/wiki/Identicon>Identicon</a>
 */
@Path("identicon")
public interface IdenticonResource {

    public static final int DEFAULT_SIZE = 24;

    public static final int MAXIMUM_SIZE = 512;

    public static final String MESSAGE_DIGEST_ALGORITHM = "SHA1";

    public static final int MINIMUM_SIZE = 1;

    /**
     * 一辺のサイズが24pxのIdenticonを生成する。
     * @param value 生成対象の文字列
     * @return Identiconの画像
     */
    @GET
    @Path("{value}")
    @Produces(BouvardiaApplication.IMAGE_PNG)
    BufferedImage build(@PathParam("value") String value);

    /**
     * サイズを指定してIdenticonを生成する。
     * @param value 生成対象の文字列
     * @param size 一辺のサイズ(px)
     * @throws IllegalArgumentException 一辺のサイズが1～512以外の場合
     * @return Identiconの画像
     */
    @GET
    @Path("{value}/{size}")
    @Produces(BouvardiaApplication.IMAGE_PNG)
    BufferedImage build(@PathParam("value") String value, @PathParam("size") Integer size);
}
