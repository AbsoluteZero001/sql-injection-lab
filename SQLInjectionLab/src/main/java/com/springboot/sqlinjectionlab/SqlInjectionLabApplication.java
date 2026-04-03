package com.springboot.sqlinjectionlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.net.URI;

@SpringBootApplication
public class SqlInjectionLabApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SqlInjectionLabApplication.class);
		app.run(args);
		
		// 自动打开浏览器访问前端页面
		openBrowser();
	}

	private static void openBrowser() {
		try {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				URI uri = new URI("http://localhost:8080/");
				desktop.browse(uri);
			} else {
				System.out.println("===========================================");
				System.out.println("请手动访问：http://localhost:8080/");
				System.out.println("===========================================");
			}
		} catch (Exception e) {
			System.out.println("===========================================");
			System.out.println("请手动访问：http://localhost:8080/");
			System.out.println("===========================================");
			e.printStackTrace();
		}
	}

}
