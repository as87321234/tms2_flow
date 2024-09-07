package ca.mgis.ansinist2k;

import ca.mgis.ansinist2k.validation.Occurrence;
import ca.mgis.ansinist2k.validation.RecordTag;
import ca.mgis.ansinist2k.validation.RecordType;
import ca.mgis.ansinist2k.validation.TransactionType;
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
	public String byte_regx;
	
	
	public ArrayList<TransactionType> transaction_type;
	
	public void dumpValidator() {
		
		log.info(String.format("ALPHA REGX: %s, NUMERIC REGX: %s, SPECIAL REGX %s, PERIOD REGX %s, CRLF REGX %s"));
		
		for (TransactionType transactionType : transaction_type) {
			
			for (RecordType recordType : transactionType.getRecord_type()) {
				
				for (RecordTag recordTag : recordType.getRecord_tag()) {
					
					for (Occurrence occurrence : recordTag.getOccurrence()) {
						
						log.info(String.format("Transaction Type: %s Record Type: %s, Record Tag: %s: %s %s [%s,%s] %s, %s, %s, %s, %s , %s, %s ",
								transactionType.getTransactionType(),
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
	}
	
	public ArrayList<RecordType> findRecordTypeFrom(String transactionTypeId) {
		
		for (TransactionType transactionType : transaction_type) {
		
			if (transactionType.getTransactionType().equals(transactionTypeId)) {
				
				return transactionType.getRecord_type();
			}
			
		}
		
		return null;
		
	}
	
	public RecordTag findTag(String transactionType, String tag) {
		
		for (RecordType recordType : findRecordTypeFrom(transactionType)) {
			
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
	public boolean validate(String transactionType, String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value) {
		RecordTag recordTag = findTag(transactionType, tag);
		
		log.debug(String.format("recordTag: %s ", recordTag));
		
		boolean valid = validateCharacterSet(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, value);
		boolean lengthValid = validateFieldLength(transactionType, tag, fieldIdKey, subfieldIdKey, itemIdKey, value);
		
		log.warn("Not fully implemented validation ...");
		return valid && lengthValid;
	}
	
	@Override
	public boolean validateOccurrence(String transactionType, AnsiNistPacket packet, String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value) {
		
		String key = String.format("%s:%s.%s.%s", tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		RecordTag recordTag = findTag(transactionType, tag);
		List<List<Integer>> keyList = packet.getKeyList();
		
		int rectype = Integer.parseInt(tag.split("\\.")[0]);
		
		if (subfieldIdKey == 1 && itemIdKey == 1 && recordTag.getOccurrence().size() == 0) {
			
			int occurrenceCnt = countOccurrence(keyList, rectype, fieldIdKey, subfieldIdKey, itemIdKey);
			
			int fieldOccMin = recordTag.getField_occ_min();
			int fieldOccMax = recordTag.getField_occ_max();
			
			boolean valid = ((recordTag.condition.matches("O|C") && occurrenceCnt >= 0) ||
					(occurrenceCnt >= 1 && recordTag.condition.equals("M") ));
			
			if (valid) {
				log.info(String.format("validateCondition key: %s - length: %s ", key, valid));
			} else {
				log.error(String.format("validateCondition key: %s - length: %s ", key, valid));
			}

			return valid;
			
		} else {
			
			int occurrenceCnt = countOccurrence(keyList, rectype, fieldIdKey, subfieldIdKey, itemIdKey);;
			
			Occurrence occurrence = recordTag.getOccurrence().get(subfieldIdKey - 1);
			int fieldOccMin = occurrence.getField_occ_min();
			int fieldOccMax = occurrence.getField_occ_max();
			
			boolean valid = ((recordTag.condition.matches("O|C") && occurrenceCnt == 0) ||
					((occurrenceCnt >= 0 && occurrence.condition.matches("C|O") )) ||
					(occurrenceCnt >= 1 && occurrence.getCondition().equals("M") ||
							(occurrenceCnt >= 0 && occurrence.getCondition().equals("O|C"))));
			
			if (valid) {
				log.info(String.format("validateCondition key: %s - length: %s ", key, valid));
			} else {
				log.error(String.format("validateCondition key: %s - length: %s ", key, valid));
			}
			
			return valid;
			
		}
		
	}
	
	private int countOccurrence(List<List<Integer>> keyList, int rectype, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey) {
		
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
	
	@Override
	public boolean validateRegexPattern(String transactionType, String tag, Integer fieldIdKey,
									  Integer subfieldIdKey, Integer itemIdKey, String value) {
		
		String key = String.format("%s:%s.%s.%s", tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		RecordTag recordTag = findTag(transactionType, tag);
		
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
	public String getAlphaCharacterSet() {
		return "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	}
	
	@Override
	public String getNumericCharacterSet() {
		return "0123456789";
	}
	
	@Override
	public String getSpecialCharacterSet() {
		return " #$&’()*,-.";
	}
	
	@Override
	public String getCrlfCharacterSet() {
		return "\r\n";
	}
	
	@Override
	public String getPeriodCharacterSet() {
		return ".";
	}
	
	@Override
	public String getHyphenCharacterSet() {
		return "-";
	}
	
	@Override
	public String getSpaceCharacterSet() {
		return " ";
	}
	
	@Override
	public String getApostropheCharacterSet() {
		return "’";
	}
	
	@Override
	public String getByteCharacterSet() {
		return ".";
	}
	
	@Override
	public int getOccurrenceCount(AnsiNistPacket packet, int rectype, int fieldIdKey, int subfieldIdKey, int itemIdKey) {
		
		List<List<Integer>> keyList = packet.getKeyList();
		
		return countOccurrence(keyList, rectype, fieldIdKey, subfieldIdKey, itemIdKey);
		
	}
	
	@Override
	public boolean validateCondition(String transactionType, AnsiNistPacket packet, String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value) {
		
		
		String key = String.format("%s:%s.%s.%s", tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		RecordTag recordTag = findTag(transactionType, tag);
		List<List<Integer>> keyList = packet.getKeyList();
		
		int rectype = Integer.parseInt(tag.split("\\.")[0]);
		
		if (subfieldIdKey == 1 && itemIdKey == 1 && recordTag.getOccurrence().size() == 0) {
			
			int occurrenceCnt = countOccurrence(keyList, rectype, fieldIdKey, subfieldIdKey, itemIdKey);
			
			int fieldOccMin = recordTag.getField_occ_min();
			int fieldOccMax = recordTag.getField_occ_max();
			
			boolean valid = ((recordTag.condition.matches("O|C") && occurrenceCnt >= 0) ||
					(occurrenceCnt >= 1 && recordTag.condition.equals("M") ));
			
			if (valid) {
				log.info(String.format("validateCondition key: %s - length: %s ", key, valid));
			} else {
				log.error(String.format("validateCondition key: %s - length: %s ", key, valid));
			}
			
			return valid;
			
		} else {
			
			int occurrenceCnt = countOccurrence(keyList, rectype, fieldIdKey, subfieldIdKey, itemIdKey);;
			
			Occurrence occurrence = recordTag.getOccurrence().get(subfieldIdKey - 1);
			
			boolean valid = ((recordTag.condition.matches("O|C") && occurrenceCnt >= 0) ||
					(occurrenceCnt >= 1 && occurrence.getCondition().equals("M") ||
							(occurrenceCnt >= 0 && occurrence.getCondition().matches("O|C"))));
			
			if (valid) {
				log.info(String.format("validateCondition key: %s - length: %s ", key, valid));
			} else {
				log.error(String.format("validateCondition key: %s - length: %s ", key, valid));
			}
			
			return valid;
			
		}
		
	}

	@Override
	public boolean validateFieldLength(String transactionType, String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value) {
		
		String key = String.format("%s:%s.%s.%s", tag, fieldIdKey, subfieldIdKey, itemIdKey);

		boolean valid;
		
		RecordTag recordTag = findTag(transactionType, tag);
		
		if (subfieldIdKey == 1 && itemIdKey == 1 && recordTag.getOccurrence().size() == 0) {
			
			int fieldMinSize = recordTag.getField_min_size();
			int fieldMaxSize = recordTag.getField_max_size();
			
			valid = value.length() >= fieldMinSize && value.length() <= fieldMaxSize;
			
			if (valid) {
				if (!tag.matches(".*999.*")) {
					log.info(String.format("validateFieldLength key: %s value: %s - length: %s ", key, value, valid));
				} else {
					log.info(String.format("validateFieldLength key: %s value: %s - length: %s ", key, "{Image}", valid));
				}
			} else {
				if (!tag.matches(".*999.*")) {
					log.error(String.format("validateFieldLength key: %s value: %s - length: %s ", key, value, valid));
				} else {
					log.info(String.format("validateFieldLength key: %s value: %s - length: %s ", key, "{Image}", valid));
				}
			}
			
			return valid;
			
		} else {
			
			Occurrence occurrence = recordTag.getOccurrence().get(subfieldIdKey - 1);
			
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
	public boolean validateCharacterSet(String transactionType, String tag, Integer fieldIdKey, Integer subfieldIdKey, Integer itemIdKey, String value) {
		
		String key = String.format("%s:%s.%s.%s", tag, fieldIdKey, subfieldIdKey, itemIdKey);
		
		RecordTag recordTag = findTag(transactionType, tag);
		StringBuilder regex = new StringBuilder();
		
		if (subfieldIdKey == 1 && itemIdKey == 1 && recordTag.getOccurrence().size() == 0) {
			
			String regexStr = null;
			try {
				regexStr = buildRegex(recordTag.getCharacter_set());
			} catch (Exception e) {

				log.error(e.getMessage());
			}
			
			boolean valid = value.matches(regexStr) ? true : false;
			
			if (valid) {
				log.info(String.format("validateCharacterSet key: %s value: %s - matches: %s ", key, value, valid));
			} else {
				log.error(String.format("validateCharacterSet key: %s value: %s - matches: %s ", key, value, valid));
			}
			
			return valid;
			
		} else {
			
			Occurrence occurrence = recordTag.getOccurrence().get(subfieldIdKey - 1);
			
			String regexStr = null;
			try {
				regexStr = buildRegex(occurrence.getCharacter_set());
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			
			boolean valid = value.matches(regexStr) ? true : false;
			
			if (valid) {
				log.info(String.format("validateCharacterSet key: %s value: %s - matches: %s ", key, value, valid));
			} else {
				log.error(String.format("validateCharacterSet key: %s value: %s - matches: %s ", key, value, valid));
			}
			
			return valid;
			
		}
	}
	
	private String buildRegex(List<String> characterSetList) throws Exception {
		
		StringBuilder regex = new StringBuilder();
		
		for (String characterSet : characterSetList) {
			
			if (characterSet.equals("numeric")) {
				regex.append(numeric_regx).append(",");
			} else if (characterSet.equals("alpha")) {
				regex.append(alpha_regx).append(",");
			} else if (characterSet.equals("crlf")) {
				regex.append(crlf_regx).append(",");
			} else if (characterSet.equals("period")) {
				regex.append(period_regx).append(",");
			}else if (characterSet.equals("special")) {
				regex.append(special_regx).append(",");
			} else if (characterSet.equals("space")) {
				regex.append(space_regx).append(",");
			} else if (characterSet.equals("hyphen")) {
				regex.append(hyphen_regx).append(",");
			} else if (characterSet.equals("apostrophe")) {
				regex.append(apostrophe_regx).append(",");
			} else if (characterSet.equals("byte")) {
				regex.append(byte_regx).append(",");
			} else
				throw new Exception("Invalid character set. ");
		}
		
		regex = new StringBuilder(regex.substring(0, regex.length() - 1));
		String regexStr = "[" + regex.toString() + "]+";
		
		return regexStr;
		
	}
	
}
