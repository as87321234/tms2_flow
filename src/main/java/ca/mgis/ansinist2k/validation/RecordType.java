package ca.mgis.ansinist2k.validation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString

public class RecordType{
	public int id;
	public ArrayList<RecordTag> record_tag;
}
