package com.sagroup.teacherservice.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;



@Slf4j
@Configuration
public class ConfigFileExternalizationConfig {
    public static final int CONFIG_FILES_EXISTED_STATE = 0;
    public static final int CONFIG_FILES_CREATED_STATE = 1;
    public static final int CONFIG_FILES_ERROR_STATE = 2;

//    private static final String DOSSIER_CONFIG = ".swa-project-teacher" + File.separator + "conf";
    private static final String DOSSIER_CONFIG = ".swa-project-teachers" + File.separator + "conf";
    public ConfigFileExternalizationConfig() {

    }

    public static String getExternalConfigFolder() {
        return System.getProperty("user.home")
                + File.separator + DOSSIER_CONFIG;
    }

    public static Map<String, Object> getPropertiesConfig() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("spring.config.location", "classpath:/,file:${user.home}/" + DOSSIER_CONFIG + "/");
        return properties;
    }

    public static String[] enhanceArgs(String[] args) {
        final String location = "--spring.config.location=classpath:/," + "file:${user.home}/" + DOSSIER_CONFIG + "/";
        if (args.length == 0) {
            return new String[]{location};
        }
        if (!String.join("", args).contains("spring.config.location")) {
            String[] result = Arrays.copyOf(args, args.length + 1);
            result[result.length - 1] = location;
            return result;
        }
        return args;
    }

    public static int createConfigFiles() {
        File externalConfigFileFolder = new File(getExternalConfigFolder());
        if (externalConfigFileFolder.exists()) {
            String[] list = externalConfigFileFolder.list();
            if (list.length != 0) {
                log.info("Configuration files have been found");
                return CONFIG_FILES_EXISTED_STATE;
            }
        }
        if (!externalConfigFileFolder.exists() && !externalConfigFileFolder.mkdirs()) {
            log.error("An error occurred while creating configuration folder");
            return CONFIG_FILES_ERROR_STATE;
        }

        List<ConfigFile> configFilesToCopy = getConfigFilesToCopy();
        configFilesToCopy.forEach(configFile -> {
            try {
                Path destination = Paths.get(new File(getExternalConfigFolder(), configFile.getFilename()).toURI());
                Files.copy(configFile.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                log.info("{} has been created", destination.toString());
            } catch (IOException e) {
                log.error("Config file {} cannot been created. Reason : {}", configFile.getFilename(), e.getMessage());
            }
        });
        return CONFIG_FILES_CREATED_STATE;
    }

    private static List<ConfigFile> getConfigFilesToCopy() {
        List<ConfigFile> files = new ArrayList<>();
        try {
            files.add(new ConfigFile("application.yml", new ClassPathResource("application.yml").getInputStream()));
            //files.add(new ConfigFile("bootstrap.yml", new ClassPathResource("bootstrap.yml").getInputStream()));
            //files.add(new ConfigFile("logback-spring.xml", new ClassPathResource("logback-spring.xml").getInputStream()));
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
        return files;
    }

    @Data
    @AllArgsConstructor
    public static class ConfigFile {
        private String filename;
        private InputStream inputStream;
    }
}
