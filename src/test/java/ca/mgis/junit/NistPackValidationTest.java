package ca.mgis.junit;

import ca.mgis.ansinist2k.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@SpringBootTest(classes = NistPackValidationTest.class)
public class NistPackValidationTest {
	
	Logger log = LoggerFactory.getLogger(NistPackValidationTest.class);
	
	@Test
	public void logger() throws IOException {
		log.trace("A TRACE Message");
		log.debug("A DEBUG Message");
		log.info("An INFO Message");
		log.warn("A WARN Message");
		log.error("An ERROR Message");
	}
	
	
	@Test
	public void loadEFT() throws Exception {
		
		AnsiNistPacket ansiNistPacket = new AnsiNistPacket("src/main/resources/test1.eft", new Validator177f()  );
		ansiNistPacket.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		
		byte[] serializedEft = ansiNistPacket.serialize();
		
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File("/Users/stlouisa/Downloads/test1_serialized.eft"));
			fos.write(serializedEft);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			// Put data in your baos
			baos.writeTo(fos);
			
		} catch(IOException ioe) {
			// Handle exception here
			ioe.printStackTrace();
		} finally {
			fos.close();
		}
		
//		ansiNistPacket.validate();
		
	}
	
	@Test
	public void tag_1_001() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		// Test min and max length
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet("1.001", 1,1,1, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet("1.001", 1,1,1, "0123456789"));

		// Test CharacterSet
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength("1.001", 1,1,1, "9"));
		Assertions.assertTrue(validator.validateFieldLength("1.001", 1,1,1, "99"));
		Assertions.assertTrue(validator.validateFieldLength("1.001", 1,1,1, "999"));
		Assertions.assertFalse(validator.validateFieldLength("1.001", 1,1,1, "9999"));
		
		// Test Min Max Occurrence
		
		packet.deleteAll();
		packet.createItem("300", 1,1,1,1,1);
		Assertions.assertTrue(validator.validateOccurrence( packet, "1.001", 1,1,1, "9"));
		packet.createItem("300", 1,2,1,1,1);
		Assertions.assertFalse(validator.validateOccurrence( packet, "1.001", 1,1,1, "9"));
		
		// Condition Mandatory

		packet.deleteAll();
		packet.createItem("300", 1,1,1,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, "1.001", 1,1,1, "9"));
		packet.createItem("300", 1,2,2,1,1);
		Assertions.assertFalse(validator.validateCondition( packet, "1.001", 1,1,1, "9"));
		
		
	}
	
	@Test
	public void tag_1_002() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		// Test min and max length
		packet.deleteAll();
		packet.createItem("300", 1,1,2,1,1);
		Assertions.assertFalse(validator.validateCharacterSet("1.002", 1,1,1, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet("1.002", 1,1,1, "0123456789"));
		
		// Test CharacterSet
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength("1.001", 1,1,1, "9"));
		Assertions.assertTrue(validator.validateFieldLength("1.001", 1,1,1, "99"));
		Assertions.assertTrue(validator.validateFieldLength("1.001", 1,1,1, "999"));
		Assertions.assertFalse(validator.validateFieldLength("1.001", 1,1,1, "9999"));
		
		// Test Min Max Occurrence
		
		packet.deleteAll();
		packet.createItem("300", 1,1,1,1,1);
		Assertions.assertTrue(validator.validateOccurrence( packet, "1.001", 1,1,1, "9"));
		packet.createItem("300", 1,2,1,1,1);
		Assertions.assertFalse(validator.validateOccurrence( packet, "1.001", 1,1,1, "9"));
		
		// Condition Mandatory
		
		packet.deleteAll();
		packet.createItem("300", 1,1,1,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, "1.001", 1,1,1, "9"));
		packet.createItem("300", 1,2,2,1,1);
		Assertions.assertFalse(validator.validateCondition( packet, "1.001", 1,1,1, "9"));
		
		
	}
	
}
