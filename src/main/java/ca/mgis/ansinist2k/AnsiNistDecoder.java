package ca.mgis.ansinist2k;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class AnsiNistDecoder {

	private static Logger log = LoggerFactory.getLogger(AnsiNistDecoder.class);

	// Information separators
	private static final byte SEP_US = 31;
	private static final byte SEP_RS = 30;
	private static final byte SEP_GS = 29;
	private static final byte SEP_FS = 28;

	private static final String SEP_US_STR = new String(new byte[] { SEP_US });
	private static final String SEP_RS_STR = new String(new byte[] { SEP_RS });
	private static String SEP_GS_STR = new String(new byte[] { SEP_GS });
	private static String SEP_FS_STR = new String(new byte[] { SEP_FS });

	private static String SEP_COLN = ":";

	String buffer;

	int recordPosition = 0;
	int fieldPosition = 0;
	int subFieldPosition = 0;
	int itemPosition = 0;
	
	@Getter
	AnsiNistPacket ansiNistPacket;

	public AnsiNistDecoder(byte[] processingBuffer, AnsiNistValidator ansiNistValidator) throws Exception {

		this.buffer = new String (processingBuffer, StandardCharsets.US_ASCII);
		this.ansiNistPacket = new AnsiNistPacket();
		this.ansiNistPacket.setValidator(ansiNistValidator);
		this.decode();
	}
	
	

	private void decode() throws Exception {

		int prevRectype = 0;
		Integer[] recindx = new Integer[] { 0 };

		while (recordPosition < buffer.length()) {

			Integer[] rectype = new Integer[1];

			Integer[] fieldId = new Integer[1];

			String rawRecord = nextRecord(rectype);

			if (prevRectype != rectype[0]) {
				recindx = new Integer[] { 1 };
			} else {
				recindx[0]++;
			}

			prevRectype = rectype[0];

			decodeFields(rectype, recindx, fieldId, rawRecord);
			log.info(String.format("Finish decoding record type: %s", prevRectype));
		}

	}

	private void decodeFields(Integer[] rectype, Integer[] recindx, Integer[] fieldId, String rawRecord)
			throws Exception {

		fieldPosition = 0;

		while (fieldPosition < rawRecord.length()) {

			Integer[] subfieldId = { 0 };

			String rawField = nextField(rectype, recindx, fieldId, rawRecord);

			decodeSubFields(rectype, recindx, fieldId, subfieldId, rawField);

		}

	}

	private void decodeSubFields(Integer[] rectype, Integer[] recindx, Integer[] fieldId, Integer[] subFieldId,
			String rawField) throws Exception {

		subFieldPosition = 0;

		subFieldId[0] = 1;
		Integer[] itemId = { 0 };

		while (subFieldPosition < rawField.length()) {

			String rawSubfield = nextSubfield(rectype, recindx, fieldId, subFieldId, rawField);

			decodeItem(rectype, recindx, fieldId, subFieldId, itemId, rawSubfield);

			subFieldId[0]++;
		}

	}

	private void decodeItem(Integer[] rectype, Integer[] recindx, Integer[] fieldId, Integer[] subFieldId,
			Integer[] itemId, String rawSubfield) throws Exception {

		itemPosition = 0;

		itemId[0] = 1;

		while (itemPosition < rawSubfield.length()) {

			String value = nextItem(rectype, recindx, fieldId, subFieldId, itemId, rawSubfield);

			if (fieldId[0] != 999) {

				ansiNistPacket.createItem(cleanSeperator(value), rectype[0], recindx[0], fieldId[0], subFieldId[0],
						itemId[0]);

				log.debug(String.format("field %d.%d.%d.%d.%d=%s", rectype[0], recindx[0], fieldId[0], subFieldId[0],
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

	private String nextRecord(Integer[] recType) {

		int length = nextRecordLenght(recType);

		String record = buffer.substring(recordPosition, recordPosition + length);

		recordPosition = recordPosition + length;

		return record;

	}

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

	private int nextRecordLenght(Integer[] recType) {

		String record = this.buffer.substring(recordPosition, recordPosition + 20);

		StringTokenizer tokens = new StringTokenizer(record, SEP_COLN);
		record = tokens.nextToken(); // Skip

		int recordType = Integer.parseInt((new StringTokenizer(record, ".")).nextToken());
		recType[0] = new Integer(recordType);

		record = tokens.nextToken(); // Skip
		tokens = new StringTokenizer(record, SEP_GS_STR, true);
		String length = (String) tokens.nextToken();

		return Integer.parseInt(length);

	}

	private int nextFieldLength(String rawRecord) {

		StringTokenizer tokens = new StringTokenizer(rawRecord.substring(fieldPosition), SEP_GS_STR, true);
		String field = tokens.nextToken(); // Skip

		return field.length();

	}

	private int nextSubfieldLength(Integer[] subFieldId, String rawField) {

		StringTokenizer tokens = new StringTokenizer(rawField.substring(subFieldPosition), SEP_RS_STR, true);
		String field = tokens.nextToken(); // Skip

		return field.length();

	}

	private int nextItemLength(Integer[] itemId, String rawField) {

		StringTokenizer tokens = new StringTokenizer(rawField.substring(itemPosition), SEP_US_STR, true);
		String field = tokens.nextToken(); // Skip

		field = cleanSeperator(field);

		return field.length();

	}

	private String cleanSeperator(String str) {

		return str.replaceAll(SEP_US_STR + "+$", "").replaceAll(SEP_GS_STR + "+$", "").replaceAll(SEP_RS_STR + "+$", "")
				.replaceAll(SEP_FS_STR + "+$", "");
	}

	public byte[] serialize() {

		StringBuffer fileBuffer = new StringBuffer(1000);

		for (int rectype = 1; rectype <= 100; rectype++) {

			// If RecordType does not exist move the next record
			if (ansiNistPacket.getRecordTypeCount(rectype) == 0) {
				continue;
			}

			try {

				for (int recindx = 1; recindx <= ansiNistPacket.getRecordTypeCount(rectype); recindx++) {

					StringBuffer recordBuffer = new StringBuffer(1000);

					int fieldId = 1;

					while (fieldId > 0) {

						recordBuffer.append(String.format("%d.", rectype));
						recordBuffer.append(String.format("%03d:", fieldId));

						int numSubfields = ansiNistPacket.numSubfields(rectype, recindx, fieldId);

						for (int subFieldId = 1; subFieldId <= numSubfields; subFieldId++) {

							int numItems = ansiNistPacket.numItems(rectype, recindx, fieldId, subFieldId);

							for (int itemId = 1; itemId <= numItems; itemId++) {

								String value = ansiNistPacket.findItem(rectype, recindx, fieldId, subFieldId, itemId);
								if (fieldId != 999) {
									log.debug(String.format("Serializing %d.%d.%03d.%d.%d=%s", rectype, recindx,
											fieldId, subFieldId, itemId, value));

								}

								// Set place holder for the record length
								if (fieldId == 1) {
									recordBuffer.append("%s");
								} else {
									recordBuffer.append(value);
								}

								if (numItems >= 1 && itemId < numItems) {

									if (itemId >= 1 && itemId < numItems) {
										recordBuffer.append(SEP_US_STR);
									} else if (itemId > 1) {
										recordBuffer.append(SEP_RS_STR);
									}
								}
							}

							if (numSubfields >= 1 && subFieldId < numSubfields) {
								recordBuffer.append(SEP_RS_STR);
							}

						}

						fieldId = ansiNistPacket.getNextField(rectype, 1, fieldId);

						if (fieldId == 0) {
							recordBuffer.append(SEP_FS_STR);
						} else {
							recordBuffer.append(SEP_GS_STR);
						}

					}

					// Calculate record length
					String length = (recordBuffer.length() - 2 + (recordBuffer.length() + "").length()) + "";
					String recordStr = recordBuffer.toString().replaceFirst("%s", length);
					log.debug(String.format("Record type %d.%d.%03d lenght is: %s", rectype, recindx, fieldId, length));

					fileBuffer.append(recordStr);

				}

			} catch (Exception e) {

				e.printStackTrace();

			}
		}

		return fileBuffer.toString().getBytes();
	}

}
