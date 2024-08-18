package ca.mgis.tms2flow.listener;

import ca.mgis.tms2flow.helper.FlowableProcessHelper;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GenericFlowableListener implements ExecutionListener {
	
	private final FlowableProcessHelper fpHelper = new FlowableProcessHelper();
	private final Logger log = LoggerFactory.getLogger(GenericFlowableListener.class);
	
	@Override
	public void notify(DelegateExecution execution) {
		
		log.info(fpHelper.logFormatter(execution, ""));
		
	}
}
