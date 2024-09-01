package ca.mgis.ansinist2k;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class AnsiNistPacket {
	
	
	Logger log = LoggerFactory.getLogger(AnsiNistPacket.class);
	
	@Getter
	private SortedMap<Integer, Object> recordTypeMap = new TreeMap<Integer, Object>();
	
	@Getter
	private String buffer;
	
	private AnsiNistDecoder ansiNistDecoder;
	
	@Setter
	@Getter
	private AnsiNistValidator ansiNistValidator;
	
	/**
	 * Construct a NIST packet from an input stream
	 *
	 * @throws Exception
	 */
	
	public AnsiNistPacket() {
	
	}
	
	public AnsiNistPacket(String filePath, AnsiNistValidator ansiNistValidator) throws Exception {
		
		byte[] bFile = Files.readAllBytes(Paths.get(filePath));
		
		log.info(String.format("Nist packet %s successfully decoded", filePath));
		
		loadNistPackFromInputStream(bFile,  ansiNistValidator );
	}
	
	/**
	 * Construct a NIST packet from an input stream
	 *
	 * @param ins
	 * @throws Exception
	 */
	public AnsiNistPacket(InputStream ins, AnsiNistValidator ansiNistValidator) throws Exception {
		
		loadNistPackFromInputStream(ins,  ansiNistValidator);
		
	}
	
	public AnsiNistPacket(  AnsiNistValidator ansiNistValidator) throws Exception {

		this.ansiNistDecoder = new AnsiNistDecoder(this);
		this.ansiNistValidator = ansiNistValidator;
		
	}
	
	public AnsiNistPacket(byte[] bytes, Validator177f validator177f) {
	}

//	public AnsiNistPacket(AnsiNistDecoder decoder) {
//
//		this.ansiNistDecoder = decoder;
//	}
//
//	public AnsiNistPacket(AnsiNistValidator validator) {
//
//		this.validator = validator;
//
//	}
	
	public void loadNistPackFromInputStream(InputStream ins, AnsiNistValidator ansiNistValidator) throws Exception {
		
		ByteArrayOutputStream bous = new ByteArrayOutputStream();
		
		IOUtils.copy(ins, bous);
		
		loadNistPackFromInputStream(bous.toByteArray(),  ansiNistValidator);
		
	}
	
	public void loadNistPackFromInputStream(byte[] bFile ,AnsiNistValidator ansiNistValidator ) throws Exception {
		
		buffer = new String(bFile, StandardCharsets.ISO_8859_1);
		
		// Instantiate a NIST Pack decoder and load byte array in memory
		ansiNistDecoder = new AnsiNistDecoder(this);
		ansiNistDecoder.decode();
		
	}
	
	// Record Type
	
	@SuppressWarnings("unchecked")
	public SortedMap<Integer, Object> getMapLevel(SortedMap<Integer, Object> nextLevel, Integer key) {
		
		return (SortedMap<Integer, Object>) nextLevel.get(key);
		
	}
	
	public Object getMapLevelObject(SortedMap<Integer, Object> nextLevel, Integer key) {
		
		return nextLevel.get(key);
		
	}
	
	@SuppressWarnings("unchecked")
	public SortedMap<Integer, Object> addMapLevel(SortedMap<Integer, Object> map, Integer key) throws Exception {
		
		SortedMap<Integer, Object> node = (SortedMap<Integer, Object>) map.get(key);
		
		if (node != null) {
			
			return node;
		} else {
			SortedMap<Integer, Object> aNode = new TreeMap<Integer, Object>();
			map.put(key, aNode);
			return aNode;
		}
		
	}
	
	public String findItem(int rectype, int recindx, int fieldId, int subFieldId, int itemId) throws Exception {
		
		String key = String.format("%d.%d.%d.%d.%d", rectype, recindx, fieldId, subFieldId, itemId);
		return findItem(key);
	}
	
	@SuppressWarnings("unchecked")
	public String findItem(String key) throws Exception {
		
		List<Integer> keyLevel = parseKey(key);
		
		SortedMap<Integer, Object> nextLevel = recordTypeMap;
		
		for (int i = 0; i < keyLevel.size(); i++) {
			
			try {
				nextLevel = (SortedMap<Integer, Object>) getMapLevelObject(nextLevel, keyLevel.get(i));
				
				if (nextLevel == null) {
					
					return null;
					
				}
				
			} catch (RuntimeException e) {
				
				String value = (String) nextLevel.get(keyLevel.get(4));
				return value;
			}
			
		}
		
		throw new Exception(String.format("Key %s not found", key));
		
	}
	
	public void createItem(String value, Integer rectype, Integer recindx, Integer fieldId, Integer subFieldId,
						   Integer itemId) throws Exception {
		
		String key = String.format("%d.%d.%d.%d.%d", rectype, recindx, fieldId, subFieldId, itemId);
		
		createItem(value, key);
		
	}
	
	@SuppressWarnings("unchecked")
	public void createItem(String value, String key) throws Exception {
		
		List<Integer> keyLevel = parseKey(key);
		
		SortedMap<Integer, Object> nextLevel = recordTypeMap;
		
		if (keyLevel.get(2) == 1) {}
		
		for (int i = 0; i < keyLevel.size(); i++) {
			
			if (getMapLevelObject(nextLevel, keyLevel.get(i)) instanceof byte[]) {
				
				throw new Exception(String.format("Key %s does exist", key));
			}
			
			if (getMapLevelObject(nextLevel, keyLevel.get(i)) == null && i < 4) {
				
				SortedMap<Integer, Object> aNode = new TreeMap<Integer, Object>();
				
				nextLevel.put(keyLevel.get(i), aNode);
				nextLevel = aNode;
				
			} else if (getMapLevelObject(nextLevel, keyLevel.get(i)) != null && i < 4) {
				
				nextLevel = (SortedMap<Integer, Object>) getMapLevelObject(nextLevel, keyLevel.get(i));
				
			} else {
				
				nextLevel.put(keyLevel.get(i), value);
				
			}
			
		}
		
	}
	
	public void setItem(String value, int rectype, int recindx, int fieldId, int subFieldId, int itemId)
			throws Exception {
		
		String key = String.format("%d.%d.%d.%d.%d", rectype, recindx, fieldId, subFieldId, itemId);
		setItem(value, key);
	}
	
	public void setItem(String value, String key) throws Exception {
		
		List<Integer> keyLevel = parseKey(key);
		
		SortedMap<Integer, Object> nextLevel = recordTypeMap;
		
		for (int i = 0; i < keyLevel.size(); i++) {
			
			if (getMapLevelObject(nextLevel, keyLevel.get(i)) == null) {
				
				throw new Exception("Key does not exists");
				
			}
			
			if (getMapLevelObject(nextLevel, keyLevel.get(i)) instanceof String) {
				
				nextLevel.put(keyLevel.get(4), value);
				
			} else {
				nextLevel = getMapLevel(nextLevel, keyLevel.get(i));
			}
		}
		
	}
	
	private List<Integer> parseKey(String key) {
		
		String[] tokens = key.split("\\.");
		
		List<Integer> keyLevel = new ArrayList<Integer>();
		
		for (int i = 0; i < tokens.length; i++) {
			
			keyLevel.add(Integer.parseInt(tokens[i]));
		}
		
		return keyLevel;
	}
	
	public int getRecordTypeCount(int rectype) {
		
		@SuppressWarnings("unchecked")
		SortedMap<Integer, Object> map = (SortedMap<Integer, Object>) this.recordTypeMap.get(rectype);
		
		if (map == null) {
			return 0;
		} else {
			return map.size();
		}
	}
	
	public String retrieveItem(int recordType, int recordIndex, int field, int subfield, int item) throws Exception {
		return new String(findItem(String.format("%d.%d.%d.%d.%d", recordType, recordIndex, field, subfield, item)));
		
	}
	
	@SuppressWarnings("unchecked")
	public int getNextField(int rectype, int recordindx, int fieldId) {
		
		SortedMap<Integer, Object> map = (SortedMap<Integer, Object>) this.recordTypeMap.get(rectype);
		map = (SortedMap<Integer, Object>) map.get(recordindx);
		
		final Integer[] fieldIdResponse = new Integer[]{0};
		final boolean[] isNext = new boolean[]{false};
		
		try {
			map.forEach((k, value) -> {
				
				if (isNext[0]) {
					fieldIdResponse[0] = k;
					
					throw new RuntimeException("");
					
				}
				
				if (k == fieldId) {
					isNext[0] = true;
				}
				
			});
		} catch (Exception e) {
			// continue
		}
		
		return fieldIdResponse[0];
		
	}
	
	@SuppressWarnings("unchecked")
	public int numItems(int rectype, int recordindx, int fieldId, int subFieldId) {
		SortedMap<Integer, Object> map = (SortedMap<Integer, Object>) this.recordTypeMap.get(rectype);
		map = (SortedMap<Integer, Object>) map.get(recordindx);
		map = (SortedMap<Integer, Object>) map.get(fieldId);
		map = (SortedMap<Integer, Object>) map.get(subFieldId);
		
		return map.size();
	}
	
	@SuppressWarnings("unchecked")
	public int numSubfields(int rectype, int recordindx, int fieldId) {
		
		SortedMap<Integer, Object> map = (SortedMap<Integer, Object>) this.recordTypeMap.get(rectype);
		map = (SortedMap<Integer, Object>) map.get(recordindx);
		map = (SortedMap<Integer, Object>) map.get(fieldId);
		
		return map.size();
	}
	
	public SortedSet<Integer> getRectypeList() {
		
		SortedMap<Integer, Object> map = recordTypeMap;
		SortedSet<Integer> rectypeSet = new TreeSet<Integer>();
		
		map.forEach((k, n) -> {
			
			rectypeSet.add(k);
		});
		
		return rectypeSet;
	}
	
	public byte[] serialize() throws Exception {
		
		return ansiNistDecoder.serialize();
		
	}
	
	
	public void traverse() {
		
		for (Map.Entry<Integer, Object> rectypeEntry : getRecordTypeMap().entrySet()) {
			Integer rectypeValue = rectypeEntry.getKey();
			TreeMap<Integer, Object> rectypeMap = (TreeMap<Integer, Object>) rectypeEntry.getValue();
			
			log.debug(String.format("rectype: %s - %s", rectypeValue, rectypeMap.toString()));
			
			for (Map.Entry<Integer, Object> recindxEntry : rectypeMap.entrySet()) {
				Integer recindxValue = recindxEntry.getKey();
				TreeMap<Integer, Object> fieldIdMap = (TreeMap<Integer, Object>) recindxEntry.getValue();
				
				log.debug(String.format("recindx: %s - %s", recindxValue, recindxEntry.toString()));
				
				for (Map.Entry<Integer, Object> fieldIdEntry : fieldIdMap.entrySet()) {
					Integer fieldIdValue = fieldIdEntry.getKey();
					TreeMap<Integer, Object> subfieldIdMap = (TreeMap<Integer, Object>) fieldIdEntry.getValue();
					
					log.debug(String.format("fieldId: %s - %s", fieldIdValue, fieldIdEntry.toString()));
					
					for (Map.Entry<Integer, Object> subFieldIdEntry : subfieldIdMap.entrySet()) {
						Integer subFieldIdValue = subFieldIdEntry.getKey();
						TreeMap<Integer, Object> itemIdMap = (TreeMap<Integer, Object>) subFieldIdEntry.getValue();
						
						log.debug(String.format("subFieldId: %s - %s", subFieldIdValue, subFieldIdEntry.toString()));
						
						for (Map.Entry<Integer, Object> itemIdEntry : itemIdMap.entrySet()) {
							Integer itemIdValue = subFieldIdEntry.getKey();
							Object valuedMap = itemIdEntry.getValue();
							
							log.debug(String.format("itemId: %s - %s", itemIdValue, itemIdEntry.toString()));
							
							if (fieldIdValue == 999) {
								log.debug(String.format("{%s,%s,%s,%s,%s} value: %s",
										rectypeValue, recindxValue, fieldIdValue,
										subFieldIdValue, itemIdValue, "Image"));
							} else {
								log.debug(String.format("{%s,%s,%s,%s,%s} value: %s",
										rectypeValue, recindxValue, fieldIdValue,
										subFieldIdValue, itemIdValue, valuedMap.toString()));
							}
						}
					}
				}
			}
		}
	}
	
	
	public boolean validate() {
		
		boolean valid = true;
		for (Map.Entry<Integer, Object> rectypeEntry : getRecordTypeMap().entrySet()) {
			
			Integer rectypeKey = rectypeEntry.getKey();
			TreeMap<Integer, Object> recindxMap = (TreeMap<Integer, Object>) rectypeEntry.getValue();
			
			for (Map.Entry<Integer, Object> recindxEntry : recindxMap.entrySet()) {
				
				Integer recindxKey = recindxEntry.getKey();
				TreeMap<Integer, Object> fieldIdMap = (TreeMap<Integer, Object>) recindxEntry.getValue();

				for (Map.Entry<Integer, Object> fieldIdEntry : fieldIdMap.entrySet()) {
					
					Integer fieldIdKey = fieldIdEntry.getKey();
					TreeMap<Integer, Object> subfieldIdMap = (TreeMap<Integer, Object>) fieldIdEntry.getValue();
					
					for (Map.Entry<Integer, Object> subfieldIdEntry : subfieldIdMap.entrySet()) {
						
						Integer subfieldIdKey = subfieldIdEntry.getKey();
						TreeMap<Integer, Object> itemIdMap = (TreeMap<Integer, Object>) subfieldIdEntry.getValue();
						
						for (Map.Entry<Integer, Object> itemIdEntry : itemIdMap.entrySet()) {
							
							Integer itemIdKey = itemIdEntry.getKey();

							String key = String.format("%s.%s.%s.%s.%s", rectypeKey, recindxKey, fieldIdKey, subfieldIdKey, itemIdKey);
							String tag = String.format("%s.%03d", rectypeKey, fieldIdKey);
							String value = (String) itemIdEntry.getValue();
							
							if (fieldIdKey != 999) {
								log.debug(String.format("itemId: %s - tag: %s - %s", key, tag, value));
							} else {
								log.debug(String.format("itemId: %s - tag: %s - %s", key, tag, "Image"));
							}
							
							log.warn("AnsiNistPacket validator not being called, to be enable once validation is in place");
							// valid = valid && ansiNistValidator.validate(tag, fieldIdKey, subfieldIdKey, itemIdKey, value);
							
						}
						
					}
					
				}
				
				
			}
			
		}
		
		return valid;

	}
	
	/**
	 * Get a list of all decode keys
	 *
	 * @return
	 */
	public List<List<Integer>> getKeyList() {
		
		List<List<Integer>> keyList = new ArrayList<List<Integer>>();
		
		for (Map.Entry<Integer, Object> rectypeEntry : getRecordTypeMap().entrySet()) {
			
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
	 * Delete all record types
	 *
	 */
	public void deleteAll() {
		
		Set<Map.Entry<Integer, Object>> keyset = recordTypeMap.entrySet();
		
		for (Map.Entry<Integer, Object> rectypeEntry : getRecordTypeMap().entrySet()) {
			
			TreeMap<Integer, Object> recindxMap = (TreeMap<Integer, Object>) recordTypeMap.get(recordTypeMap.firstKey());
		
			log.info("Delete all NistPacket record types = " + recindxMap.size());
			recordTypeMap = new TreeMap<Integer, Object>();
		
			break;
		}
		
	}
}
