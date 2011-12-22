package com.github.nagaseyasuhito.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Error {

    private String message;

    private Class<? extends Throwable> type;

    public String getMessage() {
        return this.message;
    }

    public Class<? extends Throwable> getType() {
        return this.type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(Class<? extends Throwable> type) {
        this.type = type;
    }
}
