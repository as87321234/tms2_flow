package ca.mgis.junit;

import ca.mgis.ansinist2k.*;
import org.junit.jupiter.api.*;
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
		
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789"));

		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "99"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "9999"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, "9"));
		packet.createItem("300", 1,2,fieldIdKey,1,1);
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, 1,1,1, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,1, "9"));
		packet.createItem("300", 1,2,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,1, "9"));
		
	}
	
	@Test
	public void tag_1_002() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.002";
		int fieldIdKey = 2;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		packet.createItem("1", 1,1,fieldIdKey,1,1);
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789"));
		
		// Test min and max length
		log.info("Check min and max record length");

		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "9"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "99"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "0500"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("0500", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, "9"));
		packet.createItem("0500", 1,1,fieldIdKey,2,1);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("0050", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		packet.deleteAll();
		packet.createItem("0050", 1,1,fieldIdKey,1,1);
		packet.createItem("0050", 1,1,fieldIdKey,2,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		
	}
	
	
	@Test
	public void tag_1_003() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.003";
		int fieldIdKey = 3;
		
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
		packet.createItem("1", 1,1,fieldIdKey,1,1);
		packet.createItem("5", 1,1,fieldIdKey,1,2);
		
		packet.createItem("2", 1,1,fieldIdKey,2,1);
		packet.createItem("00", 1,1,fieldIdKey,2,2);
		
		packet.createItem("10", 1,1,fieldIdKey,3,1);
		packet.createItem("01", 1,1,fieldIdKey,3,2);
		
		packet.createItem("14", 1,1,fieldIdKey,4,1);
		packet.createItem("02", 1,1,fieldIdKey,4,2);
		
		packet.createItem("14", 1,1,fieldIdKey,5,1);
		packet.createItem("03", 1,1,fieldIdKey,5,2);
		
		packet.createItem("14", 1,1,fieldIdKey,6,1);
		packet.createItem("04", 1,1,fieldIdKey,6,2);
		
		// Test CharacterSet
		log.info("Check CharacterSet");

		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "99"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "999"));
		
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "99"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "9999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "99999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "999999"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("00", 1,1,fieldIdKey,1,1);
		packet.createItem("00", 1,1,fieldIdKey,1,2);
		
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("00", 1,1,fieldIdKey,1,1);
		packet.createItem("00", 1,1,fieldIdKey,1,2);
		
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		
		packet.deleteAll();
		packet.createItem("00", 1,1,fieldIdKey,2,1);
		packet.createItem("00", 1,1,fieldIdKey,2,2);
		
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,2,1, "9"));
		
		packet.deleteAll();
		packet.createItem("00", 1,1,fieldIdKey,3,1);
		packet.createItem("00", 1,1,fieldIdKey,3,2);
		
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,3,1, "9"));
		
		packet.deleteAll();
		packet.createItem("00", 1,1,fieldIdKey,4,1);
		packet.createItem("00", 1,1,fieldIdKey,4,2);
		
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,4,1, "9"));
		
		packet.deleteAll();
		packet.createItem("00", 1,1,fieldIdKey,5,1);
		packet.createItem("00", 1,1,fieldIdKey,5,2);
		
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,5,1, "9"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,2,1, "9"));
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,3,1, "9"));
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,4,1, "9"));
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,5,1, "9"));
		
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
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "2024-01-01"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "20240101"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "202401012"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "202401013"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("202401012", 1,1,fieldIdKey,1,1);
		
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("202401012", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		
	}
	
	@Test
	public void tag_1_006() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.006";
		int fieldIdKey = 6;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "2024-01-01"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "0"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "1"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "11"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("202401012", 1,1,fieldIdKey,1,1);
		
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("202401012", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		
		// Validate value
		
		Assertions.assertFalse(validator.validateRegexPattern( tag, fieldIdKey,1, 1, "0"));
		Assertions.assertTrue(validator.validateRegexPattern(  tag, fieldIdKey,1, 1, "1"));
		Assertions.assertTrue(validator.validateRegexPattern(  tag, fieldIdKey,1, 1, "9"));
		Assertions.assertFalse(validator.validateRegexPattern( tag, fieldIdKey,1, 1, "10"));
		
	}
	
	@Test
	public void tag_1_007() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="1.007";
		int fieldIdKey = 7;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ+"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789#"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ@"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "0000000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "ON00000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "00000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "ON000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "ON0000"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("ON00000", 1,1,fieldIdKey,1,1);
		
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("ON00000", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "ON00000"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,1,1, "ON00000"));
		
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
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ+"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789#"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ@"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "0000000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "ON00000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "00000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "ON000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "ON0000"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("ON00000", 1,1,fieldIdKey,1,1);
		
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("ON00000", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "ON00000"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,1,1, "ON00000"));
		
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
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ+"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789#"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ@"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "0000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "00000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "000000000000"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("0000000000000", 1,1,fieldIdKey,1,1);
		
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("0000000000000", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "0000000000000"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,1,1, "0000000000000"));
		
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
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ+"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789#"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ@"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "0000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "00000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "000000000000"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("0000000000000", 1,1,fieldIdKey,1,1);
		
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		packet.deleteAll();
		
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("0000000000000", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "0000000000000"));
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "0000000000000"));
		
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
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ+"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789#"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ@"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "00000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "00.00"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "0000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "00.0"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "00.000"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("0000000000000", 1,1,fieldIdKey,1,1);
		
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("0000000000000", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "0000000000000"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,1,1, "0000000000000"));
		
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
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ+"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789#"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ@"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "00000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "00.00"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "0000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "00.0"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "00.000"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("0000000000000", 1,1,fieldIdKey,1,1);
		
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("0000000000000", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "0000000000000"));
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, fieldIdKey,1,1, "0000000000000"));
		
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
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "20200325173337Z"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "20200325173337Z."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "20200325173337Z#"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "20200325173337Z"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "202003251733370Z"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "2020032517333Z"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		
		packet.createItem("20200325173337Z", 1,1,fieldIdKey,1,1);
		
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		packet.deleteAll();
		
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("20200325173337Z", 1,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "0000000000000"));
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "0000000000000"));
		
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
		
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "99"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "9999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "99999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "999999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "9999999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "99999999"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,1);
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, 1,1,1, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,1, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,1, "9"));
		
	}
	
	@Test
	public void tag_2_002() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.002";
		int fieldIdKey = 2;
		
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "TWO"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "9"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "99"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "9999"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "99999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "999999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "9999999"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "99999999"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,1);
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,1, "9"));
		
	}
	
	@Test
	public void tag_2_067_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.067";
		int fieldIdKey = 67;
		
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWX"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXY"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,1);
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,1, "9"));
		
	}
	
	@Test
	public void tag_2_067_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.067";
		int fieldIdKey = 67;
		
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,2, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,2, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,2, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,2, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,2, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,2, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,2, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWX"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,2, "ABCDEFGHIJKLMNOPQRSTUVWXY"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,2, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,2);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,2, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,2);
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,2, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,2);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,2, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,2);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,2, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,2, "9"));
		
	}
	
	@Test
	public void tag_2_067_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.067";
		int fieldIdKey = 67;
		
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,3, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,3, "0123456789"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,3, " #$&’()*,-."));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,3, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,3, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,3, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,3, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWX"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,3, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXZ"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,3, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXZZ"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,3);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,3, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,3);
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,3, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,3);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,3, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,3);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,3, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,3, "9"));
		
	}
	
	@Test
	public void tag_2_800() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.800";
		int fieldIdKey = 800;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, ""));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "0123456789012345678"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "012345678901234567899"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "01234567890123456789"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,1);
		Assertions.assertFalse(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition( packet, tag, 1,1,1, "9"));
		
	}
	@Test
	public void tag_2_801() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator( (new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag="2.801";
		int fieldIdKey = 801;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertTrue(validator.validateCharacterSet(tag, fieldIdKey,1,1, "0123456789"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, " #$&’()*,-."));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, " #$&’()*,-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		Assertions.assertFalse(validator.validateCharacterSet(tag, fieldIdKey,1,1, "AbCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, ""));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "ON00000"));
		Assertions.assertFalse(validator.validateFieldLength(tag, fieldIdKey,1,1, "ON000000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "ON0000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "ON000"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "ON00"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "O00"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "00"));
		Assertions.assertTrue(validator.validateFieldLength(tag, fieldIdKey,1,1, "0"));
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, "9"));
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence( packet, tag, fieldIdKey,1,1, "9"));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		packet.createItem("300", 2,1,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		packet.createItem("300", 2,2,fieldIdKey,1,1);
		Assertions.assertTrue(validator.validateCondition( packet, tag, fieldIdKey,1,1, "9"));
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition( packet, tag, 1,1,1, "9"));
		
	}
	
	
}
