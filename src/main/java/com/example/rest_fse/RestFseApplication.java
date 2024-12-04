package com.example.rest_fse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestFseApplication {

	public static void main(String[] args) {
		int startPort = 8080;
		int selectedPort = PortUtil.findAvailablePort(startPort);
		System.setProperty("server.port", String.valueOf(selectedPort)); // Dynamische Porteinstellung
		SpringApplication.run(RestFseApplication.class, args);
		System.out.println("Server läuft auf Port: " + selectedPort);
	}

}
