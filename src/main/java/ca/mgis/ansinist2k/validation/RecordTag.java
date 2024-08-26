package ca.mgis.ansinist2k.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString

public class RecordTag{
	public String id;
	public String identifier;
	public String tag_name = "";
	public String note = "";
	public int tag_min_occurrence = 1;
	public int tag_max_occurrence = 1;
	public ArrayList<Occurrence> occurrence;
}
