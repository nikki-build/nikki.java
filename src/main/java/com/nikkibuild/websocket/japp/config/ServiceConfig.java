package com.nikkibuild.websocket.japp.config;

import com.nikkibuild.websocket.japp.util.ServiceDefinition;
import com.nikkibuild.websocket.japp.util.ServiceToken;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;


public final class ServiceConfig {

    private final PropertiesProvider provider;

    public ServiceConfig(String definitionPath, String tokenPath) {
        provider = new PropertiesProvider(definitionPath, tokenPath);
    }

    public static OkHttpClient okHttp() {
        return new OkHttpClient.Builder()
                .pingInterval(2, TimeUnit.SECONDS)
                .build();
    }

    public ServiceDefinition serviceDefinition() {
        return provider.getServiceDefinition();
    }


    public ServiceToken serviceToken() {
        return provider.getServiceToken();
    }

}