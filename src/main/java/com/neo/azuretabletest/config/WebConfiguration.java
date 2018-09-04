package com.neo.azuretabletest.config;

import com.microsoft.azure.storage.CloudStorageAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Configuration
public class WebConfiguration {

    private static final String storageConnectionString =
            "";

    @Bean
    public CloudStorageAccount cloudStorageAccount() {

        try {
            return CloudStorageAccount.parse(storageConnectionString);
        } catch (URISyntaxException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }


}
