package ca.mgis.junit;

import ca.mgis.ansinist2k.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

@SpringBootTest(classes = ca.mgis.junit.NistPackValidationTest.class)
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
	
	@Test void deserialize1_7_7fJson() {
		
		AnsiNistPacket ansiNistPacket = new AnsiNistPacket();
		ansiNistPacket.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		
		Assertions.assertNotNull(ansiNistPacket.getAnsiNistValidator());
		
	}
	
	@Test
	public void loadEFT() throws Exception {
		
		byte[] orginal = new FileInputStream("src/main/resources/test1.eft").readAllBytes();
		AnsiNistPacket ansiNistPacket = new AnsiNistPacket("src/main/resources/test1.eft", new Validator177f()  );
		ansiNistPacket.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		
		byte[] serializedEft = ansiNistPacket.serialize();
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File("src/main/resources/test1_serialized.eft"));
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
		
		Assertions.assertArrayEquals(orginal, serializedEft);
		
//		ansiNistPacket.validate();
		
	}
	
	@Test
	public void tag_1_001() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.001";
		int recordType = 1;
		int fieldIdKey = 1;
		int subfieldIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getAlphaCharacterSet()));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getNumericCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpecialCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getCrlfCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getHyphenCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getPeriodCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpaceCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getApostropheCharacterSet()));

		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "99"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "9999"));
		
	}
	
	@Test
	public void tag_1_002() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.002";
		int recordType = 1;
		int fieldIdKey = 2;
		int subfieldIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getAlphaCharacterSet()));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getNumericCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpecialCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getCrlfCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getHyphenCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getPeriodCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpaceCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getApostropheCharacterSet()));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		packet.createItem("300", recordType, 1, fieldIdKey, subfieldIdKey, 1);
		
	}
	
	@Test
	public void tag_1_003() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.003";
		int recordType = 1;
		int fieldIdKey = 3;
		int subfieldIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getAlphaCharacterSet()));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getNumericCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpecialCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getCrlfCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getHyphenCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getPeriodCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpaceCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getApostropheCharacterSet()));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("1", recordType, 1, fieldIdKey, subfieldIdKey, 1);
		packet.createItem("5", recordType, 1, fieldIdKey, subfieldIdKey, 1 + 1);
		
		packet.createItem("2", recordType, 1, fieldIdKey, subfieldIdKey+1, 1);
		packet.createItem("00", recordType, 1, fieldIdKey, subfieldIdKey+1, 1 + 1);
		
		packet.createItem("10", recordType, 1, fieldIdKey, subfieldIdKey+2, 1);
		packet.createItem("01", recordType, 1, fieldIdKey, subfieldIdKey+2, 1 + 1);
		
		packet.createItem("14", recordType, 1, fieldIdKey, subfieldIdKey+3, 1);
		packet.createItem("02", recordType, 1, fieldIdKey, subfieldIdKey+3, 1 + 1);
		
		packet.createItem("14", recordType, 1, fieldIdKey, subfieldIdKey+4, 1);
		packet.createItem("03", recordType, 1, fieldIdKey, subfieldIdKey+4, 1 + 1);
		
		packet.createItem("14", recordType, 1, fieldIdKey, subfieldIdKey+5, 1);
		packet.createItem("04", recordType, 1, fieldIdKey, subfieldIdKey+5, 1 + 2);
		
		packet.createItem("00", recordType, 1, fieldIdKey, subfieldIdKey, 1);
		packet.createItem("00", recordType, 1, fieldIdKey, subfieldIdKey, 1 + 1);
		
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		Assertions.assertTrue(false);
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(false);
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, 1, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, 1, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, 1, "99"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, 1, "999"));
		
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, 1, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, 1, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, 1, "99"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, 1, "999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, 1, "9999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, 1, "99999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, 1, "999999"));
		
	}
	
	@Test
	public void tag_1_004() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.004";
		int recordType = 1;
		int fieldIdKey = 4;
		int subfieldIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getAlphaCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getNumericCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpecialCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getCrlfCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getHyphenCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getPeriodCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpaceCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getApostropheCharacterSet()));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,1, "AA"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,1, "AAA"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,1, "AAAA"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,1, "AAAAA"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,1, "AAAAAA"));
		
	}
	
	@Test
	public void tag_1_005() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.005";
		int recordType = 1;
		int fieldIdKey = 5;
		int subfieldIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getAlphaCharacterSet()));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getNumericCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpecialCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getCrlfCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getHyphenCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getPeriodCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpaceCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getApostropheCharacterSet()));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "20240101"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "202401012"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "202401013"));
		
	}
	
	@Test
	public void tag_1_006() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.006";
		int recordType = 1;
		int fieldIdKey = 6;
		int subfieldIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getAlphaCharacterSet()));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getNumericCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpecialCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getCrlfCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getHyphenCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getPeriodCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpaceCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getApostropheCharacterSet()));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "0"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "11"));
		
	}
	
	@Test
	public void tag_1_007() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.007";
		int recordType = 1;
		int fieldIdKey = 7;
		int subfieldIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getAlphaCharacterSet()));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getNumericCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpecialCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getCrlfCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getHyphenCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getPeriodCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpaceCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getApostropheCharacterSet()));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "0000000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "ON00000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "00000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "ON000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "ON0000"));
		
	}
	
	@Test
	public void tag_1_008() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.008";
		int recordType = 1;
		int fieldIdKey = 8;
		int subfieldIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getAlphaCharacterSet()));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getNumericCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpecialCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getCrlfCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getHyphenCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getPeriodCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpaceCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getApostropheCharacterSet()));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "0000000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "ON00000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "00000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "ON000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "ON0000"));
		
	}
	
	@Test
	public void tag_1_009() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.009";
		int recordType = 1;
		int fieldIdKey = 9;
		int subfieldIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getAlphaCharacterSet()));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getNumericCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpecialCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getCrlfCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getHyphenCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getPeriodCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpaceCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getApostropheCharacterSet()));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "0000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "00000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "000000000000"));
		
	}
	
	@Test
	public void tag_1_010() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.010";
		int recordType = 1;
		int fieldIdKey = 10;
		int subfieldIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getAlphaCharacterSet()));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getNumericCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpecialCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getCrlfCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getHyphenCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getPeriodCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpaceCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getApostropheCharacterSet()));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", 1, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "0000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "00000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "000000000000"));
		
	}
	
	@Test
	public void tag_1_011() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.011";
		int recordType = 1;
		int fieldIdKey = 11;
		int subfieldIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getAlphaCharacterSet()));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getNumericCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpecialCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getCrlfCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getHyphenCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getPeriodCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpaceCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getApostropheCharacterSet()));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "00000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "00.00"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "0000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "00.0"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "00.000"));
		
	}
	
	@Test
	public void tag_1_012() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.012";
		int recordType = 1;
		int fieldIdKey = 12;
		int subfieldIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getAlphaCharacterSet()));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getNumericCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpecialCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getCrlfCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getHyphenCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getPeriodCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpaceCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getApostropheCharacterSet()));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "00000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "00.00"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "0000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "00.0"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "00.000"));
		
	}
	
	@Test
	public void tag_1_014() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.014";
		int recordType = 1;
		int fieldIdKey = 14;
		int subfieldIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getAlphaCharacterSet()));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getNumericCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpecialCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getCrlfCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getHyphenCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getPeriodCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getSpaceCharacterSet()));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, 1, validator.getApostropheCharacterSet()));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 2, fieldIdKey, subfieldIdKey, 1);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, 1, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "20200325173337Z"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "202003251733370Z"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, 1, "2020032517333Z"));
		
	}
	
	@Test
	public void tag_2_001() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.001";
		int fieldIdKey = 1;
		int itemIdKey = 1;
		
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.createItem("300", 2, 2, fieldIdKey, 1, itemIdKey);
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, 1, 1, itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, 1, 1, itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, 1, 1, itemIdKey, "9"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "99"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "9999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "99999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "999999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "9999999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "99999999"));
		
	}
	
	@Test
	public void tag_2_002() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.002";
		int fieldIdKey = 2;
		int itemIdKey = 1;
		
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "99"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "9999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "99999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "999999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "9999999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "99999999"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.createItem("300", 2, 2, fieldIdKey, 1, itemIdKey);
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.createItem("300", 2, 2, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, 1, 1, itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_067_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.067";
		int fieldIdKey = 67;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWX"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXY"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.createItem("300", 2, 2, fieldIdKey, 1, itemIdKey);
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.createItem("300", 2, 2, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, 1, 1, itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_067_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.067";
		int fieldIdKey = 67;
		int itemIdKey = 2;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWX"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXY"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.createItem("300", 2, 2, fieldIdKey, 1, itemIdKey);
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.createItem("300", 2, 2, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, 1, 1, itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_067_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.067";
		int fieldIdKey = 67;
		int itemIdKey = 3;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWX"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXZ"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXZZ"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.createItem("300", 2, 2, fieldIdKey, 1, itemIdKey);
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.createItem("300", 2, 2, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, 1, 1, itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_800() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.800";
		int fieldIdKey = 800;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "0123456789012345678"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "012345678901234567899"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "01234567890123456789"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.createItem("300", 2, 2, fieldIdKey, 1, itemIdKey);
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.createItem("300", 2, 2, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, 1, 1, itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_801() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.801";
		int fieldIdKey = 801;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ON00000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ON000000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ON0000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ON000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ON00"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "O00"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "00"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "0"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	
	@Test
	public void tag_2_802_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.802";
		int fieldIdKey = 802;
		int itemIdKey = 1;
		
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "A"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTQUVWXABCDEFGHIJKLMNOPQRSTQUVWX"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTQUVWXABCDEFGHIJKLMNOPQRSTQUVWXZ"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_802_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.802";
		int fieldIdKey = 802;
		int itemIdKey = 2;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "A"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTQUVWXABCDEFGHIJ"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTQUVWXABCDEFGHIJK"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	
	@Test
	public void tag_2_802_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.802";
		int fieldIdKey = 802;
		int subfieldIdKey = 3;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,subfieldIdKey,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,subfieldIdKey,1, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,subfieldIdKey,1, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,subfieldIdKey,1, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,subfieldIdKey,1, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,1, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,1, "0"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,1, "01"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,1, "001"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,subfieldIdKey,1);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,subfieldIdKey,1, "9"));
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,subfieldIdKey,1, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,subfieldIdKey,1, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,subfieldIdKey,1);
		packet.createItem("300", 2,1,fieldIdKey,subfieldIdKey,1);
		packet.createItem("300", 2,1,fieldIdKey,subfieldIdKey,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,subfieldIdKey,1, "9"));
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,subfieldIdKey,1, "9"));
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,subfieldIdKey,1, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,subfieldIdKey,1);
		packet.createItem("300", 2,1,fieldIdKey,subfieldIdKey,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, subfieldIdKey,1,1, "9"));
		
	}
	
	@Test
	public void tag_2_803() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.803";
		int fieldIdKey = 803;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123ABCDEFGHIJKLMNOPQRSTUVWXYZ012"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123ABCDEFGHIJKLMNOPQRSTUVWXYZ0123"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123ABCDEFGHIJKLMNOPQRSTUVWXYZ01234"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_804() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.804";
		int fieldIdKey = 804;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ01234"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ012345"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	
	@Test
	public void tag_2_806_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.806";
		int fieldIdKey = 806;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "’"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVW"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWX"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXY"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_806_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.806";
		int fieldIdKey = 806;
		int itemIdKey = 2;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "’"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOP"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_806_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.806";
		int fieldIdKey = 806;
		int itemIdKey = 3;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "’"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOP"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_806_4() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.806";
		int fieldIdKey = 806;
		int itemIdKey = 4;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "’"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOP"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_806_5() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.806";
		int fieldIdKey = 806;
		int itemIdKey = 5;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "’"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOP"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	@Test
	public void tag_2_806_6() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.806";
		int fieldIdKey = 806;
		int itemIdKey = 6;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "’"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOP"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_807() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.807";
		int fieldIdKey = 807;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "A"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "AB"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_808() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.808";
		int fieldIdKey = 808;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "A"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "AB"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABC"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_809() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.809";
		int fieldIdKey = 809;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "A"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "AB"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABC"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,3,itemIdKey);
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_810() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.810";
		int fieldIdKey = 810;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "123"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1234"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_811() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.811";
		int fieldIdKey = 811;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "123"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1234"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_814() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.814";
		int fieldIdKey = 814;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_815() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.815";
		int fieldIdKey = 815;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_816() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.816";
		int fieldIdKey = 816;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		packet.createItem("300", 2,1,fieldIdKey,3,itemIdKey );
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		packet.createItem("300", 2,1,fieldIdKey,3,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,4,itemIdKey );
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_817() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.817";
		int fieldIdKey = 817;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12345"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "123456"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1234567"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12345678"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "123456789"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1234567890"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_818() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.818";
		int fieldIdKey = 818;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_819() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.819";
		int fieldIdKey = 819;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_822() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.822";
		int fieldIdKey = 822;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "123"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1234"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12345"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_823_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.823";
		int fieldIdKey = 823;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_823_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.823";
		int fieldIdKey = 823;
		int itemIdKey = 2;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "123"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1234"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_823_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.823";
		int fieldIdKey = 823;
		int itemIdKey = 3;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1234567890123456789"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12345678901234567890"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "123456789012345678901"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_2_824_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.824";
		int fieldIdKey = 824;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,itemIdKey, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12345678901234567890123"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "123456789012345678901234"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1234567890123456789012345"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		packet.createItem("300", 2,1,fieldIdKey,2,itemIdKey );
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,itemIdKey);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,itemIdKey, "9"));
		
	}
	
}
