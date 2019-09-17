package com.amazonaws.lambda.demo;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;

public class CARedisClient {
	
	private static RedisClient redisClient;
	private static RedisConnection<String, String> connection;
	
	private static String redisConnectionString="redis://password@redis-server";

	public static void testRedisClient()
	{		
		 redisClient = new RedisClient(
	     RedisURI.create(redisConnectionString));
	     connection = redisClient.connect();

	     System.out.println("Connected to Redis");

	     connection.close();
	     redisClient.shutdown();		 
	}
	
	public static boolean connectRedisClient()
	{		
	   try
	   {
		 redisClient = new RedisClient(
	     RedisURI.create(redisConnectionString));
	     connection = redisClient.connect();

	     System.out.println("Connected to Redis");	     
	   }catch(Exception ex)
	   {
		   return false;
	   }
	   return true;
	}

	public static boolean disconnectRedisClient()
	{
		
	   try{
		   if(connection.isOpen()) {
			   connection.close();
			   redisClient.shutdown();	
			   System.out.println("Disconnected to Redis");	
		   }		       
	   }catch(Exception ex)
	   {
		   return false;
	   }
	   return true;
		
	}
}
