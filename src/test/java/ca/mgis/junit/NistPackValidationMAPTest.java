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

@SpringBootTest(classes = NistPackValidationMAPTest.class)
public class NistPackValidationMAPTest {
	
	Logger log = LoggerFactory.getLogger(NistPackValidationMAPTest.class);
	
	private String transactionType = "MAP";
	
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "9"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "99"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "999"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "9999"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "99999"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "999999"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "9999999"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "99999999"));
		
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
	public void tag_2_067_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.067";
		int fieldIdKey = 67;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		
		// Test Min Max Occurrence
		log.info("Check min and max number of occurrences");
		
		packet.deleteAll();
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
		packet.deleteAll();
		packet.createItem("", recordType, 1, fieldIdKey, subfieldIdKey, itemIdKey);
		Assertions.assertTrue(validator.validateOccurrence(transactionType, packet, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		
	}
	
	@Test
	public void tag_2_067_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.067";
		int fieldIdKey = 67;
		int recordType = 2;
		int subfieldIdKey = 2;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator,transactionType,  tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWX"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXY"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
	}
	
	@Test
	public void tag_2_067_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.067";
		int recordType = 2;
		int fieldIdKey = 67;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator,transactionType,  tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWX"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXZ"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYABCDEFGHIJKLMNOPQRSTUVWXZZ"));
		
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
	public void tag_2_801() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.801";
		int fieldIdKey = 801;
		int recordType = 2;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON00000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON000000"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON0000"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON000"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ON00"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "O00"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "00"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "0"));
		
	}
	
	
	@Test
	public void tag_2_802_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.802";
		int fieldIdKey = 802;
		int recordType = 2;
		int subfieldIdKey = 1;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "A"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTQUVWXABCDEFGHIJKLMNOPQRSTQUVWX"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTQUVWXABCDEFGHIJKLMNOPQRSTQUVWXZ"));
	}
	
	@Test
	public void tag_2_802_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.802";
		int fieldIdKey = 802;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "A"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTQUVWXABCDEFGHIJ"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOPQRSTQUVWXABCDEFGHIJK"));
		
	}
	
	@Test
	public void tag_2_802_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.802";
		int fieldIdKey = 802;
		int recordType = 2;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("N", validator,transactionType,  tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "0"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "01"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "001"));
		
	}
	
	@Test
	public void tag_2_803() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.803";
		int fieldIdKey = 803;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123ABCDEFGHIJKLMNOPQRSTUVWXYZ012"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123ABCDEFGHIJKLMNOPQRSTUVWXYZ0123"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123ABCDEFGHIJKLMNOPQRSTUVWXYZ01234"));
		
	}
	
	@Test
	public void tag_2_804() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.804";
		int fieldIdKey = 804;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ01234"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ012345"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456"));
		
	}
	
	@Test
	public void tag_2_806_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.806";
		int fieldIdKey = 806;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABHTP", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVW"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWX"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXY"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		
	}
	
	@Test
	public void tag_2_806_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.806";
		int fieldIdKey = 806;
		int recordType = 2;
		int subfieldIdKey = 2;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABHTP", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOP"));
		
	}
	
	@Test
	public void tag_2_806_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.806";
		int fieldIdKey = 806;
		int recordType = 2;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABHTP", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOP"));
		
	}
	
	@Test
	public void tag_2_806_4() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.806";
		int fieldIdKey = 806;
		int recordType = 2;
		int subfieldIdKey = 4;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABHTP", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOP"));
		
	}
	
	@Test
	public void tag_2_806_5() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.806";
		int fieldIdKey = 806;
		int recordType = 2;
		int subfieldIdKey = 5;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABHTP", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOP"));
		
	}
	
	@Test
	public void tag_2_806_6() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.806";
		int fieldIdKey = 806;
		int recordType = 2;
		int subfieldIdKey = 6;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABHTP", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMN"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNO"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABCDEFGHIJKLMNOP"));
		
	}
	
	@Test
	public void tag_2_807() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.807";
		int fieldIdKey = 807;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "A"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "AB"));
		
	}
	
	
	@Test
	public void tag_2_808() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.808";
		int fieldIdKey = 808;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "A"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "AB"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABC"));
		
	}
	
	@Test
	public void tag_2_809() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.809";
		int fieldIdKey = 809;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "A"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "AB"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "ABC"));
		
	}
	
	@Test
	public void tag_2_810() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.810";
		int fieldIdKey = 810;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234"));
		
	}
	
	@Test
	public void tag_2_811() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.811";
		int fieldIdKey = 811;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "1"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "12"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "123"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, 1, itemIdKey, "1234"));
		
	}
	
	@Test
	public void tag_2_814() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.814";
		int fieldIdKey = 814;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
	}
	
	@Test
	public void tag_2_815() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.815";
		int fieldIdKey = 815;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
	}
	
	@Test
	public void tag_2_816() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.816";
		int fieldIdKey = 816;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
	}
	
	@Test
	public void tag_2_817() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.817";
		int fieldIdKey = 817;
		int recordType = 2;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890"));
		
	}
	
	@Test
	public void tag_2_818() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.818";
		int fieldIdKey = 818;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
	}
	
	@Test
	public void tag_2_819() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.819";
		int fieldIdKey = 819;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
	}
	
	@Test
	public void tag_2_822() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.822";
		int fieldIdKey = 822;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345"));
		
	}
	
	@Test
	public void tag_2_823_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.823";
		int fieldIdKey = 823;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
	}
	
	@Test
	public void tag_2_823_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.823";
		int fieldIdKey = 823;
		int recordType = 2;
		int subfieldIdKey = 2;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234"));
		
	}
	
	@Test
	public void tag_2_823_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.823";
		int fieldIdKey = 823;
		int recordType = 2;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890123456789"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234567890"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012345678901"));
		
	}
	
	@Test
	public void tag_2_824_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.824";
		int fieldIdKey = 824;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ABTHP", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234567890123"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012345678901234"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890123456789012345"));
		
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
		checkCharacterSet("ABTHP", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012345"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890123456"));
		
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
		checkCharacterSet("ABTHP", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012345"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890123456"));
		
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
		checkCharacterSet("ABTHP", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012345"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890123456"));
		
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
		checkCharacterSet("ABTHP", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012345"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890123456"));
		
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
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(999, 'A')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1000, 'A')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1001, 'A')));
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123"));
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12"));
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123456789012345678901234"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234567890123456789012345"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345678901234567890123456"));
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "123"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "1234"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "12345"));
		
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
		checkCharacterSet("AS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, 'A')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(50, 'A')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(51, 'A')));
		
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
		checkCharacterSet("NP", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "0"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "000000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000000"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "000.00"));
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "000000"));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "0000000"));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, "00000000"));
		
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
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, 'A')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(49, 'A')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(50, 'A')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(51, 'A')));
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, 'A')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, 'A')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, 'A')));
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		
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
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(299, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(300, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(301, '1')));
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(11, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(12, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(13, '1')));
		
	}
	
	@Test
	public void tag_2_887() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.887";
		int fieldIdKey = 887;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(16, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(17, '1')));
		
	}
	
	@Test
	public void tag_2_888() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.888";
		int fieldIdKey = 888;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(16, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(17, '1')));
		
	}
	
	@Test
	public void tag_2_889() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.889";
		int fieldIdKey = 889;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		
	}
	
	@Test
	public void tag_2_891() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.891";
		int fieldIdKey = 891;
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
	public void tag_2_892_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.892";
		int fieldIdKey = 892;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		
	}
	
	@Test
	public void tag_2_892_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.892";
		int fieldIdKey = 892;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(31, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(32, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(33, '1')));
		
	}
	
	@Test
	public void tag_2_893_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.893";
		int fieldIdKey = 893;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
	}
	
	@Test
	public void tag_2_893_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.893";
		int fieldIdKey = 893;
		int recordType = 2;
		int subfieldIdKey = 2;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
	}
	
	@Test
	public void tag_2_893_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.893";
		int fieldIdKey = 893;
		int recordType = 2;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, 'A')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(49, 'B')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(50, 'C')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(51, 'D')));
	
	}
	
	@Test
	public void tag_2_894() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.894";
		int fieldIdKey = 894;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, 'A')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(499, 'B')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(500, 'C')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(501, 'D')));
	}
	
	@Test
	public void tag_2_8005() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8005";
		int fieldIdKey = 8005;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, 'A')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, 'B')));
	}
	
	@Test
	public void tag_2_8022() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8022";
		int fieldIdKey = 8022;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(7, 'A')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(8, 'B')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(9, 'B')));
	}
	
	@Test
	public void tag_2_8038() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8038";
		int fieldIdKey = 8038;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(7, 'A')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(8, 'B')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(9, 'B')));
	}
	
	@Test
	public void tag_2_8084_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8084";
		int fieldIdKey = 8084;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
	}
	
	@Test
	public void tag_2_8084_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8084";
		int fieldIdKey = 8084;
		int recordType = 2;
		int subfieldIdKey = 2;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		
	}
	
	@Test
	public void tag_2_8084_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8084";
		int fieldIdKey = 8084;
		int recordType = 2;
		int subfieldIdKey = 3;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(7, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(8, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(9, '1')));
		
	}
	
	@Test
	public void tag_2_8900() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8900";
		int fieldIdKey = 8900;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		
	}
	
	@Test
	public void tag_2_8903_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8903";
		int fieldIdKey = 8903;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(39, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(40, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(41, '1')));
		
	}
	
	@Test
	public void tag_2_8903_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8903";
		int fieldIdKey = 8903;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(49, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(50, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(51, '1')));
		
	}
	
	@Test
	public void tag_2_8903_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8903";
		int fieldIdKey = 8903;
		int recordType = 2;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(49, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(50, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(51, '1')));
		
	}
	
	@Test
	public void tag_2_8903_4() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8903";
		int fieldIdKey = 8903;
		int recordType = 2;
		int subfieldIdKey = 4;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(49, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(50, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(51, '1')));
		
	}
	
	@Test
	public void tag_2_8903_5() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8903";
		int fieldIdKey = 8903;
		int recordType = 2;
		int subfieldIdKey = 5;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(34, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(35, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(36, '1')));
		
	}
	
	@Test
	public void tag_2_8903_6() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8903";
		int fieldIdKey = 8903;
		int recordType = 2;
		int subfieldIdKey = 6;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		
	}
	
	@Test
	public void tag_2_8903_7() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8903";
		int fieldIdKey = 8903;
		int recordType = 2;
		int subfieldIdKey = 7;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANBH", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(9, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(10, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(11, '1')));
		
	}
	
	@Test
	public void tag_2_8903_8() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8903";
		int fieldIdKey = 8903;
		int recordType = 2;
		int subfieldIdKey = 8;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("AS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(34, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(35, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(36, '1')));
		
	}
	
	@Test
	public void tag_2_8903_9() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8903";
		int fieldIdKey = 8903;
		int recordType = 2;
		int subfieldIdKey = 9;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		
	}
	
	@Test
	public void tag_2_8908() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8908";
		int fieldIdKey = 8908;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(31, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(32, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(33, '1')));
		
	}
	
	@Test
	public void tag_2_8910() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8910";
		int fieldIdKey = 8910;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		
	}
	
	@Test
	public void tag_2_8911() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8911";
		int fieldIdKey = 8911;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		
	}
	
	@Test
	public void tag_2_8931() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8931";
		int fieldIdKey = 8931;
		int recordType = 2;
		int subfieldIdKey = 1;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(49, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(50, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(51, '1')));
		
	}
	
	@Test
	public void tag_2_8934_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8934";
		int fieldIdKey = 8934;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		
	}
	
	@Test
	public void tag_2_8934_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8934";
		int fieldIdKey = 8934;
		int recordType = 2;
		int subfieldIdKey = 2;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(19, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(20, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(21, '1')));
		
	}
	
	@Test
	public void tag_2_8934_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8934";
		int fieldIdKey = 8934;
		int recordType = 2;
		int subfieldIdKey = 3;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		
	}
	
	@Test
	public void tag_2_8938() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8938";
		int fieldIdKey = 8938;
		int recordType = 2;
		int subfieldIdKey = 1;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(49, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(50, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(51, '1')));
		
	}
	
	@Test
	public void tag_2_8939() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8939";
		int fieldIdKey = 8939;
		int recordType = 2;
		int subfieldIdKey = 1;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(19, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(20, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(21, '1')));
		
	}
	
	@Test
	public void tag_2_8943() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8943";
		int fieldIdKey = 8943;
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
	public void tag_2_8944_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8944";
		int fieldIdKey = 8944;
		int recordType = 2;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(99, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(100, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(101, '1')));
		
	}
	
	@Test
	public void tag_2_8944_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8944";
		int fieldIdKey = 8944;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(99, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(100, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(101, '1')));
		
	}
	
	@Test
	public void tag_2_8944_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "2.8944";
		int fieldIdKey = 8944;
		int recordType = 2;
		int subfieldIdKey = 3;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(99, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(100, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(101, '1')));
		
	}
	
	@Test
	public void tag_10_001() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.001";
		int recordType = 10;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(7, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(8, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(9, '1')));
		
	}
	
	@Test
	public void tag_10_002() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.002";
		int recordType = 10;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		
	}
	
	@Test
	public void tag_10_003() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.003";
		int recordType = 10;
		int fieldIdKey = 3;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(7, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(8, '1')));
		
	}
	
	@Test
	public void tag_10_004() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.004";
		int recordType = 10;
		int fieldIdKey = 4;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(7, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(8, '1')));
		
	}
	
	@Test
	public void tag_10_005() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.005";
		int recordType = 10;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(7, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(8, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(9, '1')));
		
	}
	
	@Test
	public void tag_10_006() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.006";
		int recordType = 10;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		
	}
	
	@Test
	public void tag_10_007() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.007";
		int recordType = 10;
		int fieldIdKey = 7;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		
	}
	
	@Test
	public void tag_10_008() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.008";
		int recordType = 10;
		int fieldIdKey = 8;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		
	}
	
	@Test
	public void tag_10_009() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.009";
		int recordType = 10;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		
	}
	
	@Test
	public void tag_10_010() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.010";
		int recordType = 10;
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
	public void tag_10_011() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.011";
		int recordType = 10;
		int fieldIdKey = 11;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(7, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(8, '1')));
		
	}
	
	@Test
	public void tag_10_012() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.012";
		int recordType = 10;
		int fieldIdKey = 12;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		
	}
	
	@Test
	public void tag_10_020() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.020";
		int recordType = 10;
		int fieldIdKey = 20;
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
	public void tag_10_021() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.021";
		int recordType = 10;
		int fieldIdKey = 21;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("NH", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		
	}
	
	@Test
	public void tag_10_200() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.200";
		int recordType = 10;
		int fieldIdKey = 200;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("ANS", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(499, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(500, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(501, '1')));
		
	}
	
	@Test
	public void tag_10_999() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "10.999";
		int recordType = 10;
		int fieldIdKey = 999;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("Y", validator,transactionType,  tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5000001, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(50000002, '1')));
		
	}
	
	@Test
	public void tag_14_001() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.001";
		int recordType = 14;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(7, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(8, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(9, '1')));
		
	}
	
	@Test
	public void tag_14_002() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.002";
		int recordType = 14;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		
	}
	
	@Test
	public void tag_14_003() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.003";
		int recordType = 14;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		
	}
	
	@Test
	public void tag_14_004() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.004";
		int recordType = 14;
		int fieldIdKey = 4;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(7, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(8, '1')));
		
	}
	
	@Test
	public void tag_14_005() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.005";
		int recordType = 14;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(7, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(8, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(9, '1')));
		
	}
	
	@Test
	public void tag_14_006() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.006";
		int recordType = 14;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		
	}
	
	@Test
	public void tag_14_007() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.007";
		int recordType = 14;
		int fieldIdKey = 7;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		
	}
	
	@Test
	public void tag_14_008() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.008";
		int recordType = 14;
		int fieldIdKey = 8;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		
	}
	
	@Test
	public void tag_14_009() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.009";
		int recordType = 14;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		
	}
	
	@Test
	public void tag_14_010() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.010";
		int recordType = 14;
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
	public void tag_14_011() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.011";
		int recordType = 14;
		int fieldIdKey = 11;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, ""));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(6, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(7, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(8, '1')));
		
	}
	
	@Test
	public void tag_14_012() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.012";
		int recordType = 14;
		int fieldIdKey = 12;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		
	}
	
	@Test
	public void tag_14_013() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.013";
		int recordType = 14;
		int fieldIdKey = 13;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		
	}
	
	@Test
	public void tag_14_021_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.021";
		int recordType = 14;
		int fieldIdKey = 21;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		
	}
	
	@Test
	public void tag_14_021_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.021";
		int recordType = 14;
		int fieldIdKey = 21;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		
	}
	
	@Test
	public void tag_14_021_3() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.021";
		int recordType = 14;
		int fieldIdKey = 21;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		
	}
	
	@Test
	public void tag_14_021_4() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.021";
		int recordType = 14;
		int fieldIdKey = 21;
		int subfieldIdKey = 4;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		
	}
	
	@Test
	public void tag_14_021_5() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.021";
		int recordType = 14;
		int fieldIdKey = 21;
		int subfieldIdKey = 5;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(4, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(5, '1')));
		
	}
	
	@Test
	public void tag_14_022_1() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.022";
		int recordType = 14;
		int fieldIdKey = 22;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		
	}
	
	@Test
	public void tag_14_022_2() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.022";
		int recordType = 14;
		int fieldIdKey = 22;
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
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		
	}
	
	@Test
	public void tag_14_200() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.200";
		int recordType = 14;
		int fieldIdKey = 200;
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(3, '1')));
		
	}
	
	@Test
	public void tag_14_999() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setAnsiNistValidator((new ValidationDeserializerImpl())
				.deserialize(AnsiNistValidator.validation_1_7_7f));
		AnsiNistValidator validator = packet.getAnsiNistValidator();
		
		String tag = "14.999";
		int recordType = 14;
		int fieldIdKey = 999;
		int subfieldIdKey = 1;
		int itemIdKey = 1;
		
		// Test CharacterSet
		log.info("Check CharacterSet");
		
		packet.deleteAll();
		checkCharacterSet("Y", validator, transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
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
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(1, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2, '1')));
		Assertions.assertTrue(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2000000000, '1')));
		Assertions.assertFalse(validator.validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, buildString(2000000001, '1')));
		
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