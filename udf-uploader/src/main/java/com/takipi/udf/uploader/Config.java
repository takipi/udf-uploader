package com.takipi.udf.uploader;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Strings;
import com.takipi.api.client.ApiClient;
import com.takipi.api.client.request.functions.CreateFunctionRequest;
import com.takipi.api.client.request.functions.CreateGlobalFunctionRequest;
import com.takipi.api.client.result.functions.CreateFunctionResult;
import com.takipi.api.core.request.intf.ApiPostRequest;

public class Config
{
	private static Config instance;
	
	public static void init(String[] args)
	{
		instance = construct(args);
	}
	
	public static Config getInstance()
	{
		return instance;
	}
	
	private final String host;
	private final String username;
	private final String password;
	private final String apiKey;
	private final String serviceId;
	private final boolean global;
	private final byte[] bytes;
	
	Config(String host, String username, String password, String apiKey, String serviceId, boolean global, byte[] bytes)
	{
		this.host = host;
		this.username = username;
		this.password = password;
		this.apiKey = apiKey;
		this.serviceId = serviceId;
		this.global = global;
		this.bytes = bytes;
	}
	
	public ApiClient client()
	{
		ApiClient.Builder builder = ApiClient.newBuilder().setHostname(host);
		
		if (!Strings.isNullOrEmpty(apiKey))
		{
			builder.setApiKey(apiKey);
		}
		else
		{
			builder.setUsername(username).setPassword(password);
		}
		
		return builder.build();
	}
	
	public ApiPostRequest<CreateFunctionResult> functionRequest()
	{
		if (global)
		{
			return CreateGlobalFunctionRequest.newBuilder().setData(bytes).build();
		}
		else
		{
			return CreateFunctionRequest.newBuilder().setServiceId(serviceId).setData(bytes).build();
		}
	}
	
	private static Builder newBuilder()
	{
		return new Builder();
	}
	
	private static Config construct(String[] args)
	{
		Builder builder = newBuilder();
		
		int index = 0;
		
		while (index < args.length)
		{
			String cur = args[index];
			
			if ("-h".equals(cur))
			{
				builder.setHost(args[++index]);
			}
			else if ("-u".equals(cur))
			{
				builder.setUsername(args[++index]).setApiKey(null);
			}
			else if ("-p".equals(cur))
			{
				builder.setPassword(args[++index]).setApiKey(null);
			}
			else if ("-k".equals(cur))
			{
				builder.setApiKey(args[++index]).setUsername(null).setPassword(null);
			}
			else if ("-s".equals(cur))
			{
				builder.setServiceId(args[++index]).setGlobal(false);
			}
			else if ("-g".equals(cur))
			{
				builder.setGlobal(true).setServiceId(null);
			}
			else if ("-f".equals(cur))
			{
				builder.setBytes(readFile(args[++index]));
			}
			
			index++;
		}
		
		return builder.build();
	}
	
	private static byte[] readFile(String filename)
	{
		try
		{
			return FileUtils.readFileToByteArray(new File(filename));
		}
		catch (IOException e)
		{
			throw new IllegalArgumentException("Failed reading file - " + filename, e);
		}
	}
	
	private static class Builder
	{
		private String host;
		private String username;
		private String password;
		private String apiKey;
		private String serviceId;
		private boolean global;
		private byte[] bytes;
		
		Builder()
		{
			
		}
		
		public Builder setHost(String val)
		{
			this.host = val;
			
			return this;
		}
		
		public Builder setUsername(String val)
		{
			this.username = val;
			
			return this;
		}
		
		public Builder setPassword(String val)
		{
			this.password = val;
			
			return this;
		}
		
		public Builder setApiKey(String val)
		{
			this.apiKey = val;
			
			return this;
		}
		
		public Builder setServiceId(String val)
		{
			this.serviceId = val;
			
			return this;
		}
		
		public Builder setGlobal(boolean val)
		{
			this.global = val;
			
			return this;
		}
		
		public Builder setBytes(byte[] val)
		{
			this.bytes = val;
			
			return this;
		}
		
		private void validate()
		{
			if (Strings.isNullOrEmpty(host))
			{
				throw new IllegalArgumentException("Empty hostname");
			}
			
			if (Strings.isNullOrEmpty(apiKey))
			{
				if ((Strings.isNullOrEmpty(username)) ||
					(Strings.isNullOrEmpty(password)))
				{
					throw new IllegalArgumentException("Missing credentials");
				}
			}
			
			if (Strings.isNullOrEmpty(serviceId))
			{
				if (!global)
				{
					throw new IllegalArgumentException("Missing serviceId");
				}
			}
			
			if ((bytes == null) ||
				(bytes.length == 0))
			{
				throw new IllegalArgumentException("Missing bytes");
			}
		}
		
		public Config build()
		{
			validate();
			
			return new Config(host, username, password, apiKey, serviceId, global, bytes);
		}
	}
}
