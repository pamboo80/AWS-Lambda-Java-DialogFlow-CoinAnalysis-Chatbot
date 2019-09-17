package com.amazonaws.lambda.demo;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LambdaFunctionHandlerTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		// TODO: set up your sample input object here.
		input = "hello";
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		// TODO: customize your context here if needed.
		ctx.setFunctionName("Your Function Name");
		return ctx;
	}

	@Test
	public void testLambdaFunctionHandler() {
		LambdaFunctionHandler handler = new LambdaFunctionHandler();
		Context ctx = createContext();
		//String inputdata = "{\"duration\":[{\"amount\":7,\"unit\":\"day\"}],\"coin\":[\"btc\"],\"detail_list\":[\"chart\"]}";
		//String inputdata ="{\"number\":[],\"miscellaneous\":[\"coin\"],\"analyze_type\":\"best\"}";
		//String inputdata ="{\"number\":[2018.0],\"miscellaneous\":[\"ico\"],\"duration\":[],\"analyze_type\":\"best\"}";
		//String inputdata ="{\"number\":[2018.0],\"duration\":[],\"analyze_type\":\"best\",\"miscellaneous\":[\"coin\"]}";
		String inputdata ="{\"number\":[1.0],\"duration\":[{\"amount\":10.0,\"unit\":\"day\"}],\"analyze_type\":\"best\",\"miscellaneous\":[\"ico\"],\"analyze_factor\":[\"times\"]}";
		// System.out.println(inputdata);
		String output = handler.handleRequest(inputdata, ctx);
		System.out.println(output);

		// TODO: validate output here if needed.
		// Assert.assertEquals("Hello from Lambda!", output);
	}
}
