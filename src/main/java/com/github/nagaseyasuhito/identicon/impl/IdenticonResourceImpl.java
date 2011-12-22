package com.github.nagaseyasuhito.identicon.impl;

import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.docuverse.identicon.IdenticonRenderer;
import com.docuverse.identicon.NineBlockIdenticonRenderer2;
import com.github.nagaseyasuhito.BouvardiaException;
import com.github.nagaseyasuhito.identicon.IdenticonResource;
import com.google.common.collect.Ranges;

public class IdenticonResourceImpl implements IdenticonResource {

    private static final ThreadLocal<MessageDigest> MESSAGE_DIGEST = new ThreadLocal<MessageDigest>();

    private IdenticonRenderer renderer = new NineBlockIdenticonRenderer2();

    private int buildHashedCode(String value) {
        if (IdenticonResourceImpl.MESSAGE_DIGEST.get() == null) {
            try {
                IdenticonResourceImpl.MESSAGE_DIGEST.set(MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        byte[] hashedCode = IdenticonResourceImpl.MESSAGE_DIGEST.get().digest(value.getBytes(Charset.forName("utf-8")));

        return ((hashedCode[0] & 0xFF) << 24) | ((hashedCode[1] & 0xFF) << 16) | ((hashedCode[2] & 0xFF) << 8) | (hashedCode[3] & 0xFF);
    }

    @Override
    public BufferedImage build(String value) {
        return this.build(value, DEFAULT_SIZE);
    }

    @Override
    public BufferedImage build(String value, Integer size) {
        if (!Ranges.closed(MINIMUM_SIZE, MAXIMUM_SIZE).contains(size)) {
            throw new BouvardiaException(new IllegalArgumentException("invalid size"));
        }

        return this.renderer.render(this.buildHashedCode(value), size);
    }
}
