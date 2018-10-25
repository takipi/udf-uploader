package com.takipi.udf.uploader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.takipi.api.client.ApiClient;
import com.takipi.api.client.data.functions.UserFunction;
import com.takipi.api.client.data.functions.UserLibrary;
import com.takipi.api.client.result.functions.CreateFunctionResult;
import com.takipi.api.core.request.intf.ApiPostRequest;
import com.takipi.api.core.url.UrlClient.Response;

public class Uploader
{
	protected static final Logger logger = LoggerFactory.getLogger(Uploader.class);
	
	public static void main(String[] args)
	{
		try
		{
			Config.init(args);
			
			ApiClient client = Config.getInstance().client();
			ApiPostRequest<CreateFunctionResult> functionRequest = Config.getInstance().functionRequest();
			
			Response<CreateFunctionResult> response = client.post(functionRequest);
			
			if ((response.isBadResponse()) ||
				(response.data == null) ||
				(response.data.library == null))
			{
				logger.error("Failed creating library - code: {}.", response.responseCode);
				return;
			}
			
			UserLibrary library = response.data.library;
			
			logger.info("Library creation success.");
			logger.info("");
			logger.info("Library id {}", library.id);
			logger.info(" |");
			logger.info(" +-- name - {}", library.name);
			logger.info(" |");
			logger.info(" +-- version - {}", library.version);
			logger.info(" |");
			logger.info(" +-- scope - {}", library.scope);
			logger.info(" |");
			logger.info(" +-- key - {}", library.key);
			logger.info(" |");
			logger.info(" +-- functions:");
			
			for (UserFunction function : library.functions)
			{
				logger.info("         |");
				logger.info("         +-- {}", function.functionId);
			}
		}
		catch (Exception e)
		{
			logger.error("Something bad happended.", e);
			printUsage();
		}
	}
	
	private static void printUsage()
	{
		System.out.println("Usage: java -jar <jar_name> [-h hostname] [-u username] [-p password] [-k api key] [-s service id] [-g] [-f filename]");
	}
}
