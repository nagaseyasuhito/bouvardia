package com.github.nagaseyasuhito.bouvardia.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class SearchResult implements Serializable {

    private Integer totalNumberOfResults;

    private Integer currentNumberOfResults;

    private Integer offset;

    public Integer getTotalNumberOfResults() {
        return this.totalNumberOfResults;
    }

    public void setTotalNumberOfResults(Integer totalNumberOfResults) {
        this.totalNumberOfResults = totalNumberOfResults;
    }

    public Integer getCurrentNumberOfResults() {
        return this.currentNumberOfResults;
    }

    public void setCurrentNumberOfResults(Integer currentNumberOfResults) {
        this.currentNumberOfResults = currentNumberOfResults;
    }

    public Integer getOffset() {
        return this.offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
