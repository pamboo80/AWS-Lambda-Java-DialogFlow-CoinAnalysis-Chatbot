package com.amazonaws.lambda.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import com.lambdaworks.redis.*;

public class DialogResponse {

	// Initialize the Log4j logger.
	// static final Logger logger = LogManager.getLogger(DialogResponse.class);

	String parseResponse(String query) {

		JSONObject jsonObject = new JSONObject();
		JSONObject jsonFromDialog = new JSONObject(query);

		// System.out: One log statement but with a line break (AWS Lambda writes two
		// events to CloudWatch).
		// System.out.println(query);

		try {

			String coin = "";
			Object objCoin = null;
			try {
				objCoin = jsonFromDialog.get("coin");
				if (objCoin instanceof JSONArray) {
					if (((JSONArray) objCoin).length() > 0) {
						coin = ((JSONArray) objCoin).get(0).toString();
					}
				} else if (objCoin instanceof String) {
					coin = jsonFromDialog.getString("coin");
				}

				Data mData = getCoinDetail(coin);
				jsonObject = mData.getJSONObject();
			} catch (JSONException e) {
				Data data = new Data();
				jsonObject = data.getJSONObject();
			}

			boolean is24Hour = false;
			int day = -1;
			try {
				jsonFromDialog.get("days");
				if (!jsonFromDialog.isNull("days")) {
					String days = jsonFromDialog.getString("days");
					day = Integer.parseInt(days);
				}
			} catch (JSONException e) {
			}

			try {
				jsonFromDialog.get("duration");
				if (!jsonFromDialog.isNull("duration")) {
					JSONArray jsonArray = (JSONArray) jsonFromDialog.get("duration");
					if (jsonArray.length() > 0 && !jsonArray.isNull(0)) {
						JSONObject object = jsonArray.getJSONObject(0);
						float daydata = object.getFloat("amount");
						daydata = Math.abs(daydata);
						String type = object.getString("unit");
						switch (type) {
						case "day":
							day = (int) daydata;
							break;
						case "wk":
							day = (int) (daydata * 7);
							break;
						case "mo":
							day = (int) (daydata * 30);
							break;
						case "yr":
							day = (int) (daydata * 365);
							break;
						case "h":
							day = (int) (daydata);
							is24Hour = true;
							break;
						case "min":
							day = (int) (daydata);
							day = day / 60;
							if (day == 0) {
								day = 24;
							}
							is24Hour = true;
							break;
						default:
							day = (int) daydata;
							break;
						}
					}
				}
			} catch (JSONException e) {

			}

			String analyze_type = "";
			int year = -1;
			int analyze_factor_unit = -1;
			String analyze_factor = "";
			String currency_type = "";

			try {
				analyze_type = jsonFromDialog.get("analyze_type").toString();
				if (!analyze_type.equals("")) {
					try {
						if (!jsonFromDialog.isNull("miscellaneous")) {
							JSONArray miscellaneous_list = (JSONArray) jsonFromDialog.get("miscellaneous");
							for (int i = 0; i <= miscellaneous_list.length(); i++) {
								if (!miscellaneous_list.isNull(i)) {
									String detail = miscellaneous_list.getString(i);
									if (detail.toLowerCase().compareTo("coin") == 0
											|| detail.toLowerCase().compareTo("ico") == 0) {
										currency_type = detail.toLowerCase();
										break;
									}
								}
							}
						}
					} catch (JSONException e) {
						currency_type = "";
					}

					try {
						analyze_factor = jsonFromDialog.get("analyze_factor").toString();
					} catch (JSONException e) {
						analyze_factor = "";
					}

					try {
						JSONArray jsonArray = (JSONArray) jsonFromDialog.get("number");
						if (jsonArray.length() == 1) {
							if (!jsonArray.isNull(0)) {
								int num1;
								num1 = jsonArray.getInt(0);
								num1 = Math.abs(num1);
								if (analyze_factor.toLowerCase().compareTo("") == 0) {
									year = num1;
								} else {
									if (num1 >= 2010 && num1 <= 2030) {
										year = num1;
									} else {
										analyze_factor_unit = num1;
									}
								}
							}
						}
						if (jsonArray.length() == 2) {
							int num1, num2;
							num1 = jsonArray.getInt(0);
							num1 = Math.abs(num1);

							num2 = jsonArray.getInt(1);
							num2 = Math.abs(num2);

							analyze_factor_unit = num1;
							year = num2;
						}

					} catch (JSONException e) {
					}

					//
					if (analyze_factor.toLowerCase().compareTo("times") == 0) {
					} else if (analyze_factor.toLowerCase().compareTo("percentage") == 0) {
					} else {
					}

					String fromDate,fromDateDisplay="";
					String toDate,toDateDisplay="";
					
					String pattern = "yyyy-MM-dd";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
					Date today = new Date(); //today date
										
					Calendar c = Calendar.getInstance();					
					c.setTime(today);
					c.add(Calendar.DAY_OF_MONTH, -1);  
					toDateDisplay = simpleDateFormat.format(c.getTime());  
										   
					c.setTime(today);
					c.add(Calendar.DAY_OF_MONTH, 1);  					
					toDate = simpleDateFormat.format(c.getTime());  
					
					Date date = new Date(today.getYear(), 0, 1);
				    c.setTime(date);			
				    fromDateDisplay = simpleDateFormat.format(c.getTime()); 
				    				    
					c.add(Calendar.DAY_OF_MONTH, -1);  					
					fromDate = simpleDateFormat.format(c.getTime());   
				    
					if (day > 0) {											
					    c.setTime(today);				    					    
						c.add(Calendar.DAY_OF_MONTH, -day-1);  						
						fromDate = simpleDateFormat.format(c.getTime()); 											
						//fromDateDisplay = fromDate;
												
						c.setTime(today);				    					    
						c.add(Calendar.DAY_OF_MONTH, -day);  						
						fromDateDisplay = simpleDateFormat.format(c.getTime());						
					}
					else if (year >=0 && year!=(today.getYear()+1900))
					{
						 date = new Date(year-1900, 0, 1);
						 c.setTime(date);		
						 fromDateDisplay = simpleDateFormat.format(c.getTime()); 
					
						 c.add(Calendar.DAY_OF_MONTH, -1);  						
						 fromDate = simpleDateFormat.format(c.getTime()); 
						 
						 date = new Date(year-1900+1, 0, 2);
						 c.setTime(date);						 
						 toDate = simpleDateFormat.format(c.getTime()); 
						 
						 c.add(Calendar.DAY_OF_MONTH, -2);  
						 toDateDisplay = simpleDateFormat.format(c.getTime()); 
					}				
					
					if(day!=0)
					{
						JSONArray jsonArrayAnalysis = getAnalysis(analyze_type, analyze_factor, analyze_factor_unit, currency_type,fromDate,toDate);														
						jsonObject.put("analysis",jsonArrayAnalysis);
						jsonObject.put("fromDate",fromDateDisplay);
						jsonObject.put("toDate",toDateDisplay);
					}
					else
					{
						jsonObject.put("analysis", new JSONArray());
					}					
					
				} else {
					jsonObject.put("analysis", new JSONArray());
				}
			} catch (JSONException e) {
				jsonObject.put("analysis", new JSONArray());
			}

			if (day == -1) {
				is24Hour = true;
				day = 24;
			}

			day = Math.abs(day);
			if (!coin.equals("")) {
				jsonObject.put("is24Hour", is24Hour);

				if (is24Hour) {
					JSONArray jsonArrayHistorical = getHistoricalDataFromDatabaseByHour(coin, day);
					jsonObject.put("data", jsonArrayHistorical);
				} else {
					JSONArray jsonArrayHistorical = getHistoricalDataFromDatabaseByDay(coin, day);
					jsonObject.put("data", jsonArrayHistorical);
				}

				JSONArray jsonArrayYearlyTrend = getYearlyTrendFromDatabase(coin);
				jsonObject.put("yearlytrend", jsonArrayYearlyTrend);
			}

			jsonObject.put("success", "true");

		} catch (JSONException e) {
			JSONArray jsonArrayDataEmpty = new JSONArray();		
			jsonObject.put("success", "false");
			jsonObject.put("data", new JSONArray());
			jsonObject.put("yearlytrend", new JSONArray());
			jsonObject.put("analysis", new JSONArray());
		}

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
				data.setPrice_btc(rs.getString("price_btc"));
				data.setLastUpdated(rs.getString("last_updated"));
			}
			rs.close();
			st.close();
			conn.close();

		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e.getMessage());
		} catch (Exception ex) {
			System.out.println("General Exception: " + ex.getMessage());
		}
		return data;
	}

	private JSONArray getHistoricalDataFromDatabaseByDay(String coin, int limit) {

		JSONArray array = new JSONArray();
		try {
			Database database = new Database();
			String query = " select * from historical_data_by_day where coin_symbol=\"" + coin
					+ "\"  ORDER BY time desc limit " + limit;
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
	
	private JSONArray getAnalysis(String analyze_type, String analyze_factor, int analyze_factor_unit, String currencyType,String fromDate, String toDate) {
		
		//CARedisClient.connectRedisClient();
		//CARedisClient.disconnectRedisClient();
		
		JSONArray array = new JSONArray();
		try {
			Database database = new Database();
			String query = "select * from (select *, ((sum(history_price) - history_price * 2) / history_price) * 100 as percentage_change from (select analysis.* from analysis, " + 
					"((select analysis.coin_symbol,max(history_price_date) as maxdate ,min(history_price_date) as mindate " + 
					"from analysis where history_price_date > '"  + fromDate  + "' and history_price_date < '" + toDate + "' " + 
					((currencyType.compareToIgnoreCase("ico")==0)?"and ISNULL(platform_symbol)=false ": " ") + 
					"group by coin_symbol) as temp) " + 
					"where analysis.coin_symbol = temp.coin_symbol and " + 
					"(analysis.history_price_date = temp.maxdate or " + 
					"analysis.history_price_date = temp.mindate) " +
				    "order by coin_symbol,history_price_date) as temp1 " + 
				    "where rank <="+
				    ((currencyType.compareToIgnoreCase("ico")==0)?"1000 ": "100 ") + 
				    "group by coin_symbol " + 
				    "order by rank asc) as temp2 " + 
				    ((analyze_factor_unit!=-1)? "where percentage_change >= " + ((analyze_factor.compareToIgnoreCase("times")==0)?(analyze_factor_unit * 100)+ " ":analyze_factor_unit + " "):"") + 
				    "order by percentage_change "+ ((analyze_type.compareToIgnoreCase("worst")==0)? "asc" : "desc") + ";";
			
			System.out.println(query);
			Connection conn = database.getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) { 		
				
					JSONObject jsonObject = new JSONObject();					
					jsonObject.put("coin_symbol", rs.getString("coin_symbol"));
					jsonObject.put("coin_name", rs.getString("coin_name"));
					jsonObject.put("price", rs.getString("today_price"));
					jsonObject.put("percentage_change", rs.getString("percentage_change")); 
					jsonObject.put("platform", rs.getString("platform_symbol"));
					jsonObject.put("rank", rs.getString("rank"));
					
					array.put(jsonObject);				
			}
			rs.close();
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("rows count: " + array.length());
		return array;
	}

	private JSONArray getYearlyTrendFromDatabase(String coin) {
		JSONArray array = new JSONArray();
		try {
			Database database = new Database();
			String query = "select coin.coin_symbol,coin.coin_name,historical_data_by_day.close as history_price, DATE_FORMAT(FROM_UNIXTIME(time), \"%M %d %Y\") as history_price_date from historical_data_by_day,coin "
					+ "where historical_data_by_day.coin_symbol = coin.coin_symbol "
					+ "and historical_data_by_day.coin_symbol='" + coin + "' and ("
					+ "(YEAR(FROM_UNIXTIME(time))='2013' and MONTH(FROM_UNIXTIME(time))='1' and DAY(FROM_UNIXTIME(time))='1') OR "
					+ "(YEAR(FROM_UNIXTIME(time))='2014' and MONTH(FROM_UNIXTIME(time))='1' and DAY(FROM_UNIXTIME(time))='1') OR "
					+ "(YEAR(FROM_UNIXTIME(time))='2015' and MONTH(FROM_UNIXTIME(time))='1' and DAY(FROM_UNIXTIME(time))='1') OR "
					+ "(YEAR(FROM_UNIXTIME(time))='2016' and MONTH(FROM_UNIXTIME(time))='1' and DAY(FROM_UNIXTIME(time))='1') OR "
					+ "(YEAR(FROM_UNIXTIME(time))='2017' and MONTH(FROM_UNIXTIME(time))='1' and DAY(FROM_UNIXTIME(time))='1') OR "
					+ "(YEAR(FROM_UNIXTIME(time))='2018' and MONTH(FROM_UNIXTIME(time))='1' and DAY(FROM_UNIXTIME(time))='1') OR "
					+ "(YEAR(FROM_UNIXTIME(time))='2019' and MONTH(FROM_UNIXTIME(time))='1' and DAY(FROM_UNIXTIME(time))='1') OR "
					+ "historical_data_by_day.time=coin.history_data_start_time OR "
					+ "historical_data_by_day.time=coin.history_data_end_time) " 
					+ "order by historical_data_by_day.time asc;";
			Connection conn = database.getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) { 
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("coin_symbol", rs.getString("coin_symbol"));
				jsonObject.put("coin_name", rs.getString("coin_name"));
				jsonObject.put("history_price", rs.getString("history_price"));
				jsonObject.put("history_price_date", rs.getString("history_price_date"));
				
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

	private JSONArray getHistoricalDataFromDatabaseByHour(String coin, int limit) {

		JSONArray array = new JSONArray();
		try {
			Database database = new Database();
			String query = " select * from historical_data_by_hour where coin_symbol=\"" + coin
					+ "\"  ORDER BY time desc limit " + limit;
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
