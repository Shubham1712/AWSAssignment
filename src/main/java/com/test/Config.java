package com.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

@Configuration
public class Config {
	
	@Value("${cloud.aws.credentials.access-key}")
	private String amazonAWSAccessKey = "abc";
	
	@Value("${cloud.aws.credentials.secret-key}")
	private String amazonAWSSecretKey = "xyz";
	
	@Value("${aws_dynamodb_table}")
	String tableName= "AssignDBTable";

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public AmazonDynamoDB getBuilder() {

		return  AmazonDynamoDBClientBuilder.standard()
	  			.withRegion(Regions.US_EAST_1)
	  			.withCredentials(new  AWSStaticCredentialsProvider(new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey))) 
	  			.build();
	}
}
