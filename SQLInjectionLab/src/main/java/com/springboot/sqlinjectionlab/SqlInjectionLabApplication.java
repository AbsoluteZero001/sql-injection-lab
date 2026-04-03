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

		// 延迟2秒后自动打开浏览器，确保服务器完全启动
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 自动打开浏览器访问前端页面
		openBrowser();
	}

	private static void openBrowser() {
		try {
			System.out.println("===========================================");
			System.out.println("尝试自动打开浏览器...");
			System.out.println("===========================================");

			if (Desktop.isDesktopSupported()) {
				System.out.println("Desktop is supported");
				Desktop desktop = Desktop.getDesktop();
				URI uri = new URI("http://localhost:8080/");
				System.out.println("Opening browser to: " + uri);
				desktop.browse(uri);
				System.out.println("===========================================");
				System.out.println("浏览器已自动打开，请查看");
				System.out.println("===========================================");
			} else {
				// 尝试使用命令行打开浏览器
				System.out.println("===========================================");
				System.out.println("Desktop is not supported, trying command line...");
				String os = System.getProperty("os.name").toLowerCase();
				System.out.println("Operating system: " + os);

				if (os.contains("win")) {
					// Windows系统
					System.out.println("Using Windows start command");
					Runtime.getRuntime().exec("cmd /c start http://localhost:8080/");
					System.out.println("===========================================");
					System.out.println("浏览器已自动打开，请查看");
					System.out.println("===========================================");
				} else if (os.contains("mac")) {
					// macOS系统
					System.out.println("Using macOS open command");
					Runtime.getRuntime().exec("open http://localhost:8080/");
					System.out.println("===========================================");
					System.out.println("浏览器已自动打开，请查看");
					System.out.println("===========================================");
				} else if (os.contains("nix") || os.contains("nux")) {
					// Linux系统
					System.out.println("Using Linux xdg-open command");
					Runtime.getRuntime().exec("xdg-open http://localhost:8080/");
					System.out.println("===========================================");
					System.out.println("浏览器已自动打开，请查看");
					System.out.println("===========================================");
				} else {
					// 其他系统
					System.out.println("===========================================");
					System.out.println("不支持的操作系统");
					System.out.println("请手动访问：http://localhost:8080/");
					System.out.println("===========================================");
				}
			}
		} catch (Exception e) {
			System.out.println("===========================================");
			System.out.println("打开浏览器时出现异常：" + e.getMessage());
			System.out.println("请手动访问：http://localhost:8080/");
			System.out.println("===========================================");
			e.printStackTrace();
		}
	}

}
