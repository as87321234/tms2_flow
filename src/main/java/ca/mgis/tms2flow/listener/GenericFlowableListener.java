package ca.mgis.tms2flow.listener;

import ca.mgis.tms2flow.helper.FlowableProcessHelper;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GenericFlowableListener implements ExecutionListener {

    private final Logger log = LoggerFactory.getLogger(GenericFlowableListener.class);

    final FlowableProcessHelper fpHelper = new FlowableProcessHelper();

    @Override
    public void notify(DelegateExecution execution) {

        log.info(fpHelper.logFormatter(execution, ""));

    }
}
