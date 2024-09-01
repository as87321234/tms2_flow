package ca.mgis.ansinist2k;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ValidationDeserializerImpl extends  ValidationDeserializer {
	
	private static Logger log = LoggerFactory.getLogger(ValidationDeserializerImpl.class);
	
	public  AnsiNistValidator deserialize( String filename ) {
		
		try {

			ObjectMapper mapper = new ObjectMapper();
			File f = new File(filename );

			log.info(String.format("loading validation file: %s ", f.getCanonicalFile()));

			return mapper.readValue(f, Validator177f.class);
			
		} catch (IOException e) {
			
			log.error(String.valueOf(e));
		
		}
		
		return null;
	}
	
}
