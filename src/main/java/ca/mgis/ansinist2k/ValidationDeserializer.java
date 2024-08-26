package ca.mgis.ansinist2k;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ValidationDeserializer {
	
	private static Logger log = LoggerFactory.getLogger(ValidationDeserializer.class);
	
	public static Validator177f deserialize() throws IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		File f = new File("/Users/stlouisa/IdeaProjects/tms2_flow/src/main/resources/validation/validation-1_7_7F.json");
		
		ValidationDeserializer.log.info(String.format("loading validation file: %s ", f.getCanonicalFile()));
		
		return mapper.readValue(f, Validator177f.class);
		
	}
	
}
