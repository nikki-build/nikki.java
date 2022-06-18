package com.nikkibuild.websocket.japp.config;

import com.nikkibuild.websocket.japp.util.ServiceDefinition;
import com.nikkibuild.websocket.japp.util.ServiceToken;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Module
public class ServiceConfig {

    @Provides
    @Singleton
    OkHttpClient okHttp() {
        return new OkHttpClient.Builder()
                .pingInterval(2, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    ServiceDefinition serviceDefinition(PropertiesProvider provider) {
        return provider.getServiceDefinition();
    }

    @Provides
    @Singleton
    ServiceToken serviceToken(PropertiesProvider provider) {
        return provider.getServiceToken();
    }

}