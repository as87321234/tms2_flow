package ca.mgis.ansinist2k;

import ca.mgis.ansinist2k.validation.RecordTag;
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
	static public String space_regx;
	static public String hyphen_regx;
	
	public static String validation_1_7_7f = "/Users/stlouisa/IdeaProjects/tms2_flow/src/main/resources/validation/validation-1_7_7F.json";
	
	public abstract boolean validate(String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKEy, String value);
	
	public abstract boolean validateCharacterSet(String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKEy, String value);
	
	public abstract boolean validateFieldLength(String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value);
	
	public abstract boolean validateCondition(AnsiNistPacket packet, String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value);
	
	public abstract boolean validateOccurrence(AnsiNistPacket packet, String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value);
	
	public abstract boolean validateRegexPattern(String tag, Integer fieldIdKey,
												 Integer subfieldIdKey, Integer itemIdKey, String value);
	
	public abstract String getAlphaCharacterSet();
	
	public abstract String getNumericCharacterSet();
	
	public abstract String getSpecialCharacterSet();
	
	public abstract String getCrlfCharacterSet();
	
	public abstract String getPeriodCharacterSet();
	
	public abstract String getHyphenCharacterSet();
	
	public abstract String getSpaceCharacterSet();
	
	public abstract String getApostropheCharacterSet();
	
	public abstract int getOccurrenceCount(AnsiNistPacket packet, int rectype, int fieldIdKey, int subfieldIdKey, int itemIdKey);
	
	public abstract RecordTag findTag(String tag);
	
}