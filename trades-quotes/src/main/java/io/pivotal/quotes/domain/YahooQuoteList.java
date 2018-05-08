package io.pivotal.quotes.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class YahooQuoteList {
	
	@JsonProperty(value="quote")
	private List<YahooQuote> quote;

	public List<YahooQuote> getQuote() {
		return quote;
	}

	public void setQuote(List<YahooQuote> quote) {
		this.quote = quote;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("YahooQuoteList [quote=").append(quote).append("]");
		return builder.toString();
	}
	
}