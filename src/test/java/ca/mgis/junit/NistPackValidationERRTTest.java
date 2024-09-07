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

@SpringBootTest(classes = NistPackValidationERRTTest.class)
public class NistPackValidationERRTTest {
	
	Logger log = LoggerFactory.getLogger(NistPackValidationERRTTest.class);
	
	private String transactionType = "ERRT";
	
	@Test
	public void logger() throws IOException {
		log.trace("A TRACE Message");
		log.debug("A DEBUG Message");
		log.info("An INFO Message");
		log.warn("A WARN Message");
		log.error("An ERROR Message");
	}
	
	@Test
	void deserialize1_7_7fJson() {
		
		AnsiNistPacket ansiNistPacket = new AnsiNistPacket();
		ansiNistPacket.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		
		Assertions.assertNotNull(ansiNistPacket.getAnsiNistValidator());
		
	}
	
	@Test
	public void loadEFT() throws Exception {
		
		byte[] orginal = new FileInputStream("src/main/resources/test1.eft").readAllBytes();
		AnsiNistPacket ansiNistPacket = new AnsiNistPacket("src/main/resources/test1.eft", new Validator177f());
		ansiNistPacket.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		
		byte[] serializedEft = ansiNistPacket.serialize();
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File("src/main/resources/test1_serialized.eft"));
			fos.write(serializedEft);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			// Put data in your baos
			baos.writeTo(fos);
			
		} catch (IOException ioe) {
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
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.001";
		int recordType = 1;
		int fieldIdKey = 1;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "99"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "999"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "9999"));
		
	}
	
	@Test
	public void tag_1_002() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.002";
		int recordType = 1;
		int fieldIdKey = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		packet.createItem("300", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		
	}
	
	@Test
	public void tag_1_003_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.003";
		int recordType = 1;
		int fieldIdKey = 3;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "99"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "999"));
		
	}
	
	@Test
	public void tag_1_003_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.003";
		int recordType = 1;
		int fieldIdKey = 3;
		int subfieldIdKey = 2;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "99"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "999"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "9999"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "99999"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "999999"));
		
	}
	
	@Test
	public void tag_1_003_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.003";
		int recordType = 1;
		int fieldIdKey = 3;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "99"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "999"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "9999"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "99999"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "999999"));
		
	}
	
	@Test
	public void tag_1_004() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.004";
		int recordType = 1;
		int fieldIdKey = 4;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("A", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "AA"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "AAA"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "AAAA"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "AAAAA"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "AAAAAA"));
		
	}
	
	@Test
	public void tag_1_005() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.005";
		int recordType = 1;
		int fieldIdKey = 5;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "20240101"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "202401012"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "202401013"));
		
	}
	
	@Test
	public void tag_1_006() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.006";
		int recordType = 1;
		int fieldIdKey = 6;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "0"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "11"));
		
	}
	
	@Test
	public void tag_1_007() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.007";
		int recordType = 1;
		int fieldIdKey = 7;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("AN", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000000"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON00000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "00000000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON000000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "000000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON0000"));
		
	}
	
	@Test
	public void tag_1_008() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.008";
		int recordType = 1;
		int fieldIdKey = 8;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("AN", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000000"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON00000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "00000000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON000000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "000000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON0000"));
		
	}
	
	@Test
	public void tag_1_009() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.009";
		int recordType = 1;
		int fieldIdKey = 9;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "00000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "000000000000"));
		
	}
	
	@Test
	public void tag_1_010() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.010";
		int recordType = 1;
		int fieldIdKey = 10;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "00000000000000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "000000000000"));
		
	}
	
	@Test
	public void tag_1_011() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.011";
		int recordType = 1;
		int fieldIdKey = 11;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("NP", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "00000"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "00.00"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "00.0"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "00.000"));
		
	}
	
	@Test
	public void tag_1_012() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.012";
		int recordType = 1;
		int fieldIdKey = 12;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("NP", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "00000"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "00.00"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "00.0"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "00.000"));
		
	}
	
	@Test
	public void tag_1_014() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "1.014";
		int recordType = 1;
		int fieldIdKey = 14;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("AN", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "20200325173337Z"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "202003251733370Z"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "2020032517333Z"));
		
	}
	
	@Test
	public void tag_2_001() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.001";
		int recordType = 2;
		int fieldIdKey = 1;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1,' ')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2,' ')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3,' ')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6,' ')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(7,' ')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(8,' ')));
		
	}
	
	@Test
	public void tag_2_002() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.002";
		int recordType = 2;
		int fieldIdKey = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "99"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "999"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "9999"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "99999"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "999999"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "9999999"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "99999999"));
		
	}
	
	@Test
	public void tag_2_800() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.800";
		int fieldIdKey = 800;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "0123456789012345678"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "012345678901234567899"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "01234567890123456789"));
		
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
		checkCharacterSet("ANSC", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(999, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1000, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1001, '1')));
		
	}
	
	@Test
	public void tag_2_875() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.875";
		int fieldIdKey = 875;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("A", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		
	}
	
	@Test
	public void tag_2_8060_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8060";
		int fieldIdKey = 8060;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		
	}
	
	@Test
	public void tag_2_8060_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8060";
		int fieldIdKey = 8060;
		int recordType = 2;
		int subfieldIdKey = 2;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Condition Mandatory
		log.info("Check field condition");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateCondition(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		// Test min and max length
		log.info("Check min and max record length");
		
		packet.deleteAll();
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(299, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(300, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(301, '1')));
		
	}
	
	@Disabled
	private void checkCharacterSet(String validSet, AnsiNistValidator validator, String transactionType, String tag, int fieldIdKey, int subfieldIdKey, int itemIdKey) {
		
		if (!validSet.equals("Y")) {
			if (validSet.matches(".*A.*"))
				Assertions.assertTrue(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getAlphaCharacterSet()));
			else
				Assertions.assertFalse(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getAlphaCharacterSet()));
			
			if (validSet.matches(".*N.*"))
				Assertions.assertTrue(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getNumericCharacterSet()));
			else
				Assertions.assertFalse(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getNumericCharacterSet()));
			
			if (validSet.matches(".*S.*"))
				Assertions.assertTrue(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getSpecialCharacterSet()));
			else
				Assertions.assertFalse(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getSpecialCharacterSet()));
			
			if (validSet.matches(".*C.*"))
				Assertions.assertTrue(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getCrlfCharacterSet()));
			else
				Assertions.assertFalse(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getCrlfCharacterSet()));
			
			if (validSet.matches(".*H.*") || validSet.matches(".*S.*"))
				Assertions.assertTrue(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getHyphenCharacterSet()));
			else
				Assertions.assertFalse(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getHyphenCharacterSet()));
			
			if (validSet.matches(".*P.*") || validSet.matches(".*S.*"))
				Assertions.assertTrue(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getPeriodCharacterSet()));
			else
				Assertions.assertFalse(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getPeriodCharacterSet()));
			
			if (validSet.matches(".*B.*") || validSet.matches(".*S.*"))
				Assertions.assertTrue(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getSpaceCharacterSet()));
			else
				Assertions.assertFalse(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getSpaceCharacterSet()));
			
			if (validSet.matches(".*T.*") || validSet.matches(".*S.*"))
				Assertions.assertTrue(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getApostropheCharacterSet()));
			else
				Assertions.assertFalse(validator.validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, validator.getApostropheCharacterSet()));
		} else {
			
			if (validSet.matches("Y"))
				Assertions.assertTrue(true);
			else
				Assertions.assertTrue(false);
			
		}
		
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