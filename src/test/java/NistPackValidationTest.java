import ca.mgis.ansinist2k.AnsiNistPacket;
import ca.mgis.ansinist2k.Validator177f;
import junit.framework.TestCase;

public class NistPackValidationTest extends TestCase {
	
	public void testNistPackValidation_1_001() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.setValidator(new Validator177f());
		
		packet.createItem("300", 1,1,1,1,1);
		String value = packet.findItem(1,1,1,1,1);
		assertEquals("300", value);
		
		packet.validate();
		
	}
	public void testNistPackValidation_1_002() throws Exception {
		
		AnsiNistPacket packet = new AnsiNistPacket();
		packet.createItem("300", 1,1,1,1,1);
		String value = packet.findItem(1,1,1,1,1);
		assertEquals("300", value);
		
	}
	
}
