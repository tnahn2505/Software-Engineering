package com.sagroup.userservice;

import com.sagroup.userservice.entity.NewAppUser;
import com.sagroup.userservice.repository.NewAppUserRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.sagroup.userservice.config.ConfigFileExternalizationConfig;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.sagroup.userservice.repository")
@EnableDiscoveryClient
@OpenAPIDefinition
public class UserServiceApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceApplication.class);

	public static void main(String[] args) {

		if (ConfigFileExternalizationConfig.createConfigFiles()
				!= ConfigFileExternalizationConfig.CONFIG_FILES_ERROR_STATE) {
			ConfigurableApplicationContext app = new SpringApplicationBuilder(
				UserServiceApplication.class)
					.build().run(ConfigFileExternalizationConfig.enhanceArgs(args));
			Environment env = app.getEnvironment();
			String protocol = "http";
			if (env.getProperty("server.ssl.key-store") != null) {
				protocol = "https";
			}
			LOGGER.info("\n----------------------------------------------------------\n\t"
							+ "Application '{}' is running! Access URLs:\n\t"
							+ "Local: \t\t{}://localhost:{}\n\t"
							+ "Profile(s): \t{}\n----------------------------------------------------------",
					env.getProperty("spring.application.name"),
					protocol,
					env.getProperty("server.port"),
					env.getActiveProfiles());
		} else {
			LOGGER.error("Please proceed to manually creation of configuration files!");
		}
	}
	@Bean
	CommandLineRunner createAdminUser(NewAppUserRepository userRepository) {
		return args -> {
			if (userRepository.findByUsernameIgnoreCase("admin") == null) {
				NewAppUser admin = new NewAppUser();
				admin.setUsername("admin");
				admin.setPassword("123456"); // Có thể dùng BCrypt nếu muốn
				admin.setRole("ADMIN");
				userRepository.save(admin);
				LOGGER.info("✅ Admin user created successfully.");
			} else {
				LOGGER.info("ℹ️ Admin user already exists.");
			}
		};
	}
}
