package ca.mgis.tms2flow.service;

import ca.mgis.tms2flow.helper.FlowableProcessHelper;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnrolmentProcessingGenerateDcnTcn implements JavaDelegate {
	
	private final Logger log = LoggerFactory.getLogger(EnrolmentProcessingFinalizeService.class);
	
	@Autowired
	ProcessEngine processEngine;
	@Autowired
	FlowableProcessHelper fpHelper;
	
	@Override
	public void execute(DelegateExecution execution) {
		
		log.info(fpHelper.logFormatter(execution, "Generating DCN and TCN"));
		
	}
	
}
