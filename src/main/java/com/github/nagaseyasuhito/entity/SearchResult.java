package com.github.nagaseyasuhito.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class SearchResult implements Serializable {

	private Long numberOfResults;

	public Long getNumberOfResults() {
		return this.numberOfResults;
	}

	public void setNumberOfResults(Long numberOfResults) {
		this.numberOfResults = numberOfResults;
	}
}
