package ca.mgis.ansinist2k;

import ca.mgis.ansinist2k.validation.RecordTag;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public abstract class AnsiNistValidator {
	
	public abstract void validate();
	
}
	