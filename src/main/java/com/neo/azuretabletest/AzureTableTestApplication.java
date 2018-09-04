package com.neo.azuretabletest;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.neo.azuretabletest.entity.PeopleEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@SpringBootApplication
@Configuration
public class AzureTableTestApplication {

	private static final String storageConnectionString =
			"DefaultEndpointsProtocol=http;" +
					"AccountName=neoteststorage;" +
					"AccountKey=Mzr0EYO+uOmKUAecHLziS7yzLSbEQYzAA0HsURTQebkYSOAmDdjoyROMFBx8AbSsTg443bNec44+qEuSpJATQg==";

	public static void main(String[] args) {
		SpringApplication.run(AzureTableTestApplication.class, args);
	}


}
