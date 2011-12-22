package com.github.nagaseyasuhito.identicon.impl;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


import com.docuverse.identicon.IdenticonRenderer;
import com.docuverse.identicon.NineBlockIdenticonRenderer2;
import com.github.nagaseyasuhito.identicon.IdenticonResource;
import com.google.common.collect.Ranges;

public class IdenticonResourceImpl implements IdenticonResource {

    private IdenticonRenderer renderer = new NineBlockIdenticonRenderer2();

    public static final String MESSAGE_DIGEST_ALGORITHM = "SHA1";

    public static final int DEFAULT_SIZE = 24;

    public static final int MINIMUM_SIZE = 1;

    public static final int MAXIMUM_SIZE = 512;

    private static final ThreadLocal<MessageDigest> messageDigest = new ThreadLocal<MessageDigest>();

    @Override
    public Response get(String value) {
        return this.get(value, DEFAULT_SIZE);
    }

    @Override
    public Response get(String value, Integer size) {
        if (!Ranges.closed(MINIMUM_SIZE, MAXIMUM_SIZE).contains(size)) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        return Response.ok(this.renderer.render(this.buildHashedCode(value), size)).build();
    }

    private int buildHashedCode(String code) {
        if (IdenticonResourceImpl.messageDigest.get() == null) {
            try {
                IdenticonResourceImpl.messageDigest.set(MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        byte[] hashedCode = IdenticonResourceImpl.messageDigest.get().digest(code.getBytes(Charset.forName("utf-8")));

        return ((hashedCode[0] & 0xFF) << 24) | ((hashedCode[1] & 0xFF) << 16) | ((hashedCode[2] & 0xFF) << 8) | (hashedCode[3] & 0xFF);
    }
}
