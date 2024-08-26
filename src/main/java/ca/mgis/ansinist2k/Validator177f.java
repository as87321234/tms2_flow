package ca.mgis.ansinist2k;

import ca.mgis.ansinist2k.validation.Occurrence;
import ca.mgis.ansinist2k.validation.RecordTag;
import ca.mgis.ansinist2k.validation.RecordType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
	
	@Override
	public void validate() {
		
		log.info(String.format("ALPHA REGX: %s, NUMERIC REGX: %s, SPECIAL REGX %s, PERIOD REGX %s, CRLF REGX %s", alpha_regx, numeric_regx, special_regx, period_regx, crlf_regx));
		
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
}
