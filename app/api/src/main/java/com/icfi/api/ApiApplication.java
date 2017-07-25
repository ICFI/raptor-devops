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

/**
 * This class is a 'Hello World' REST API application example to show our expertise of Java, Spring Boot, and DevSecOps.
 * 
 */
@Controller
@EnableAutoConfiguration
public class ApiApplication {

	private static Properties props;
	private static final Logger logger = LoggerFactory.getLogger(ApiApplication.class);

	/**
	 * Initializes all properties for config
	 * 
	 */
	@PostConstruct
	public void init() {
		try {
			props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/META-INF/build-info.properties"));
		} catch (IOException e) {
			logger.error("Unable to load build.properties", e);
			props = new Properties();
		}
	}

	/**
	 * Exposes a REST endpoint '/greet' to return a Hello World message to the client.
	 * 
	 * @return Map response body with message info
	 */
	@RequestMapping(value = { "/greet" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, String> greet() {
		Map<String, String> message = new HashMap<String, String>();

		message.put("greeting", "Hello World");

		return message;
	}

	/**
	 * Exposes a REST endpoint '/' to return applicaiton build info to the client.
	 * 
	 * @return Map response body with message info
	 */
	@RequestMapping(value = { "/", "/version" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, String> version() {
		Map<String, String> message = new HashMap<String, String>();

		message.put("name", props.getProperty("build.artifact"));
		message.put("version", props.getProperty("build.version"));
		message.put("branch", props.getProperty("build.branch"));
		message.put("commit", props.getProperty("build.commit"));
		message.put("timestamp", props.getProperty("build.time"));

		return message;
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}