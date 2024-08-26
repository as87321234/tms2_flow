import ca.mgis.ansinist2k.*;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileInputStream;

public class NistPackValidationTest extends TestCase {
	
	public void testLoadEFT() throws Exception {
		
		File file = new File("src/main/resources/test1.eft");
		FileInputStream fis = new FileInputStream(file);
		AnsiNistDecoder decoder = new AnsiNistDecoder(fis.readAllBytes(), new Validator177f() );
		AnsiNistPacket packet = decoder.getAnsiNistPacket();
		
		packet.setValidator(ValidationDeserializer.deserialize177f());
		
		packet.validate();
		
	}
	
	public void testNistPackValidation_1_001() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		
		packet.setValidator(ValidationDeserializer.deserialize177f());
		
		String value;
		
		packet.createItem("300", 1,1,1,1,1);
		value = packet.findItem(1,1,1,1,1);
		packet.createItem("301", 2,1,1,1,1);
		value = packet.findItem(1,1,1,1,1);
		packet.createItem("302", 10,1,1,1,1);
		value = packet.findItem(1,1,1,1,1);
		packet.createItem("303", 14,1,1,1,1);
		value = packet.findItem(1,1,1,1,1);
		packet.createItem("304", 14,2,1,1,1);
		value = packet.findItem(1,1,1,1,1);
		packet.createItem("305", 14,3,1,1,1);
		value = packet.findItem(1,1,1,1,1);
		assertEquals("305", value);
		
		packet.validate();
		
	}
	public void testNistPackValidation_1_002() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.createItem("300", 1,1,1,1,1);
		String value = packet.findItem(1,1,1,1,1);
		assertEquals("300", value);
		
	}
	
}
