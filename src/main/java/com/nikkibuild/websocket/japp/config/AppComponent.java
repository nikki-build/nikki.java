package com.nikkibuild.websocket.japp.config;

import com.nikkibuild.websocket.japp.App;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = {ServiceConfig.class})
@Singleton
public interface AppComponent {
    App app();
}