package com.icfi.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Controller
@EnableAutoConfiguration
public class ApiApplication {

    private static Properties props;
    private static final Logger logger = LoggerFactory.getLogger(ApiApplication.class);

    @PostConstruct
    public void init() {
        try {
            props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/META-INF/build-info.properties"));
        } catch (IOException e) {
            logger.error("Unable to load build.properties");
            props = new Properties();
        }
    }

    @RequestMapping(value = {"/greet"}, method = RequestMethod.GET)
    public @ResponseBody
    Map greet() {
        return new HashMap<String, String>() {{
            put("greeting", "Hello World");
        }};
    }

    @RequestMapping(value = {"/", "/version"}, method = RequestMethod.GET)
    public @ResponseBody
    Map version() {
        return new HashMap<String, String>() {{
            put("name", props.getProperty("build.artifact"));
            put("version", props.getProperty("build.version"));
            put("branch", props.getProperty("build.branch"));
            put("commit", props.getProperty("build.commit"));
            put("timestamp", props.getProperty("build.time"));
        }};
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}