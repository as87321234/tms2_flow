package ca.mgis.tms2flow.controller;

import ca.mgis.ansinist2k.AnsiNistDecoder;
import ca.mgis.ansinist2k.AnsiNistValidator;
import ca.mgis.ansinist2k.Validator177f;
import ca.mgis.tms2flow.controller.pojo.BiometricEnrolment;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
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
@RequestMapping("/flow-api/biometric")

public class BiometricCollectionController {
	
	private final Logger log = LoggerFactory.getLogger(BiometricCollectionController.class);
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	TaskService taskService;
	
	@RequestMapping(value = "/post-biometric-collection",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.POST)
	public @ResponseBody
	void sendBiometricCollection(HttpServletResponse response, @RequestBody BiometricEnrolment biometricEnrolment) {
		
		log.info(String.format("Biometric Enrolment received: %s", biometricEnrolment.getBiometricId()));
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put(BiometricEnrolment.class.getName(), biometricEnrolment);
		
		runtimeService.startProcessInstanceByKey("id-enrolment-process-tre",
				biometricEnrolment.getBiometricId(), map);
		
	}
	
	@RequestMapping(value = "/get-process-instances-variables/{id}/{name}",
			produces = MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.GET)
	
	public @ResponseBody
	BiometricEnrolment getProcessInstanceVariables(HttpServletResponse response,
									 @PathVariable(value="id") final String id,
									 @PathVariable(value="name") final String name
									 ) throws Exception {
		
		log.info(String.format("Get variable for Process id: %s and Variable name: %s",
				id, name));
		
		Map<String,Object>  runtimeVariable = runtimeService.getVariables(id);
		
		String nistPackBase64 = ((BiometricEnrolment) runtimeVariable.get(name)).getEnrolmentBase64();
		
//		String decodedPacket = new String (Base64.decodeBase64(nistPackBase64));
//
//		AnsiNistDecoder nist =  new AnsiNistDecoder();
		
		return (BiometricEnrolment) runtimeVariable.get(name);
		
	}
	
}

