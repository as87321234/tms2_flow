package ca.mgis.ansinist2k.validation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString

public class TransactionType {
	public String transactionType;
	public String description;
	public ArrayList<RecordType> record_type;
}
