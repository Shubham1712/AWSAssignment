package com.test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

public class LambdaFunction implements RequestHandler<SQSEvent, Void> {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(LambdaFunction.class);
	
	@Autowired
	private AmazonDynamoDB dbClient;
	
	@Autowired
	Config config;
	
	public LambdaFunction() {
		config = new Config();
		dbClient = config.getBuilder();
	}
	
	@Override
	public Void handleRequest(SQSEvent event, Context context) {
		
		for (SQSMessage record : event.getRecords()) {

			String msgId = record.getMessageId();
			String msg = record.getBody();

			LOGGER.debug("Received message with Message Id - {} and Message Body - {}", msgId, msg);
			
			msg = possibleCombinations(msg.replaceAll("[,;\\s]", ""));
			Map<String, AttributeValue> item = newItem(msgId, msg);


			PutItemRequest putItemRequest = new PutItemRequest(config.getTableName(), item);
			PutItemResult putItemResult = dbClient.putItem(putItemRequest);

			LOGGER.debug("Output Message - {}", msg);
			LOGGER.debug("Status code - {}", putItemResult.getSdkHttpMetadata().getHttpStatusCode());

		}
		return null;
	}

	private String possibleCombinations(String msg) {

		int mask = 1;
		int length = msg.length();
		List<String> output = new ArrayList<String>();
		while (mask < 1 << length) {
			String interm = "";
			for (int i = 0; i < length; i++) {
				if (((mask >> i) & 1) != 0) {
					interm += msg.charAt(i);
				}
			}
			output.add(interm);
			mask++;
		}
		return output.toString();
	}

	private Map<String, AttributeValue> newItem(String msgId, String msg) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("msgId", new AttributeValue(msgId));
		item.put("message", new AttributeValue(msg));
		item.put("timestamp", new AttributeValue(Instant.now().toString()));

		return item;
	}

}
