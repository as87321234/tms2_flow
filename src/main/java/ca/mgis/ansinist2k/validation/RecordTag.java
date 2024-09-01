package ca.mgis.ansinist2k.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString

public class RecordTag{
	public String id;
	public String identifier;
	public String tag_name = "";
	public String note = "";
	
	public String field_tag_name;
	public int field_min_size;
	public int field_max_size;
	public int field_occ_min;
	public int field_occ_max;
	public String condition;
	public String regexPattern;
	public List<String> character_set;
	
	public ArrayList<Occurrence> occurrence;
}
