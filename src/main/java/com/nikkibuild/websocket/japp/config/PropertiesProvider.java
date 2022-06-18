package com.nikkibuild.websocket.japp.config;

import com.google.gson.Gson;
import com.nikkibuild.websocket.japp.util.ServiceDefinition;
import com.nikkibuild.websocket.japp.util.ServiceToken;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@Singleton
public class PropertiesProvider {
    private final ServiceDefinition serviceDefinition;
    private final ServiceToken      serviceToken;

    @Inject
    PropertiesProvider() {
        try {
            var stream = new FileInputStream("./serviceDef.json");
            var reader = new InputStreamReader(stream);
            serviceDefinition = new Gson().fromJson(reader, ServiceDefinition.class);
            stream            = new FileInputStream("./serviceToken.json");
            reader            = new InputStreamReader(stream);
            serviceToken      = new Gson().fromJson(reader, ServiceToken.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public ServiceDefinition getServiceDefinition() {
        return serviceDefinition;
    }

    public ServiceToken getServiceToken() {
        return serviceToken;
    }
}