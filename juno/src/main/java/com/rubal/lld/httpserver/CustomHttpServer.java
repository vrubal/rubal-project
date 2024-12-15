package com.rubal.lld.httpserver;

import com.rubal.lld.httpserver.handler.ConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class CustomHttpServer {
    final private int port;
    final private int threads;
    final private Executor executor;
    final private ServerSocket serverSocket;
    final private ConnectionHandler connectionHandler;

    static CustomHttpServer server;
    public CustomHttpServer(int port, int threads) throws IOException {
        this.port = port;
        this.threads = threads;
        this.executor = Executors.newFixedThreadPool(this.threads);
        this.serverSocket = new ServerSocket(port);
        this.connectionHandler = new ConnectionHandler();
    }

    static CustomHttpServer build(final int port, final int threads) throws IOException {
        if(server==null) {
            synchronized (CustomHttpServer.class) {
                if (server == null) {
                    server = new CustomHttpServer(port, threads);
                }
            }
        }
        return server;
    }

    public void start(){
        while(true){
            try {
                Socket connection = this.serverSocket.accept();
                handleConnection(connection);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleConnection(Socket clientConnection) {
        Runnable httpRequestRunner = () -> {
            try {
                connectionHandler.handleConnection(clientConnection.getInputStream(), clientConnection.getOutputStream());
            } catch (IOException ignored) {
            }
        };
        executor.execute(httpRequestRunner);
    }
}
