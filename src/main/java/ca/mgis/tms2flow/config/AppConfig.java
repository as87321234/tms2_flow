package ca.mgis.tms2flow.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@Getter
@Setter
public class AppConfig {
	
	@Value("${rcmp.oai}")
	String oai;

	@Value("${rcmp.dai}")
	String dai;
	
	public String[] getDai() {
		return dai.split(",");
	}
	
}
