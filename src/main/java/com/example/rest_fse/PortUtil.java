package com.example.rest_fse;

import java.net.ServerSocket;

public class PortUtil {
    public static int findAvailablePort(int startPort) {
        int port = startPort;
        while (true) {
            try (ServerSocket socket = new ServerSocket(port)) {
                return port; // Port ist verfügbar
            } catch (Exception e) {
                port++; // Nächsten Port versuchen
            }
        }
    }
}
