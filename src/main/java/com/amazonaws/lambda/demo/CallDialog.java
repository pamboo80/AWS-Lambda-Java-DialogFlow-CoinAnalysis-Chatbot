package com.amazonaws.lambda.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class CallDialog {

	// Initialize the Log4j logger.
    //static final Logger logger = LogManager.getLogger(CallDialog.class);

	public CallDialog() {
		// TODO Auto-generated constructor stub
	}

	String call(String query) {
		
		// System.out: One log statement but with a line break (AWS Lambda writes two events to CloudWatch).
        System.out.println("call: "+ query);

		//using v1 , going to be deprecated in Oct 2019 - Need to be moved to v2.0
		final AIConfiguration config = new AIConfiguration("DialogFlow Token",
				AIConfiguration.SupportedLanguages.English);
		final AIDataService aiDataService = new AIDataService(config);
		final AIRequest aiRequest = new AIRequest();
		aiRequest.setQuery(query);
		JSONObject jsonObject = new JSONObject();
	
		Data mData = getCoinDetail(query);
		jsonObject = mData.getJSONObject();
		return jsonObject.toString();			
	}

	private Data getCoinDetail(String coin) {
		Data data = new Data();
		try {
			Database database = new Database();
			String query = " select * from coin	 where coin_symbol=\"" + coin + "\"";
			Connection conn = database.getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				data.setSymbol(rs.getString("coin_symbol"));
				data.setCoin_name(rs.getString("coin_name"));
				data.setRank(rs.getString("rank"));
				data.setTotal_supply(rs.getString("total_supply"));
				data.setMax_supply(rs.getString("max_supply"));
				data.setCirculating_supply(rs.getString("circulating_supply"));
				data.setPrice(rs.getString("price"));
				data.setMarket_cap(rs.getString("market_cap"));
				data.setVolume_24h(rs.getString("volume_24h"));
				data.setPercent_change_24h(rs.getString("percent_change_24h"));
				data.setPercent_change_1h(rs.getString("percent_change_1h"));
				data.setPercent_change_7d(rs.getString("percent_change_7d"));
			}
			rs.close();
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	private JSONArray getHistoricalDataFromDatabase(String coin, int limit) {

		JSONArray array = new JSONArray();
		try {
			Database database = new Database();
			String query = " select * from historical_data_by_day where coin_symbol=\"" + coin
					+ "\"  ORDER BY time DESC limit " + limit;
			Connection conn = database.getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("coin_symbol", rs.getString("coin_symbol"));
				jsonObject.put("time", rs.getString("time"));
				jsonObject.put("open", rs.getString("open"));
				jsonObject.put("close", rs.getString("close"));
				jsonObject.put("high", rs.getString("high"));
				jsonObject.put("low", rs.getString("low"));
				array.put(jsonObject);
			}
			rs.close();
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return array;
	}

}
