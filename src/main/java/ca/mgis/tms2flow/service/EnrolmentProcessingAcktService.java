package ca.mgis.tms2flow.service;

import ca.mgis.tms2flow.helper.FlowableProcessHelper;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.delegate.TriggerableActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnrolmentProcessingAcktService implements TriggerableActivityBehavior {
	
	private final Logger log = LoggerFactory.getLogger(EnrolmentProcessingAcktService.class);
	@Autowired
	ProcessEngine processEngine;
	@Autowired
	FlowableProcessHelper fpHelper;
	
	@Override
	public void execute(DelegateExecution execution) {
		
		log.info(fpHelper.logFormatter(execution, "wait for ACKT from RCMP"));
		
	}
	
	@Override
	public void trigger(DelegateExecution execution, String signalEvent, Object signalData) {
		
		log.info(fpHelper.logFormatter(execution, "Received ACKT Trigger"));
		
	}
	
}