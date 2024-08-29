import ca.mgis.ansinist2k.*;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileInputStream;

public class NistPackValidationTest extends TestCase {
	
	public void testLoadEFT() throws Exception {
		
		AnsiNistPacket ansiNistPacket = new AnsiNistPacket("src/main/resources/test1.eft", new Validator177f()  );
		
		ansiNistPacket.validate();
		
	}
	
	public void test_1_001() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		
		packet.setAnsiNistValidator(ValidationDeserializer.deserialize177f());
		
		String value;
		
		packet.createItem("300", 1,1,1,1,1);
		value = packet.findItem(1,1,1,1,1);
		assertEquals("300", value);
		
		packet.validate();
		
	}
	
	public void test_1_002() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket(new AnsiNistDecoder(), new Validator177f());
		
		String value;
		
		packet.createItem("300", 1,2,1,1,1);
		value = packet.findItem(1,2,1,1,1);
		assertEquals("300", value);
		
		packet.validate();
//		System.out.println(packet.serialize().length);
	
	}
	
}
