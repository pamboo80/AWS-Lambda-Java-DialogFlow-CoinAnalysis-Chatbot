package com.amazonaws.lambda.demo;

import org.json.JSONObject;

import com.google.gson.Gson;

public class Data {

	String coin_name, symbol, rank, circulating_supply, total_supply, max_supply, price, volume_24h, market_cap,
			percent_change_1h, percent_change_24h, percent_change_7d, historical_data, currency, price_btc,last_updated;	

	public String getLastUpdated() {
		return last_updated;
	}

	public void setLastUpdated(String last_updated) {
		this.last_updated = last_updated;
	}
	
	public String getPrice_btc() {
		return price_btc;
	}

	public void setPrice_btc(String price_btc) {
		this.price_btc = price_btc;
	}

	public String getCoin_name() {
		return coin_name;
	}

	public void setCoin_name(String coin_name) {
		this.coin_name = coin_name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getCirculating_supply() {
		return circulating_supply;
	}

	public void setCirculating_supply(String circulating_supply) {
		this.circulating_supply = circulating_supply;
	}

	public String getTotal_supply() {
		return total_supply;
	}

	public void setTotal_supply(String total_supply) {
		this.total_supply = total_supply;
	}

	public String getMax_supply() {
		return max_supply;
	}

	public void setMax_supply(String max_supply) {
		this.max_supply = max_supply;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getVolume_24h() {
		return volume_24h;
	}

	public void setVolume_24h(String volume_24h) {
		this.volume_24h = volume_24h;
	}

	public String getMarket_cap() {
		return market_cap;
	}

	public void setMarket_cap(String market_cap) {
		this.market_cap = market_cap;
	}

	public String getPercent_change_1h() {
		return percent_change_1h;
	}

	public void setPercent_change_1h(String percent_change_1h) {
		this.percent_change_1h = percent_change_1h;
	}

	public String getPercent_change_24h() {
		return percent_change_24h;
	}

	public void setPercent_change_24h(String percent_change_24h) {
		this.percent_change_24h = percent_change_24h;
	}

	public String getPercent_change_7d() {
		return percent_change_7d;
	}

	public void setPercent_change_7d(String percent_change_7d) {
		this.percent_change_7d = percent_change_7d;
	}

	public String getHistorical_data() {
		return historical_data;
	}

	public void setHistorical_data(String historical_data) {
		this.historical_data = historical_data;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public JSONObject getJSONObject() {
		Gson gson = new Gson();
		String jsonString = gson.toJson(this);
		JSONObject jsonObject = new JSONObject(jsonString);
		return jsonObject;

	}
}
