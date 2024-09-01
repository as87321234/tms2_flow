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
							recordTag.getTag_min_occurrence(),
							recordTag.getTag_max_occurrence(),
							occurrence.getOccurrence_tag_name(),
							occurrence.getCharacter_set(),
							occurrence.getCondition(),
							occurrence.getField_size_min(),
							occurrence.getField_size_max(),
							occurrence.getOccurrence_min(),
							occurrence.getOccurrence_max()
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
		
		return valid && lengthValid;
	}

	@Override
	public boolean validateOccurrence(AnsiNistPacket packet, String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value) {
		
		String key = String.format("%s:%s.%s.%s", tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		List<List<Integer>> keyList = packet.getKeyList();
		int rectype = Integer.parseInt(tag.split("\\.")[0]);
		
		int occurrenceCnt = 0;
		for (int i = 0; i < keyList.size(); i++) {
			List<Integer> keyDetail = keyList.get(0);
			
			if (keyDetail.get(0) == rectype
					&& keyDetail.get(2) == fieldIdKey
					&& keyDetail.get(3) == subfieldIdKey
					&& keyDetail.get(4) == itemIdKey) {
				
				occurrenceCnt++;
		
			}
		}
		
		// Check CharacterSet
		RecordTag recordTag = findTag(tag);
		Occurrence occurrence = recordTag.occurrence.get(subfieldIdKey - 1);
		
		int occurrenceMinSize = occurrence.getOccurrence_min();
		int occurrenceMaxSize = occurrence.getOccurrence_max();
		
		boolean valid =  occurrenceCnt >= occurrenceMinSize && occurrenceCnt <= occurrenceMaxSize;
		
		if (valid) {
			log.info(String.format("validateCondition key: %s value: %s - length: %s ", key, value, valid));
		} else {
			log.error(String.format("validateCondition key: %s value: %s - length: %s ", key, value, valid));
		}
		
		return valid;
	}
	
	@Override
	public boolean validateCondition(AnsiNistPacket packet, String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value) {
		
		String key = String.format("%s:%s.%s.%s", tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		List<List<Integer>> keyList = packet.getKeyList();
		int rectype = Integer.parseInt(tag.split("\\.")[0]);
		
		int occurrenceCnt = 0;
		for (int i = 0; i < keyList.size(); i++) {
			List<Integer> keyDetail = keyList.get(0);
			
			if (keyDetail.get(0) == rectype
					&& keyDetail.get(2) == fieldIdKey
					&& keyDetail.get(3) == subfieldIdKey
					&& keyDetail.get(4) == itemIdKey) {
				
				occurrenceCnt++;
				
			}
		}
		
		// Check CharacterSet
		RecordTag recordTag = findTag(tag);
		Occurrence occurrence = recordTag.occurrence.get(subfieldIdKey - 1);
		
		String occurrenceCondition = occurrence.getCondition();

		boolean valid = false;
		
		if (occurrenceCnt == 1 && Objects.equals(occurrenceCondition, "M")) {
			valid = true;
		}
		
		if (occurrenceCnt == 0 && Objects.equals(occurrenceCondition, "M")) {
			valid = false;
		}
		
		if (valid) {
			log.info(String.format("validateCondition key: %s value: %s - length: %s ", key, value, valid));
		} else {
			log.error(String.format("validateCondition key: %s value: %s - length: %s ", key, value, valid));
		}
		
		return valid;
	}


	@Override
	public boolean validateFieldLength(String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value) {
		
		String key = String.format("%s:%s.%s.%s", tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Check CharacterSet
		
		RecordTag recordTag = findTag(tag);
		Occurrence occurrence = recordTag.occurrence.get(subfieldIdKey - 1);
		
		int fieldMinSize = occurrence.getField_size_min();
		int fieldMaxSize = occurrence.getField_size_max();
		
		boolean valid =  value.length() >= fieldMinSize && value.length() <= fieldMaxSize;
		
		if (valid) {
			log.info(String.format("validateFieldLength key: %s value: %s - length: %s ", key, value, valid));
		} else {
			log.error(String.format("validateFieldLength key: %s value: %s - length: %s ", key, value, valid));
		}
		
		return valid;
	}
	@Override
	public boolean validateCharacterSet(String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value) {
		
		String key = String.format("%s:%s.%s.%s", tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		// Check CharacterSet
		RecordTag recordTag = findTag(tag);
		Occurrence occurrence = recordTag.occurrence.get(subfieldIdKey - 1);
		
		List<String> characterSetList = occurrence.getCharacter_set();
		
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
			
		}
		
		regex = new StringBuilder(regex.substring(0, regex.length() - 1));
		String regexStr = "[" + regex.toString() + "]+";
		boolean valid = value.matches(regexStr) ? true : false;
		
		if (valid) {
			log.info(String.format("validateCharacterSet key: %s value: %s - matches: %s ", key, value, valid));
		} else {
			log.error(String.format("validateCharacterSet key: %s value: %s - matches: %s ", key, value, valid));
		}
		
		return valid;
	}
	
}
