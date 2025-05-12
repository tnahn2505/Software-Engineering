package com.sagroup.teacherservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import com.sagroup.teacherservice.config.ConfigFileExternalizationConfig;



@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition
public class TeacherServiceApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeacherServiceApplication.class);

	public static void main(String[] args) {
		if (ConfigFileExternalizationConfig.createConfigFiles()
				!= ConfigFileExternalizationConfig.CONFIG_FILES_ERROR_STATE) {
			ConfigurableApplicationContext app = new SpringApplicationBuilder(
					TeacherServiceApplication.class)
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

//@Bean
//	CommandLineRunner seedData(TeacherRepository teacherRepository) {
//		return args -> {
//			teacherRepository.save(new Teacher(
//					null,
//					"John",
//					"Doe",
//					new Contact("john.doe@example.com", "555-555-5555"),
//					new Address("California", "Los Angeles", "90001", "USA"),
//					new School("Springfield Elementary"),
//					new TeachingClass("1", "A")
//			));
//
//			teacherRepository.save(new Teacher(
//					null,
//					"Jane",
//					"Smith",
//					new Contact("jane.smith@example.com", "555-555-5556"),
//					new Address("Texas", "Dallas", "75201", "USA"),
//					new School("Greenwood High"),
//					new TeachingClass("2", "B")
//			));
//
//			teacherRepository.findAll().forEach(System.out::println);
//		};
//	}

//	@Bean
//	public CommandLineRunner run(TeacherRepository teacherRepository) {
//		return args -> {
//			//repositoryRestConfiguration.exposeIdsFor(Teacher.class);
//			teacherRepository.save(new Teacher("22", "John", "Doe", new Contact("john.doe@example.com", "555-555-5555"),new School( "Springfield Elementary"), new TeachingClass("1", "A"),  new Address("123 Main St", "Anytown", "USA", "12345")));
//			teacherRepository.save(new Teacher("22", "Jane", "Doe", new Contact("jane.doe@example.com", "555-555-5556"),new School( "Springfield Elementary"), new TeachingClass("2", "B"), new Address("123 Main St", "Anytown", "USA", "12345")));
//			teacherRepository.save(new Teacher("24","Jim", "Smith", new Contact("jim.smith@example.com", "555-555-5557"),new School( "Evergreen Elementary"), new TeachingClass("3", "C"), new Address("323 vergin St", "BigCity", "USA", "1324")));
//			teacherRepository.findAll().forEach(System.out::println);
//		};
//	}

//	@Bean
//	public RepositoryRestConfiguration repositoryRestConfiguration() {
//		return repositoryRestConfiguration;
//	}


}

//	@Bean
//	CommandLineRunner start(TeacherRepository teacherRepository, RepositoryRestConfiguration repositoryRestConfiguration){
//			return args -> {
//				repositoryRestConfiguration.exposeIdsFor(Compte.class);
//				teacherRepository.save(new Teacher(null, ...));
//				teacherRepository.save(new Teacher(null, ...));
//				teacherRepository.save(new Teacher(null, ...));
//				teacherRepository.findAll().forEach(cp -> {
//					System.out.println(cp);
//				});
//			};
//	}
//
//	@Bean
//	public CommandLineRunner start(TeacherRepository teacherRepository, RepositoryRestConfiguration repositoryRestConfiguration) {
//		return args -> {
//			repositoryRestConfiguration.exposeIdsFor(Teacher.class);
//
//			teacherRepository.save(new Teacher(null, "John Doe", "Mathematics", "", "", ""));
//			teacherRepository.save(new Teacher(null, "Jane Doe", "Physics"));
//			teacherRepository.save(new Teacher(null, "James Bond", "Chemistry"));
//
//			List<Teacher> allTeachers = teacherRepository.findAll();
//			allTeachers.forEach(teacher -> {
//				System.out.println(teacher.getFirstName() + " teaches " + teacher.);
//			});
//		};
//	}
//
//
//}
