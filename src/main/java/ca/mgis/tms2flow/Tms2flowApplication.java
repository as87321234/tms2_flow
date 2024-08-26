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
		
		Validator177f validator177f = ValidationDeserializer.deserialize177f();

		File file = new File("src/main/resources/test1.eft");
		FileInputStream fis = new FileInputStream(file);
		
		AnsiNistDecoder decoder = new AnsiNistDecoder(fis.readAllBytes(), validator177f);

		AnsiNistPacket packet = decoder.getAnsiNistPacket();
		packet.validate();
		
		log.info(String.format("Nist packet %s successfully decoded", file.getCanonicalFile().toString()));
		
	}

}
