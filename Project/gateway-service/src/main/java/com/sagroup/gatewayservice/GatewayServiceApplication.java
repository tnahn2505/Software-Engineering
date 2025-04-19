package com.sagroup.gatewayservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import com.sagroup.gatewayservice.config.ConfigFileExternalizationConfig;

import java.util.Arrays;

@SpringBootApplication
@EnableDiscoveryClient

public class GatewayServiceApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayServiceApplication.class);

	public static void main(String[] args) {
		// Chỉ chạy kiểm tra config nếu KHÔNG phải môi trường test
		if (!isTestEnvironment()) {
			if (ConfigFileExternalizationConfig.createConfigFiles()
					!= ConfigFileExternalizationConfig.CONFIG_FILES_ERROR_STATE) {

				String[] enhancedArgs = ConfigFileExternalizationConfig.enhanceArgs(args);

				ConfigurableApplicationContext app = new SpringApplicationBuilder(GatewayServiceApplication.class)
						.build().run(enhancedArgs);

				Environment env = app.getEnvironment();
				String protocol = env.getProperty("server.ssl.key-store") != null ? "https" : "http";

				LOGGER.info("\n----------------------------------------------------------\n\t"
								+ "Application '{}' is running! Access URLs:\n\t"
								+ "Local: \t\t{}://localhost:{}\n\t"
								+ "Profile(s): \t{}\n----------------------------------------------------------",
						env.getProperty("spring.application.name"),
						protocol,
						env.getProperty("server.port"),
						Arrays.toString(env.getActiveProfiles()));
			} else {
				LOGGER.error("Please proceed to manually creation of configuration files!");
			}
		} else {
			SpringApplication.run(GatewayServiceApplication.class, args);
		}
	}

	private static boolean isTestEnvironment() {
		String env = System.getProperty("java.class.path");
		return env != null && env.contains("surefire");
	}



}
