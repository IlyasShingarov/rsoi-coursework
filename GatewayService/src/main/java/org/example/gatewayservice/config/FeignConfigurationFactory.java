package org.example.gatewayservice.config;

import feign.okhttp.OkHttpClient;
import com.fasterxml.jackson.databind.Module;
import org.springframework.cloud.openfeign.support.SortJacksonModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfigurationFactory {
    //    @Bean
//    public Module pageJacksonModule() {
//        return new PageJacksonModule();
//    }
    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }
    @Bean
    public Module sortJacksonModule() {
        return new SortJacksonModule();
    }
}