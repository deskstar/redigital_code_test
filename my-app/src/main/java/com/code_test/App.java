//Implement with Apache http client library and AWS SDK
package com.code_test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class App {
    public static void main(String[] args) {
		//HTTP Client object to listen the response
        HttpClient client = new DefaultHttpClient();
		
		//HTTP POST Request for Q3.
        HttpPost getToken = new HttpPost("https://auth.dev.az.nz/oauth2/token");
		
		//HTTP GET Request for Q4. Although the instruction said it should be POST request for No.4, 
		//but I tries several time, server only have response with GET request
		HttpGet getDetail = new HttpGet("https://api.dev.az.nz/v1/upload");
		
        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

			//Parameters encapsulated by Apache objects
            nameValuePairs.add(new BasicNameValuePair("grant_type", "client_credentials"));
            nameValuePairs.add(new BasicNameValuePair("client_id", "45mvsvgqcrfs4f73jlp85paaqq"));
            nameValuePairs.add(new BasicNameValuePair("client_secret","tmbe69ekvku1h6180oj11ugf8b37ovigadfrsro1il65q8596ps"));
            nameValuePairs.add(new BasicNameValuePair("scope", "keeper/document.archive keeper/document.read"));

            getToken.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			getToken.setHeader("Content-type", "application/x-www-form-urlencoded");
			
			//Listen response (the output of question No.3)
            HttpResponse getTokenResponse = client.execute(getToken);
            BufferedReader getTokenReader = new BufferedReader(new InputStreamReader(
                    getTokenResponse.getEntity().getContent()));

			JSONObject returnObject = new JSONObject(getTokenReader.readLine());
			String accessToken = returnObject.getString("access_token");

			//Attach the output from question no.3 to the header for authentication
			getDetail.setHeader("Authorization", "Bearer " + accessToken);
			getDetail.setHeader("Content-type", "application/x-www-form-urlencoded");

            HttpResponse getDetailResponse = client.execute(getDetail);
            BufferedReader getDetailReader = new BufferedReader(new InputStreamReader(
                    getDetailResponse.getEntity().getContent()));			

			String line = "";
			StringBuilder total = new StringBuilder();
            while ((line = getDetailReader.readLine()) != null) {
                 total.append(line);
            }
			
			//becoz the output from no.4 is not a single String, so it requires to cut the carriage return or the JSON will fail to parse it
			String totalStr = total.toString().replaceAll("\\n", "").replaceAll("\\r", "");
			
			returnObject = new JSONObject(totalStr);
			
			//Print the output of question No.4
			System.out.println(totalStr);
			JSONObject credentials = (JSONObject)returnObject.get("Credentials");
			String bucketName = returnObject.getString("BucketName");
			String accessKeyId = credentials.getString("AccessKeyId");
			String sessionToken = credentials.getString("SessionToken");
			String secretAccessKey = credentials.getString("SecretAccessKey");
			String keyPrefix = returnObject.getString("KeyPrefix");

			String clientRegion = "ap-southeast-2";
			String fileObjKeyName = keyPrefix;
			String fileName = "Johnny.txt";

			try {

				//temporary credentials obtained from STS, create a BasicSessionCredentials object
				BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(accessKeyId,secretAccessKey,sessionToken);
				
				AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
						.withRegion(clientRegion)
						.withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
						.build();
				
				//Upload file
				PutObjectRequest request = new PutObjectRequest(bucketName, fileObjKeyName, new File(fileName));
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType("plain/text");
				metadata.addUserMetadata("x-amz-meta-title", "Johnny Upload");
				request.setMetadata(metadata);
				PutObjectResult putObjectResult = s3Client.putObject(request);
				System.out.println(putObjectResult.getMetadata()+"");

				//Want to list the file to check whether the file was upload properly, but response 403			
	/* 			ObjectListing objectListing = s3Client.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix("Jo"));
				for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
						System.out.println(objectSummary.getKey());
				} */	
			}
			catch(AmazonServiceException e) {
				e.printStackTrace();
			}
			catch(SdkClientException e) {
				e.printStackTrace();
			} 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}