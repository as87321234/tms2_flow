package ca.mgis.ansinist2k;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public abstract class AnsiNistValidator {
	
	static public String alpha_regx;
	static public String numeric_regx;
	static public String special_regx;
	static public String crlf_regx;
	static public String period_regx;
	
	
	public static String validation_1_7_7f = "/Users/stlouisa/IdeaProjects/tms2_flow/src/main/resources/validation/validation-1_7_7F.json";
	
	public abstract boolean validate(String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKEy, String value);
	
	public abstract boolean validateCharacterSet(String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKEy, String value);
	
	public abstract boolean validateFieldLength(String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value);
	
	public abstract boolean validateCondition(AnsiNistPacket packet, String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value);
	
	public abstract boolean validateOccurrence(AnsiNistPacket packet, String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value);
	
	public abstract boolean validateRegexPattern(String tag, Integer fieldIdKey,
												 Integer subfieldIdKey, Integer itemIdKey, String value);
}