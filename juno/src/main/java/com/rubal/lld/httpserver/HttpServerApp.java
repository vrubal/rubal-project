package com.rubal.lld.httpserver;

import java.io.IOException;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class HttpServerApp {
    public static void main(String[] args) throws IOException {
        CustomHttpServer server = CustomHttpServer.build(8080, 10);
        server.start();

    }
}
