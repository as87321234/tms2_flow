package ca.mgis.ansinist2k.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString

public class Validator{
	
	public String alpha_regx;
	public String numeric_regx;
	public String special_regx;
	public String crlf_regx;
	public String period_regx;
	
	public ArrayList<RecordType> record_type;
	
}
