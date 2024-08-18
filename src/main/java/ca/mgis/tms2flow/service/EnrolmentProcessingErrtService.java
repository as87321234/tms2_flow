package ca.mgis.tms2flow.service;

import ca.mgis.tms2flow.helper.FlowableProcessHelper;
import ca.mgis.tms2flow.helper.MgisFlowConstant;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.delegate.TriggerableActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnrolmentProcessingErrtService implements TriggerableActivityBehavior {

    private final Logger log = LoggerFactory.getLogger(EnrolmentProcessingErrtService.class);
    @Autowired
    ProcessEngine processEngine;
    @Autowired
    FlowableProcessHelper fpHelper;

    @Override
    public void execute(DelegateExecution execution) {

        log.info(fpHelper.logFormatter(execution, "wait for ERRT from RCMP"));

    }

    @Override
    public void trigger(DelegateExecution execution, String signalEvent, Object signalData) {

        // Trigger ACKT is not already received
        fpHelper.triggerExecution(execution.getRootProcessInstanceId(),
                MgisFlowConstant.ID_ENROLMENT_PROCESSING_ACKT_RECEIVE_SERVICE_TASK);

        // Trigger ERRT it will never be received
        fpHelper.triggerExecution(execution.getRootProcessInstanceId(),
                MgisFlowConstant.ID_ENROLMENT_PROCESSING_SRE_RECEIVE_SERVICE_TASK);

        log.info(fpHelper.logFormatter(execution, "Received ERRT Trigger"));


    }

}
