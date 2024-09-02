package ca.mgis.ansinist2k;

import ca.mgis.ansinist2k.validation.Occurrence;
import ca.mgis.ansinist2k.validation.RecordTag;
import ca.mgis.ansinist2k.validation.RecordType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
public class Validator177f extends AnsiNistValidator {
	
	Logger log = LoggerFactory.getLogger(Validator177f.class);
	
	public String alpha_regx;
	public String numeric_regx;
	public String special_regx;
	public String crlf_regx;
	public String period_regx;
	public String hyphen_regx;
	public String space_regx;
	public String apostrophe_regx;
	
	
	public ArrayList<RecordType> record_type;
	
	public void dumpValidator() {
		
		log.info(String.format("ALPHA REGX: %s, NUMERIC REGX: %s, SPECIAL REGX %s, PERIOD REGX %s, CRLF REGX %s"));
		
		for (RecordType recordType : record_type) {
			
			for (RecordTag recordTag : recordType.getRecord_tag()) {
				
				for (Occurrence occurrence : recordTag.getOccurrence()) {
					
					log.info(String.format("Record Type %s, Record Tag: %s: %s %s [%s,%s] %s, %s, %s, %s, %s , %s, %s ",
							recordType.getId(),
							recordTag.getId(),
							recordTag.getIdentifier(),
							recordTag.getTag_name(),
							recordTag.getField_occ_min(),
							recordTag.getField_occ_max(),
							occurrence.getTag_name(),
							occurrence.getCharacter_set(),
							occurrence.getCondition(),
							occurrence.getField_min_size(),
							occurrence.getField_max_size(),
							occurrence.getField_occ_min(),
							occurrence.getField_occ_max()
					));
					
					if (!recordTag.getNote().contentEquals(""))
						log.info(String.format("Note: %s", recordTag.getNote()));
					
				}
			}
		}
		
	}
	
	
	private RecordTag findTag(String tag) {
		
		for (RecordType recordType : record_type) {
			
			for (RecordTag recordTag : recordType.getRecord_tag()) {
				
				if (recordTag.getId().equals(tag)) {
					return recordTag;
				}
			}
		}
		
		log.error(String.format("Tag id: %s", tag));
		return null;
		
	}
	
	@Override
	public boolean validate(String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value) {
		RecordTag recordTag = findTag(tag);
		
		log.debug(String.format("recordTag: %s ", recordTag));
		
		boolean valid = validateCharacterSet(tag, fieldIdKey, subfieldIdKey, itemIdKey, value);
		boolean lengthValid = validateFieldLength(tag, fieldIdKey, subfieldIdKey, itemIdKey, value);
		
		log.warn("Not fully implemented validation ...");
		return valid && lengthValid;
	}

	@Override
	public boolean validateOccurrence(AnsiNistPacket packet, String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value) {
		
		String key = String.format("%s:%s.%s.%s", tag, fieldIdKey, subfieldIdKey, itemIdKey);
		RecordTag recordTag = findTag(tag);
		List<List<Integer>> keyList = packet.getKeyList();
		
		int rectype = Integer.parseInt(tag.split("\\.")[0]);
		
		if (subfieldIdKey == 1 && itemIdKey == 1 && recordTag.getOccurrence().size() == 0) {
			
			int occurrenceCnt = countOccurence(keyList, rectype, fieldIdKey, subfieldIdKey, itemIdKey);;
			
			int fieldOccMin = recordTag.getField_occ_min();
			int fieldOccMax = recordTag.getField_occ_max();
			
			boolean valid = occurrenceCnt >= fieldOccMin && occurrenceCnt <= fieldOccMax;
			
			if (valid) {
				log.info(String.format("validateCondition key: %s - length: %s ", key, valid));
			} else {
				log.error(String.format("validateCondition key: %s - length: %s ", key, valid));
			}

			return valid;
			
		} else {
			
			int occurrenceCnt = countOccurence(keyList, rectype, fieldIdKey, subfieldIdKey, itemIdKey);;
			
			int fieldOccMin = recordTag.occurrence.get(itemIdKey - 1).getField_occ_min();
			int fieldOccMax = recordTag.occurrence.get(itemIdKey - 1).getField_occ_max();
			
			boolean valid = occurrenceCnt >= fieldOccMin && occurrenceCnt <= fieldOccMax;
			
			if (valid) {
				log.info(String.format("validateCondition key: %s - length: %s ", key, valid));
			} else {
				log.error(String.format("validateCondition key: %s - length: %s ", key, valid));
			}
			
			return valid;
			
		}
		
	}
	
	private int countOccurence(List<List<Integer>> keyList, int rectype, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey) {
		
		int occurrenceCnt = 0;
		
		for (int i = 0; i < keyList.size(); i++) {
			
			List<Integer> keyDetail = keyList.get(i);
			
			if (keyDetail.get(0) == rectype
					&& Objects.equals(keyDetail.get(2), fieldIdKey)
					&& Objects.equals(keyDetail.get(3), subfieldIdKey)
					&& Objects.equals(keyDetail.get(4), itemIdKey)) {
				
				occurrenceCnt++;
				
			}
			
		}
		
		return occurrenceCnt;
	}
	
	private boolean validateCondition(List<List<Integer>> keyList, int rectype, Integer fieldIdKey,
									  Integer subfieldIdKey, Integer itemIdKey, String condition) {
		
		int occurrenceCnt = 0;
		
		for (int i = 0; i < keyList.size(); i++) {
			
			List<Integer> keyDetail = keyList.get(i);
			
			if (keyDetail.get(0) == rectype
					&& Objects.equals(keyDetail.get(2), fieldIdKey)
					&& Objects.equals(keyDetail.get(3), subfieldIdKey)
					&& Objects.equals(keyDetail.get(4), itemIdKey)) {
				
				occurrenceCnt++;
				
			}
			
		}
		
		boolean valid = false;
		
		// Check if field is mandatory
		if (Objects.equals(condition, "M")) {
			valid = occurrenceCnt >= 1;
		}
		
		// Check if field is optional
		if (Objects.equals(condition, "O") || Objects.equals(condition, "C")) {
			valid = occurrenceCnt >= 0;
		}
		
		if (valid) {
			log.info(String.format("validateCondition  %s ", valid));
		} else {
			log.info(String.format("validateCondition  %s ", valid));
		}
		
		return valid;
		
	}
	
	@Override
	public boolean validateRegexPattern(String tag, Integer fieldIdKey,
									  Integer subfieldIdKey, Integer itemIdKey, String value) {
		
		String key = String.format("%s:%s.%s.%s", tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		RecordTag recordTag = findTag(tag);
		
		String regexPattern =	recordTag.getRegexPattern();
		
		boolean valid = value.matches(regexPattern);
		
		if (valid) {
			log.info(String.format("validate value: %s, regex:  %s ",value, regexPattern));
		} else {
			log.error(String.format("failed validate value: %s, regex:  %s ",value, regexPattern));
		}
		
		return valid;
		
	}
	
	@Override
	public boolean validateCondition(AnsiNistPacket packet, String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value) {
		
		String key = String.format("%s:%s.%s.%s", tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		List<List<Integer>> keyList = packet.getKeyList();
		int rectype = Integer.parseInt(tag.split("\\.")[0]);
		RecordTag recordTag = findTag(tag);
		
		if (subfieldIdKey == 1 && itemIdKey == 1 && recordTag.getOccurrence().size() == 0) {
			
			return validateCondition(keyList, rectype, fieldIdKey, subfieldIdKey, itemIdKey , recordTag.getCondition() );
			
		} else {
			
			return validateCondition(keyList, rectype, fieldIdKey, subfieldIdKey, itemIdKey , recordTag.getOccurrence().get(itemIdKey - 1).getCondition() );
			
		}
		
	}

	@Override
	public boolean validateFieldLength(String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value) {
		
		String key = String.format("%s:%s.%s.%s", tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		boolean valid;
		
		RecordTag recordTag = findTag(tag);
		
		if (subfieldIdKey == 1 && itemIdKey == 1 && recordTag.getOccurrence().size() == 0) {
			
			int fieldMinSize = recordTag.getField_min_size();
			int fieldMaxSize = recordTag.getField_max_size();
			
			valid = value.length() >= fieldMinSize && value.length() <= fieldMaxSize;
			
			if (valid) {
				log.info(String.format("validateFieldLength key: %s value: %s - length: %s ", key, value, valid));
			} else {
				log.error(String.format("validateFieldLength key: %s value: %s - length: %s ", key, value, valid));
			}
			
			return valid;
			
		} else {
			
			Occurrence occurrence = recordTag.getOccurrence().get(itemIdKey - 1);
			
			int fieldMinSize = occurrence.getField_min_size();
			int fieldMaxSize = occurrence.getField_max_size();
			
			valid = value.length() >= fieldMinSize && value.length() <= fieldMaxSize;
			
			if (valid) {
				log.info(String.format("validateFieldLength key: %s value: %s - length: %s ", key, value, valid));
			} else {
				log.error(String.format("validateFieldLength key: %s value: %s - length: %s ", key, value, valid));
			}
			
			return valid;
			
		}
		
	}
	
	@Override
	public boolean validateCharacterSet(String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value) {
		
		String key = String.format("%s:%s.%s.%s", tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		RecordTag recordTag = findTag(tag);
		StringBuilder regex = new StringBuilder();
		
		if (subfieldIdKey == 1 && itemIdKey == 1 && recordTag.getOccurrence().size() == 0) {
			
			String regexStr = buildRegex(recordTag.getCharacter_set());
			
			boolean valid = value.matches(regexStr) ? true : false;
			
			if (valid) {
				log.info(String.format("validateCharacterSet key: %s value: %s - matches: %s ", key, value, valid));
			} else {
				log.error(String.format("validateCharacterSet key: %s value: %s - matches: %s ", key, value, valid));
			}
			
			return valid;
			
		} else {
			
			Occurrence occurrence = recordTag.getOccurrence().get(itemIdKey - 1);
			
			String regexStr = buildRegex(occurrence.getCharacter_set());
			
			boolean valid = value.matches(regexStr) ? true : false;
			
			if (valid) {
				log.info(String.format("validateCharacterSet key: %s value: %s - matches: %s ", key, value, valid));
			} else {
				log.error(String.format("validateCharacterSet key: %s value: %s - matches: %s ", key, value, valid));
			}
			
			return valid;
			
		}
	}
	
	private String buildRegex(List<String> characterSetList) {
		
		StringBuilder regex = new StringBuilder();
		
		for (String characterSet : characterSetList) {
			
			if (characterSet.equals("numeric")) {
				regex.append(numeric_regx).append(",");
			}
			
			if (characterSet.equals("alpha")) {
				regex.append(alpha_regx).append(",");
			}
			
			if (characterSet.equals("crlf")) {
				regex.append(crlf_regx).append(",");
			}
			
			if (characterSet.equals("period")) {
				regex.append(period_regx).append(",");
			}
			
			if (characterSet.equals("special")) {
				regex.append(special_regx).append(",");
			}
			
			if (characterSet.equals("space")) {
				regex.append(space_regx).append(",");
			}
			
			if (characterSet.equals("apostrophe")) {
				regex.append(apostrophe_regx).append(",");
			}
			
		}
		
		regex = new StringBuilder(regex.substring(0, regex.length() - 1));
		String regexStr = "[" + regex.toString() + "]+";
		
		return regexStr;
		
	}
	
}
