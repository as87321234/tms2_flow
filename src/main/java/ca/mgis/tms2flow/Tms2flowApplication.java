package ca.mgis.tms2flow;

import ca.mgis.tms2flow.config.AppEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Tms2flowApplication {
	
	final static Logger log = LoggerFactory.getLogger(Tms2flowApplication.class);
	
	public static AppEnvironment env;
	
	
	public static void main(String[] args) {
		SpringApplication.run(Tms2flowApplication.class, args);
		
		log.info("Application started {}", Tms2flowApplication.env);
		
	}
	
	@Autowired
	public AppEnvironment loadEnvironment(AppEnvironment env) {
		
		Tms2flowApplication.env = env;
		
		return env;
		
	}
	
}
