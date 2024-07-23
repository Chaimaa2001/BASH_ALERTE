package com.adria.spring_oracle.config;

import com.infobip.ApiClient;
import com.infobip.ApiKey;
import com.infobip.BaseUrl;
import com.infobip.api.SmsApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfobipConfig {

    @Value("${infobip.api.key}")
    private String apiKey;

    @Value("${infobip.api.url}")
    private String apiUrl;

    @Bean
    public SmsApi smsApi() {
        ApiClient apiClient = ApiClient.forApiKey(ApiKey.from(apiKey))
                .withBaseUrl(BaseUrl.from(apiUrl))
                .build();
        return new SmsApi(apiClient);
    }
}
