package ca.mgis.junit;

import ca.mgis.ansinist2k.AnsiNistPacket;
import ca.mgis.ansinist2k.AnsiNistValidator;
import ca.mgis.ansinist2k.ValidationDeserializerImpl;
import ca.mgis.ansinist2k.Validator177f;
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
		int fieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789"));

		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "99"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "9999"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 1, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.createItem("300", 1, 2, fieldIdKey, 1, itemIdKey);
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, 1, 1, itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 1, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, 1, 1, itemIdKey, "9"));
		packet.createItem("300", 1, 2, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, 1, 1, itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_1_002() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.002";
		int fieldIdKey = 2;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		packet.createItem("1", 1, 1, fieldIdKey, 1, itemIdKey);
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789"));
		
		// Test min and max length
		log.info("Check min and max record length");

		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "9"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "99"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "0500"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("0500", 1, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.createItem("0500", 1, 1, fieldIdKey, 2, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("0050", 1, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.deleteAll();
		packet.createItem("0050", 1, 1, fieldIdKey, 1, itemIdKey);
		packet.createItem("0050", 1, 1, fieldIdKey, 2, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		
	}
	
	
	@Test
	public void tag_1_003() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.003";
		int fieldIdKey = 3;
		int itemIdKey = 1;
		
		//		2024-09-01 14:07:54 [main] DEBUG  ca.mgis.ansinist2k.AnsiNistDecoder:160 - field 1.1.3.1.1=1
		//		2024-09-01 14:07:54 [main] DEBUG  ca.mgis.ansinist2k.AnsiNistDecoder:160 - field 1.1.3.1.2=5
		//		2024-09-01 14:07:54 [main] DEBUG  ca.mgis.ansinist2k.AnsiNistDecoder:160 - field 1.1.3.2.1=2
		//		2024-09-01 14:07:54 [main] DEBUG  ca.mgis.ansinist2k.AnsiNistDecoder:160 - field 1.1.3.2.2=00
		//		2024-09-01 14:07:54 [main] DEBUG  ca.mgis.ansinist2k.AnsiNistDecoder:160 - field 1.1.3.3.1=10
		//		2024-09-01 14:07:54 [main] DEBUG  ca.mgis.ansinist2k.AnsiNistDecoder:160 - field 1.1.3.3.2=01
		//		2024-09-01 14:07:54 [main] DEBUG  ca.mgis.ansinist2k.AnsiNistDecoder:160 - field 1.1.3.4.1=14
		//		2024-09-01 14:07:54 [main] DEBUG  ca.mgis.ansinist2k.AnsiNistDecoder:160 - field 1.1.3.4.2=02
		//		2024-09-01 14:07:54 [main] DEBUG  ca.mgis.ansinist2k.AnsiNistDecoder:160 - field 1.1.3.5.1=14
		//		2024-09-01 14:07:54 [main] DEBUG  ca.mgis.ansinist2k.AnsiNistDecoder:160 - field 1.1.3.5.2=03
		//		2024-09-01 14:07:54 [main] DEBUG  ca.mgis.ansinist2k.AnsiNistDecoder:160 - field 1.1.3.6.1=14
		//		2024-09-01 14:07:54 [main] DEBUG  ca.mgis.ansinist2k.AnsiNistDecoder:160 - field 1.1.3.6.2=04
		
		packet.deleteAll();
		packet.createItem("1", 1, 1, fieldIdKey, 1, itemIdKey);
		packet.createItem("5", 1, 1, fieldIdKey, 1, itemIdKey + 1);
		
		packet.createItem("2", 1, 1, fieldIdKey, 2, itemIdKey);
		packet.createItem("00", 1, 1, fieldIdKey, 2, itemIdKey + 1);
		
		packet.createItem("10", 1, 1, fieldIdKey, 3, itemIdKey);
		packet.createItem("01", 1, 1, fieldIdKey, 3, itemIdKey + 1);
		
		packet.createItem("14", 1, 1, fieldIdKey, 4, itemIdKey);
		packet.createItem("02", 1, 1, fieldIdKey, 4, itemIdKey + 1);
		
		packet.createItem("14", 1, 1, fieldIdKey, 5, itemIdKey);
		packet.createItem("03", 1, 1, fieldIdKey, 5, itemIdKey + 1);
		
		packet.createItem("14", 1, 1, fieldIdKey, 6, itemIdKey);
		packet.createItem("04", 1, 1, fieldIdKey, 6, itemIdKey + 2);
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "99"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "999"));
		
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "99"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "9999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "99999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "999999"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("00", 1, 1, fieldIdKey, 1, itemIdKey);
		packet.createItem("00", 1, 1, fieldIdKey, 1, itemIdKey + 1);
		
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("00", 1, 1, fieldIdKey, 1, itemIdKey);
		packet.createItem("00", 1, 1, fieldIdKey, 1, itemIdKey + 1);
		
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("00", 1, 1, fieldIdKey, 2, itemIdKey);
		packet.createItem("00", 1, 1, fieldIdKey, 2, itemIdKey + 1);
		
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 2, itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("00", 1, 1, fieldIdKey, 3, itemIdKey);
		packet.createItem("00", 1, 1, fieldIdKey, 3, itemIdKey + 1);
		
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 3, itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("00", 1, 1, fieldIdKey, 4, itemIdKey);
		packet.createItem("00", 1, 1, fieldIdKey, 4, itemIdKey + 1);
		
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 4, itemIdKey, "9"));
		
		packet.deleteAll();
		packet.createItem("00", 1, 1, fieldIdKey, 5, itemIdKey);
		packet.createItem("00", 1, 1, fieldIdKey, 5, itemIdKey + 1);
		
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 5, itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, 2, itemIdKey, "9"));
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, 3, itemIdKey, "9"));
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, 4, itemIdKey, "9"));
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, 5, itemIdKey, "9"));
		
	}
	
	
	@Test
	public void tag_1_004() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.004";
		int fieldIdKey = 4;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "AA"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "AAAAAA"));
		
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "A0A"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();

		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "AA"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "AAA"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "AAAA"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "AAAAA"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "AAAAAA"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("TRE", 1,1,fieldIdKey,1,1);
		
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("TRE", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		
	}
	
	
	@Test
	public void tag_1_005() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.005";
		int fieldIdKey = 5;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "2024-01-01"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "20240101"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "202401012"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "202401013"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("202401012", 1, 1, fieldIdKey, 1, itemIdKey);
		
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("202401012", 1, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		
	}
	
	@Test
	public void tag_1_006() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.006";
		int fieldIdKey = 6;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "2024-01-01"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "0"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "11"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("202401012", 1, 1, fieldIdKey, 1, itemIdKey);
		
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("202401012", 1, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "9"));
		
		// Validate value
		
		Assertions.assertFalse(validator.validateRegexPattern(tag, fieldIdKey, 1, itemIdKey, "0"));
		Assertions.assertTrue(validator.validateRegexPattern(tag, fieldIdKey, 1, itemIdKey, "1"));
		Assertions.assertTrue(validator.validateRegexPattern(tag, fieldIdKey, 1, itemIdKey, "9"));
		Assertions.assertFalse(validator.validateRegexPattern(tag, fieldIdKey, 1, itemIdKey, "10"));
		
	}
	
	@Test
	public void tag_1_007() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.007";
		int fieldIdKey = 7;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ+"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789#"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ@"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "0000000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ON00000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "00000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ON000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ON0000"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("ON00000", 1, 1, fieldIdKey, 1, itemIdKey);
		
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("ON00000", 1, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "ON00000"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "ON00000"));
		
		// Validate value
		
		// Not applicable
		
	}
	
	@Test
	public void tag_1_008() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.008";
		int fieldIdKey = 8;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ+"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789#"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ@"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "0000000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ON00000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "00000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ON000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "ON0000"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("ON00000", 1,1,fieldIdKey,1,1);
		
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("ON00000", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "ON00000"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "ON00000"));
		
		// Validate value
		
		// Not applicable
		
	}
	
	@Test
	public void tag_1_009() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.009";
		int fieldIdKey = 9;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ+"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789#"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ@"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "0000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "00000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "000000000000"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("0000000000000", 1, 1, fieldIdKey, 1, itemIdKey);
		
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("0000000000000", 1, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "0000000000000"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "0000000000000"));
		
		// Validate value
		
		// Not applicable
		
	}
	
	@Test
	public void tag_1_010() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.010";
		int fieldIdKey = 10;
		int itemIdKey = 1;
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ+"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789#"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ@"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "0000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "00000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "000000000000"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("0000000000000", 1, 1, fieldIdKey, 1, itemIdKey);
		
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		packet.deleteAll();
		
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("0000000000000", 1, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "0000000000000"));
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "0000000000000"));
		
		// Validate value
		
		// Not applicable
		
	}
	
	@Test
	public void tag_1_011() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.011";
		int fieldIdKey = 11;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ+"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789#"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ@"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "00000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "00.00"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "0000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "00.0"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "00.000"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("0000000000000", 1, 1, fieldIdKey, 1, itemIdKey);
		
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("0000000000000", 1, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "0000000000000"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "0000000000000"));
		
		// Validate value
		
		// Not applicable
		
	}
	
	@Test
	public void tag_1_012() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.012";
		int fieldIdKey = 12;
		int itemIdKey = 1;
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ+"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789#"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ@"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "00000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "00.00"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "0000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "00.0"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "00.000"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("0000000000000", 1, 1, fieldIdKey, 1, itemIdKey);
		
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("0000000000000", 1, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "0000000000000"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "0000000000000"));
		
		// Validate value
		
		// Not applicable
		
	}
	
	@Test
	public void tag_1_014() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.014";
		int fieldIdKey = 14;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "20200325173337Z"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "20200325173337Z."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey, 1, itemIdKey, "20200325173337Z#"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "20200325173337Z"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "202003251733370Z"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey, 1, itemIdKey, "2020032517333Z"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("20200325173337Z", 1, 1, fieldIdKey, 1, itemIdKey);
		
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		packet.deleteAll();
		
		Assertions.assertTrue(validator.validateOccurrence(packet, tag, fieldIdKey, 1, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("20200325173337Z", 1, 1, fieldIdKey, 1, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "0000000000000"));
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(packet, tag, fieldIdKey, 1, itemIdKey, "0000000000000"));
		
		// Validate value
		
		// Not applicable
		
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
		int itemIdKey = 3;
		
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
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "0"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "01"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,itemIdKey, "001"));
		
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
	
	
	
	
}
