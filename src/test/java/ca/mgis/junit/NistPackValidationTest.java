package ca.mgis.junit;

import ca.mgis.ansinist2k.AnsiNistPacket;
import ca.mgis.ansinist2k.AnsiNistValidator;
import ca.mgis.ansinist2k.ValidationDeserializerImpl;
import ca.mgis.ansinist2k.Validator177f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "99"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "9999"));
		
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
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		packet.createItem("300", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		
	}
	
	@Test
	public void tag_1_003_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.003";
		int recordType = 1;
		int fieldIdKey = 3;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "99"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "999"));
		
	}
	
	@Test
	public void tag_1_003_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.003";
		int recordType = 1;
		int fieldIdKey = 3;
		int subfieldIdKey = 2;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "99"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "9999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "99999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "999999"));
		
	}
	@Test
	public void tag_1_003_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.003";
		int recordType = 1;
		int fieldIdKey = 3;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "99"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "9999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "99999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "999999"));
		
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
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("A", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "AA"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "AAA"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "AAAA"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "AAAAA"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "AAAAAA"));
		
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
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "20240101"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "202401012"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "202401013"));
		
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
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "0"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "11"));
		
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
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("AN", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON00000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "00000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON0000"));
		
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
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("AN", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON00000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "00000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON0000"));
		
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
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "00000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "000000000000"));
		
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
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "00000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "000000000000"));
		
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
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("NP", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "00000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "00.00"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "00.0"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "00.000"));
		
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
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("NP", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "00000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "00.00"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "00.0"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "00.000"));
		
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
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("AN", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "20200325173337Z"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "202003251733370Z"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "2020032517333Z"));
		
	}
	
	@Test
	public void tag_2_001() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.001";
		int recordType = 2;
		int fieldIdKey = 1;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "99"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "9999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "99999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "999999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "9999999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "99999999"));
		
	}
	
	@Test
	public void tag_2_002() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.002";
		int recordType = 2;
		int fieldIdKey = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
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
		
		
	}
	
	@Test
	public void tag_2_067_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.067";
		int fieldIdKey = 67;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
	}
	
	@Test
	public void tag_2_067_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.067";
		int fieldIdKey = 67;
		int recordType = 2;
		int subfieldIdKey = 2;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWX"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXY"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
	}
	
	@Test
	public void tag_2_067_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.067";
		int recordType = 2;
		int fieldIdKey = 67;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWX"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXZ"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXZZ"));
		
		
	}
	
	@Test
	public void tag_2_800() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.800";
		int fieldIdKey = 800;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "0123456789012345678"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "012345678901234567899"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "01234567890123456789"));
		
	}
	
	@Test
	public void tag_2_801() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.801";
		int fieldIdKey = 801;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("AN", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "ON00000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "ON000000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "ON0000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "ON000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "ON00"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "O00"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "00"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "0"));
		
		
	}
	
	
	@Test
	public void tag_2_802_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.802";
		int fieldIdKey = 802;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "A"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "ABCDEFGHIJKLMNOPQRSTQUVWXABCDEFGHIJKLMNOPQRSTQUVWX"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "ABCDEFGHIJKLMNOPQRSTQUVWXABCDEFGHIJKLMNOPQRSTQUVWXZ"));
	}
	
	@Test
	public void tag_2_802_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.802";
		int fieldIdKey = 802;
		int recordType = 2;
		int subfieldIdKey = 2;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "A"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "ABCDEFGHIJKLMNOPQRSTQUVWXABCDEFGHIJ"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "ABCDEFGHIJKLMNOPQRSTQUVWXABCDEFGHIJK"));
		
	}
	
	@Test
	public void tag_2_802_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.802";
		int fieldIdKey = 802;
		int recordType = 2;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "0"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "01"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,subfieldIdKey,itemIdKey, "001"));
		
	}
	
	@Test
	public void tag_2_803() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.803";
		int fieldIdKey = 803;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123ABCDEFGHIJKLMNOPQRSTUVWXYZ012"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123ABCDEFGHIJKLMNOPQRSTUVWXYZ0123"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123ABCDEFGHIJKLMNOPQRSTUVWXYZ01234"));
		
		
	}
	
	@Test
	public void tag_2_804() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.804";
		int fieldIdKey = 804;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ01234"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ012345"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456"));
		
	}
	
	
	@Test
	public void tag_2_806_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.806";
		int fieldIdKey = 806;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABHTP", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVW"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWX"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXY"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
	}
	
	@Test
	public void tag_2_806_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.806";
		int fieldIdKey = 806;
		int recordType = 2;
		int subfieldIdKey = 2;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABHTP", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOP"));
		
		
	}
	
	@Test
	public void tag_2_806_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.806";
		int fieldIdKey = 806;
		int recordType = 2;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABHTP", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOP"));
		
	}
	
	@Test
	public void tag_2_806_4() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.806";
		int fieldIdKey = 806;
		int recordType = 2;
		int subfieldIdKey = 4;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABHTP", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOP"));
		
		
	}
	
	@Test
	public void tag_2_806_5() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.806";
		int fieldIdKey = 806;
		int recordType = 2;
		int subfieldIdKey = 5;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABHTP", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOP"));
		
	}
	@Test
	public void tag_2_806_6() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.806";
		int fieldIdKey = 806;
		int recordType = 2;
		int subfieldIdKey = 6;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABHTP", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOP"));
		
	}
	
	@Test
	public void tag_2_807() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.807";
		int fieldIdKey = 807;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("A", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "A"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "AB"));
		
		
	}
	
	
	@Test
	public void tag_2_808() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.808";
		int fieldIdKey = 808;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "A"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "AB"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABC"));
		
	}
	
	@Test
	public void tag_2_809() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.809";
		int fieldIdKey = 809;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("A", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "A"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "AB"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABC"));
		
	}
	
	@Test
	public void tag_2_810() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.810";
		int fieldIdKey = 810;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234"));
		
		
	}
	
	@Test
	public void tag_2_811() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.811";
		int fieldIdKey = 811;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "12"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "123"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "1234"));
		
		
	}
	
	@Test
	public void tag_2_814() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.814";
		int fieldIdKey = 814;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("A", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
	}
	
	@Test
	public void tag_2_815() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.815";
		int fieldIdKey = 815;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("A", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
	}
	
	@Test
	public void tag_2_816() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.816";
		int fieldIdKey = 816;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("A", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
	}
	
	
	@Test
	public void tag_2_817() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.817";
		int fieldIdKey = 817;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("AN", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890"));
		
		
	}
	
	@Test
	public void tag_2_818() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.818";
		int fieldIdKey = 818;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("A", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
		
	}
	
	@Test
	public void tag_2_819() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.819";
		int fieldIdKey = 819;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("A", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
		
	}
	
	@Test
	public void tag_2_822() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.822";
		int fieldIdKey = 822;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345"));
		
	}
	
	@Test
	public void tag_2_823_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.823";
		int fieldIdKey = 823;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("A", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
	}
	
	@Test
	public void tag_2_823_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.823";
		int fieldIdKey = 823;
		int recordType = 2;
		int subfieldIdKey = 2;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234"));
		
	}
	
	@Test
	public void tag_2_823_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.823";
		int fieldIdKey = 823;
		int recordType = 2;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890123456789"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234567890"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012345678901"));
		
		
	}
	
	@Test
	public void tag_2_824_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.824";
		int fieldIdKey = 824;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABTHP", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234567890123"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012345678901234"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890123456789012345"));
		
	}
	
	@Test
	public void tag_2_824_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.824";
		int fieldIdKey = 824;
		int recordType = 2;
		int subfieldIdKey = 2;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABTHP", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012345"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890123456"));
		
	}
	
	@Test
	public void tag_2_824_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.824";
		int fieldIdKey = 824;
		int recordType = 2;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABTHP", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012345"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890123456"));
		
	}
	
	@Test
	public void tag_2_824_4() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.824";
		int fieldIdKey = 824;
		int recordType = 2;
		int subfieldIdKey = 4;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABTHP", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012345"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890123456"));
		
	}
	
	@Test
	public void tag_2_824_5() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.824";
		int fieldIdKey = 824;
		int recordType = 2;
		int subfieldIdKey = 5;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABTHP", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012345"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890123456"));
		
	}
	
	@Test
	public void tag_2_827() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.827";
		int fieldIdKey = 827;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(999, 'A')));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1000, 'A')));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1001, 'A')));
		
	}
	
	@Test
	public void tag_2_833() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.833";
		int fieldIdKey = 833;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123"));
		
	}
	
	@Test
	public void tag_2_838() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.838";
		int fieldIdKey = 838;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
	}
	
	@Test
	public void tag_2_848_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.848";
		int fieldIdKey = 848;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
	}
	
	@Test
	public void tag_2_848_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.848";
		int fieldIdKey = 848;
		int recordType = 2;
		int subfieldIdKey = 2;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012345678901234"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890123456789012345"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234567890123456"));
		
	}
	
	@Test
	public void tag_2_848_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.848";
		int fieldIdKey = 848;
		int recordType = 2;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "123"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345"));
		
	}
	
	@Test
	public void tag_2_848_4() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.848";
		int fieldIdKey = 848;
		int recordType = 2;
		int subfieldIdKey = 4;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("AS", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, 'A')));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(50, 'A')));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(51, 'A')));
		
	}
	
	@Test
	public void tag_2_848_5() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.848";
		int fieldIdKey = 848;
		int recordType = 2;
		int subfieldIdKey = 5;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("NP", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "0"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "000.00"));
		
	}
	
	@Test
	public void tag_2_849() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.849";
		int fieldIdKey = 849;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("AN", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "000000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, "00000000"));
		
	}
	
	@Test
	public void tag_2_850() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.850";
		int fieldIdKey = 850;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, 'A')));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(49, 'A')));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(50, 'A')));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(51, 'A')));
		
	}
	
	@Test
	public void tag_2_869_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.869";
		int fieldIdKey = 869;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		
	}
	
	@Test
	public void tag_2_869_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.869";
		int fieldIdKey = 869;
		int recordType = 2;
		int subfieldIdKey = 2;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, 'A')));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, 'A')));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, 'A')));
		
	}
	
	@Test
	public void tag_2_870() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.870";
		int fieldIdKey = 870;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		
	}
	
	@Test
	public void tag_2_871() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.871";
		int fieldIdKey = 871;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		
	}
	
	@Test
	public void tag_2_872() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.872";
		int fieldIdKey = 872;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		
	}
	
	@Test
	public void tag_2_873() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.873";
		int fieldIdKey = 873;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(299, '1')));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(300, '1')));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(301, '1')));
		
	}
	
	@Test
	public void tag_2_874() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.874";
		int fieldIdKey = 874;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("A", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		
	}
	
	@Test
	public void tag_2_886() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.886";
		int fieldIdKey = 886;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("A", validator, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(11, '1')));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(12, '1')));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(13, '1')));
		
	}
	
	@Disabled
	private void checkCharacterSet(String validSet, AnsiNistValidator validator, String tag, int fieldIdKey, int subfieldIdKey, int itemIdKey) {
		
		if (validSet.matches(".*A.*"))
			Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getAlphaCharacterSet()));
		else
			Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getAlphaCharacterSet()));
		
		if (validSet.matches(".*N.*"))
			Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getNumericCharacterSet()));
		else
			Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getNumericCharacterSet()));
		
		if (validSet.matches(".*S.*"))
			Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getSpecialCharacterSet()));
		else
			Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getSpecialCharacterSet()));
		
		if (validSet.matches(".*C.*"))
			Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getCrlfCharacterSet()));
		else
			Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getCrlfCharacterSet()));
		
		if (validSet.matches(".*H.*") || validSet.matches(".*S.*"))
			Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getHyphenCharacterSet()));
		else
			Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getHyphenCharacterSet()));
		
		if (validSet.matches(".*P.*") || validSet.matches(".*S.*"))
			Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getPeriodCharacterSet()));
		else
			Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getPeriodCharacterSet()));
		
		if (validSet.matches(".*B.*") || validSet.matches(".*S.*"))
			Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getSpaceCharacterSet()));
		else
			Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getSpaceCharacterSet()));
		
		if (validSet.matches(".*T.*") || validSet.matches(".*S.*"))
			Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getApostropheCharacterSet()));
		else
			Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getApostropheCharacterSet()));
		
	}
	
	@Disabled
	private String buildString(int length, Character repeatChar) {
		
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int i = 0; i < length; i++) {
			stringBuilder.append(repeatChar);
		}
		
		return stringBuilder.toString();
	}
	
}