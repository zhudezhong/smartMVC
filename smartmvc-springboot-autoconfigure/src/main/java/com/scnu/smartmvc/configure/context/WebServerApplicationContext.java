package com.scnu.smartmvc.configure.context;

import org.springframework.boot.web.server.WebServer;

public interface WebServerApplicationContext {

    WebServer getWebServer();
}
