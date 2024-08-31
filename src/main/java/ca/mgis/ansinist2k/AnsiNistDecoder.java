package ca.mgis.ansinist2k;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class AnsiNistDecoder {
	
	private static Logger log = LoggerFactory.getLogger(AnsiNistDecoder.class);
	
	// Information separators
	private static final byte SEP_US = 31; // 0X1F
	private static final byte SEP_RS = 30; // 0X1E
	private static final byte SEP_GS = 29; // 0x1D
	private static final byte SEP_FS = 28; // 0x1C
	private static final byte SEP_EOF = 10; // 0x0A
	
	
	private static final String SEP_US_STR = new String(new byte[]{SEP_US});
	private static final String SEP_RS_STR = new String(new byte[]{SEP_RS});
	private static final String SEP_GS_STR = new String(new byte[]{SEP_GS});
	private static final String SEP_FS_STR = new String(new byte[]{SEP_FS});
	private static final String SEP_EOF_STR = new String(new byte[]{SEP_EOF});
	
	private static final String SEP_COLN = ":";
	
	int recordPosition = 0;
	int fieldPosition = 0;
	int subFieldPosition = 0;
	int itemPosition = 0;
	
	AnsiNistPacket ansiNistPacket;
	
	/**
	 * Inject a Decoder
	 *
	 * @param ansiNistPacket
	 */
	public AnsiNistDecoder(AnsiNistPacket ansiNistPacket) {
		this.ansiNistPacket = ansiNistPacket;
	}
	
	/**
	 * Deserialize file
	 *
	 * @throws Exception
	 */
	
	public void decode() throws Exception {
		
		int prevRectype = 0;
		Integer[] recindx = new Integer[]{0};
		
		while (recordPosition < ansiNistPacket.getBuffer().length()) {
			
			Integer[] rectype = new Integer[1];
			
			Integer[] fieldId = new Integer[1];
			
			String rawRecord = nextRecord(rectype);
			
			if (prevRectype != rectype[0]) {
				recindx = new Integer[]{1};
			} else {
				recindx[0]++;
			}
			
			prevRectype = rectype[0];
			
			decodeFields(rectype, recindx, fieldId, rawRecord);
			log.info(String.format("Finish decoding record type: %s", prevRectype));
		}
		
	}
	
	/**
	 * Decode fields
	 *
	 * @param rectype
	 * @param recindx
	 * @param fieldId
	 * @param rawRecord
	 * @throws Exception
	 */
	private void decodeFields(Integer[] rectype, Integer[] recindx, Integer[] fieldId, String rawRecord)
			throws Exception {
		
		fieldPosition = 0;
		
		while (fieldPosition < rawRecord.length()) {
			
			Integer[] subfieldId = {0};
			
			String rawField = nextField(rectype, recindx, fieldId, rawRecord);
			
			decodeSubFields(rectype, recindx, fieldId, subfieldId, rawField);
			
		}
		
	}
	
	/**
	 * Decode subfields
	 *
	 * @param rectype
	 * @param recindx
	 * @param fieldId
	 * @param subFieldId
	 * @param rawField
	 * @throws Exception
	 */
	private void decodeSubFields(Integer[] rectype, Integer[] recindx, Integer[] fieldId, Integer[] subFieldId,
								 String rawField) throws Exception {
		
		subFieldPosition = 0;
		
		subFieldId[0] = 1;
		Integer[] itemId = {0};
		
		while (subFieldPosition < rawField.length()) {
			
			String rawSubfield = nextSubfield(rectype, recindx, fieldId, subFieldId, rawField);
			
			decodeItem(rectype, recindx, fieldId, subFieldId, itemId, rawSubfield);
			
			subFieldId[0]++;
		}
		
	}
	
	/**
	 * Decode Item
	 *
	 * @param rectype
	 * @param recindx
	 * @param fieldId
	 * @param subFieldId
	 * @param itemId
	 * @param rawSubfield
	 * @throws Exception
	 */
	private void decodeItem(Integer[] rectype, Integer[] recindx, Integer[] fieldId, Integer[] subFieldId,
							Integer[] itemId, String rawSubfield) throws Exception {
		
		itemPosition = 0;
		
		itemId[0] = 1;
		
		while (itemPosition < rawSubfield.length()) {
			
			String value = nextItem(rectype, recindx, fieldId, subFieldId, itemId, rawSubfield);
			
			if (fieldId[0] != 999) {
				
				ansiNistPacket.createItem(cleanSeperator(value), rectype[0], recindx[0], fieldId[0], subFieldId[0],
						itemId[0]);
				
				log.info(String.format("field %d.%d.%d.%d.%d=%s", rectype[0], recindx[0], fieldId[0], subFieldId[0],
						itemId[0], value));
				
			} else {
				
				itemPosition = rawSubfield.length();
				
				ansiNistPacket.createItem(cleanSeperator(value), rectype[0], recindx[0], fieldId[0], subFieldId[0],
						itemId[0]);
				
				log.debug(String.format("field %d.%d.%d.%d.%d=%s", rectype[0], recindx[0], fieldId[0], subFieldId[0],
						itemId[0], String.format("Binary Payload Length (%d)", rawSubfield.length())));
				
			}
			
			itemId[0]++;
		}
		
	}
	
	/**
	 * Move record pointer to next record
	 *
	 * @param recType
	 * @return
	 */
	private String nextRecord(Integer[] recType) {
		
		int length = nextRecordLength(recType);
		
		String record = ansiNistPacket.getBuffer().substring(recordPosition, recordPosition + length);
		
		recordPosition = recordPosition + length;
		
		return record;
		
	}
	
	/**
	 * Extract nextField
	 *
	 * @param rectype
	 * @param recindx
	 * @param fieldId
	 * @param rawRecord
	 * @return
	 */
	private String nextField(Integer[] rectype, Integer[] recindx, Integer[] fieldId, String rawRecord) {
		
		int length = nextFieldLength(rawRecord);
		
		String rawField = rawRecord.substring(fieldPosition, fieldPosition + length);
		
		fieldId[0] = Integer.parseInt(rawField.split("\\.")[1].split(SEP_COLN)[0]);
		
		if (fieldId[0] != 999) {
			
			rawField = rawField.split(SEP_COLN)[1];
			
			fieldPosition = fieldPosition + length + 1;
			
			return rawField;
			
		} else {
			
			StringTokenizer tokens = new StringTokenizer(rawField, SEP_COLN);
			int binaryPayloadPosition = tokens.nextToken().length() + 1;
			
			rawField = rawRecord.substring(fieldPosition + binaryPayloadPosition, rawRecord.length());
			fieldPosition = rawRecord.length();
			
			return rawField;
			
		}
		
	}
	
	/**
	 * Extract nextSubfield
	 *
	 * @param rectype
	 * @param recindx
	 * @param fieldId
	 * @param subFieldId
	 * @param rawField
	 * @return
	 */
	private String nextSubfield(Integer[] rectype, Integer[] recindx, Integer[] fieldId, Integer[] subFieldId,
								String rawField) {
		
		if (fieldId[0] != 999) {
			
			int length = nextSubfieldLength(subFieldId, rawField);
			
			String rawSubfield = rawField.substring(subFieldPosition, subFieldPosition + length);
			
			subFieldPosition = subFieldPosition + rawSubfield.length() + 1;
			
			return rawSubfield;
			
		} else {
			
			subFieldPosition = rawField.length();
			return rawField;
			
		}
		
	}
	
	/**
	 * Extract next item
	 *
	 * @param rectype
	 * @param recindx
	 * @param fieldId
	 * @param subFieldId
	 * @param itemId
	 * @param rawSubfield
	 * @return
	 */
	private String nextItem(Integer[] rectype, Integer[] recindx, Integer[] fieldId, Integer[] subFieldId,
							Integer[] itemId, String rawSubfield) {
		
		if (fieldId[0] != 999) {
			
			int length = nextItemLength(itemId, rawSubfield);
			
			String item = rawSubfield.substring(itemPosition, itemPosition + length);
			
			itemPosition = itemPosition + item.length() + 1;
			
			return item;
			
		} else {
			
			itemPosition = rawSubfield.length();
			return rawSubfield;
			
		}
		
	}
	
	/**
	 * Calculate nextRecordLength
	 *
	 * @param recType
	 * @return
	 */
	private int nextRecordLength(Integer[] recType) {
		
		String record = ansiNistPacket.getBuffer().substring(recordPosition, recordPosition + 20);
		
		StringTokenizer tokens = new StringTokenizer(record, SEP_COLN);
		record = tokens.nextToken(); // Skip
		
		int recordType = Integer.parseInt((new StringTokenizer(record, ".")).nextToken());
		recType[0] = recordType;
		
		record = tokens.nextToken(); // Skip
		tokens = new StringTokenizer(record, SEP_GS_STR, true);
		String length = (String) tokens.nextToken();
		
		return Integer.parseInt(length);
		
	}
	
	/**
	 * Calculate nextField length
	 *
	 * @param rawRecord
	 * @return
	 */
	private int nextFieldLength(String rawRecord) {
		
		StringTokenizer tokens = new StringTokenizer(rawRecord.substring(fieldPosition), SEP_GS_STR, true);
		String field = tokens.nextToken(); // Skip
		
		return field.length();
		
	}
	
	/**
	 * Calculate next subfield Length
	 *
	 * @param subFieldId
	 * @param rawField
	 * @return
	 */
	private int nextSubfieldLength(Integer[] subFieldId, String rawField) {
		
		StringTokenizer tokens = new StringTokenizer(rawField.substring(subFieldPosition), SEP_RS_STR, true);
		String field = tokens.nextToken(); // Skip
		
		return field.length();
		
	}
	
	/**
	 * Calculate next item length
	 *
	 * @param itemId
	 * @param rawField
	 * @return
	 */
	private int nextItemLength(Integer[] itemId, String rawField) {
		
		StringTokenizer tokens = new StringTokenizer(rawField.substring(itemPosition), SEP_US_STR, true);
		String field = tokens.nextToken(); // Skip
		
		field = cleanSeperator(field);
		
		return field.length();
		
	}
	
	/**
	 * Clean up separator from decode field
	 *
	 * @param str
	 * @return
	 */
	private String cleanSeperator(String str) {
		
		return str.replaceAll(SEP_US_STR + "+$", "").replaceAll(SEP_GS_STR + "+$", "").replaceAll(SEP_RS_STR + "+$", "")
				.replaceAll(SEP_FS_STR + "+$", "");
	}
	
	/**
	 * Calculate current delimiter based on next record.
	 *
	 * @param prevKeyList
	 * @param currKeyList
	 * @return
	 */
	private String getDelimiter(List<Integer> prevKeyList, List<Integer> currKeyList) {
		
		String delimeter = "";
		// Next Key
		int cRectypeKey = prevKeyList.get(0);
		int cRecindxKey = prevKeyList.get(1);
		int cFieldIdkey = prevKeyList.get(2);
		int cSubfieldIdkey = prevKeyList.get(3);
		int cItemIdKey = prevKeyList.get(4);
		
		// Next Key
		int nRectypeKey = currKeyList.get(0);
		int nRecindxKey = currKeyList.get(1);
		int nFieldIdkey = currKeyList.get(2);
		int nSubfieldIdkey = currKeyList.get(3);
		int nItemIdKey = currKeyList.get(4);
		
		if (cFieldIdkey > nFieldIdkey || cSubfieldIdkey > nSubfieldIdkey || cItemIdKey != nItemIdKey) {
			delimeter = AnsiNistDecoder.SEP_GS_STR;
		}
		
		if (cRectypeKey == nRectypeKey
				&& cRecindxKey == nRecindxKey
				&& cFieldIdkey == nFieldIdkey
				&& cSubfieldIdkey == nSubfieldIdkey) {
			
			delimeter = AnsiNistDecoder.SEP_US_STR;
		}
		
		if (cRectypeKey == nRectypeKey
				&& cRecindxKey == nRecindxKey
				&& cFieldIdkey == nFieldIdkey
				&& cSubfieldIdkey != nSubfieldIdkey) {
			
			delimeter = AnsiNistDecoder.SEP_RS_STR;
		}
		
		if (cRectypeKey == nRectypeKey
				&& cRecindxKey == nRecindxKey
				&& cFieldIdkey != nFieldIdkey) {
			
			delimeter = AnsiNistDecoder.SEP_GS_STR;
			
		}
		
		if (cRectypeKey == nRectypeKey
				&& cRecindxKey != nRecindxKey) {
			delimeter = AnsiNistDecoder.SEP_US_STR;
		}
		
		if (cRectypeKey != nRectypeKey) {
			delimeter = AnsiNistDecoder.SEP_FS_STR;
		}
		
		if (cFieldIdkey == 999) {
			delimeter = AnsiNistDecoder.SEP_FS_STR;
		}
		
		return delimeter;
		
	}
	
	/**
	 * Calculate record length
	 *
	 * @param recordBuffer
	 * @return
	 */
	private int calculateRecordLength(StringBuffer recordBuffer) {
		
		int length = recordBuffer.length() - 2;
		int strLength = new String("" + length).length();
		
		return length + strLength;
	}
	
	/**
	 * Get a list of all decode keys
	 *
	 * @return
	 */
	private List<List<Integer>> getKeyList() {
		
		List<List<Integer>> keyList = new ArrayList<List<Integer>>();
		
		for (Map.Entry<Integer, Object> rectypeEntry : this.ansiNistPacket.getRecordTypeMap().entrySet()) {
			
			int rectypeKey = rectypeEntry.getKey();
			TreeMap<Integer, Object> recindxMap = (TreeMap<Integer, Object>) rectypeEntry.getValue();
			
			for (Map.Entry<Integer, Object> recindxEntry : recindxMap.entrySet()) {
				
				int recindxKey = recindxEntry.getKey();
				TreeMap<Integer, Object> fieldIdMap = (TreeMap<Integer, Object>) recindxEntry.getValue();
				
				for (Map.Entry<Integer, Object> fieldIdEntry : fieldIdMap.entrySet()) {
					
					int fieldIdKey = fieldIdEntry.getKey();
					TreeMap<Integer, Object> subfieldIdMap = (TreeMap<Integer, Object>) fieldIdEntry.getValue();
					
					for (Map.Entry<Integer, Object> fsubieldIdEntry : subfieldIdMap.entrySet()) {
						
						int subfieldIdKey = fsubieldIdEntry.getKey();
						TreeMap<Integer, Object> itemldIdMap = (TreeMap<Integer, Object>) fsubieldIdEntry.getValue();
						
						for (Map.Entry<Integer, Object> itemldIdEntry : itemldIdMap.entrySet()) {
							
							int itemIdKey = itemldIdEntry.getKey();
							keyList.add(Arrays.asList(new Integer[]{rectypeKey, recindxKey, fieldIdKey, subfieldIdKey, itemIdKey}));
							
						}
						
					}
					
				}
				
			}
			
		}
		return keyList;
		
	}
	
	/**
	 * Serialize AnsiNistPacket
	 *
	 * @return
	 */
	public byte[] serialize() {
		
		byte[] empty = new byte[0];
		String serialized = new String(empty, StandardCharsets.ISO_8859_1);
		List<List<Integer>> keyList = getKeyList();
		int keycnt = 0;
		for (Map.Entry<Integer, Object> rectypeEntry : ansiNistPacket.getRecordTypeMap().entrySet()) {
			
			int rectypeKey = rectypeEntry.getKey();
			TreeMap<Integer, Object> recindxMap = (TreeMap<Integer, Object>) rectypeEntry.getValue();
			
			for (Map.Entry<Integer, Object> rectindxEntry : recindxMap.entrySet()) {
				
				StringBuffer recordBuffer = new StringBuffer(1000);
				
				int recindxKey = rectindxEntry.getKey();
				TreeMap<Integer, Object> fieldIdMap = (TreeMap<Integer, Object>) rectindxEntry.getValue();
				
				for (Map.Entry<Integer, Object> fieldIdEntry : fieldIdMap.entrySet()) {
					
					int fieldIdKey = fieldIdEntry.getKey();
					
					recordBuffer.append(String.format("%d.%03d:", rectypeKey, fieldIdKey));
					
					TreeMap<Integer, Object> subfieldIdMap = (TreeMap<Integer, Object>) fieldIdEntry.getValue();
					
					for (Map.Entry<Integer, Object> subfieldIdEntry : subfieldIdMap.entrySet()) {
						
						int subfieldIdKey = subfieldIdEntry.getKey();
						TreeMap<Integer, Object> itemIdMap = (TreeMap<Integer, Object>) subfieldIdEntry.getValue();
						
						for (Map.Entry<Integer, Object> itemIdEntry : itemIdMap.entrySet()) {
							
							int itemIdKey = itemIdEntry.getKey();
							
							String delimiter;
							if (keycnt < keyList.size() - 1) {
								List<Integer> currKeyList = keyList.get(keycnt);
								List<Integer> nextKeyList = keyList.get(keycnt + 1);
								
								delimiter = getDelimiter(currKeyList, nextKeyList);
								
							} else {
								delimiter = AnsiNistDecoder.SEP_FS_STR;
							}
							
							if (fieldIdKey != 1) {
								recordBuffer.append(String.format("%s%s", itemIdEntry.getValue(), delimiter));
							} else {
								recordBuffer.append(String.format("%s%s", "%s", delimiter));
							}
							
							if (fieldIdKey != 999) {
								log.info(String.format("Value key: {%s,%s,%s,%s,%s} = %s",
										rectypeKey, recindxKey, fieldIdKey, subfieldIdKey,
										itemIdKey, itemIdEntry.getValue().toString()));
							} else {
								log.info(String.format("Value key: {%s,%s,%s,%s,%s} = %s",
										rectypeKey, recindxKey, fieldIdKey, subfieldIdKey,
										itemIdKey, "Image"));
								
							}
							
							keycnt++;
						}
					}
				}
				
				int reclen = calculateRecordLength(recordBuffer);
				
				recordBuffer = new StringBuffer(recordBuffer.toString().replaceFirst("%s", reclen + ""));
				serialized = serialized + recordBuffer.toString();
				
				log.info(String.format("Record length calculated: %s , true length: %s", reclen, recordBuffer.length()));
				
			}
			
		}
		
		log.info(String.format("Serialize object length: %s", serialized.length()));
		
		return serialized.getBytes(StandardCharsets.ISO_8859_1);
		
	}
	
}
