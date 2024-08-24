package ca.mgis.tms2flow.controller;

import ca.mgis.tms2flow.config.AppConfig;
import ca.mgis.tms2flow.controller.pojo.BiometricEnrolment;
import jakarta.servlet.http.HttpServletResponse;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/flow-api/config")

public class BiometricConfigController {
	
	private final Logger log = LoggerFactory.getLogger(BiometricConfigController.class);
	
	@Autowired
	AppConfig appConfig;
	
	@RequestMapping(value = "/get-oai", method = RequestMethod.GET)
	public @ResponseBody
	String getOriginateAgencyIdentified(HttpServletResponse response, @RequestBody BiometricEnrolment biometricEnrolment) {
		
		log.info(String.format("Biometric get Agency Originating Identifief: %s", appConfig.getOai()));
		
		return appConfig.getOai();
		
		
	}
	
	@RequestMapping(value = "/get-dai", method = RequestMethod.GET)
	public @ResponseBody
	String[] getDestinationAgencyIdentified(HttpServletResponse response, @RequestBody BiometricEnrolment biometricEnrolment) {
		
		log.info(String.format("Biometric get Agency Originating Identifief: {}", appConfig.getDai()));
		
		return appConfig.getDai();
		
	}
	
}

