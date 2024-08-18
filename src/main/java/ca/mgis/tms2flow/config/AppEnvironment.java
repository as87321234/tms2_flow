package ca.mgis.tms2flow.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app.environment")
@Getter
@Setter
@ToString
public class AppEnvironment {
	
	private int processmaximum;
	private int executionmaximum;
	
}