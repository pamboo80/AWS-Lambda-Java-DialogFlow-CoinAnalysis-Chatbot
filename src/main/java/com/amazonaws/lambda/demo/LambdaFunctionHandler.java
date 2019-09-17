package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<String, String> {
	@Override
	public String handleRequest(String input, Context context) {
		System.out.println(input);
		if (input.length() > 0 && input.charAt(0) == '"' && input.charAt(input.length() - 1) == '"') {
			input = input.substring(1, input.length() - 1);
		}
		DialogResponse dialogResponse = new DialogResponse();
		String response = dialogResponse.parseResponse(input);
		return response;
	}
}
