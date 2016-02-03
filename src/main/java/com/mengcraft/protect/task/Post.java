package com.mengcraft.protect.task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created on 16-2-3.
 */
public class Post implements Runnable {

    private final byte[] host;

    public Post(byte[] host) {
        this.host = host;
    }

    @Override
    public void run() {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("metric.mengcraft.com", 10146), 0xF);
            OutputStream output = socket.getOutputStream();
            for (byte b : host) {
                output.write(b);
            }
            output.flush();
        } catch (IOException e) {
            //
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                //
            }
        }
    }

}
