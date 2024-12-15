package com.rubal.lld.httpserver.handler;

import java.io.*;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class ConnectionHandler {
    public void handleConnection(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        while(line != null) {
            System.out.println(line);
            reader.readLine();
        }
        final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedWriter.write("HTTP/1.1 " + 200 + " Success ");
        bufferedWriter.close();
        inputStream.close();
    }
}
