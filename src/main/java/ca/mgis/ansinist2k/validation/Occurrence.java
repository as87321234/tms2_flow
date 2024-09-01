package ca.mgis.ansinist2k.validation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root[] root = om.readValue(myJsonString, Root[].class); */
@Getter
@Setter
@ToString
public class Occurrence{
	public String condition;
	public String tag_name;
	public ArrayList<String> character_set;
	public int field_min_size;
	public int field_max_size;
	public int field_occ_min;
	public int field_occ_max;
}

