package ca.mgis.tms2flow.service;

import ca.mgis.tms2flow.helper.FlowableProcessHelper;
import ca.mgis.tms2flow.model.Dcn;
import ca.mgis.tms2flow.repository.DcnRepository;
import ca.mgis.tms2flow.repository.TcnRepository;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.hibernate.exception.spi.TemplatedViolatedConstraintNameExtractor;
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
	
	@Autowired
	DcnRepository dcnRepository;
	
	@Autowired
	TcnRepository	tcnRepository;
	
	@Override
	
	public void execute(DelegateExecution execution) {
		
		log.info(fpHelper.logFormatter(execution, "Generating DCN and TCN"));
		
		log.info(fpHelper.logFormatter(execution,
				String.format("Generating DCN:%03d and TCN:%07d",dcnRepository.getNextVal(), tcnRepository.getNextVal())));
		
	}
	
}
