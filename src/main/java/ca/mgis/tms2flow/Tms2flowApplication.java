package ca.mgis.tms2flow;

import ca.mgis.ansinist2k.*;
import ca.mgis.ansinist2k.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;

@SpringBootApplication
public class Tms2flowApplication {
	
	final static Logger log = LoggerFactory.getLogger(Tms2flowApplication.class);
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Tms2flowApplication.class, args);
		
		log.info("Application {} started.", Tms2flowApplication.class.getSimpleName());
		
		AnsiNistPacket packet = new AnsiNistPacket("src/main/resources/test1.eft", new Validator177f()  );
		
		packet.validate();
		
	}

}
