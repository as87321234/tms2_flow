package ca.mgis.tms2flow.service;

import ca.mgis.tms2flow.helper.FlowableProcessHelper;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.delegate.TriggerableActivityBehavior;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceQueryRequest;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EnrolmentProcessingAcktService implements TriggerableActivityBehavior {

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    FlowableProcessHelper fpHelper;

    Logger log = LoggerFactory.getLogger(EnrolmentProcessingAcktService.class);

    @Override
    public void execute(DelegateExecution execution) {

        log.info(fpHelper.logFormatter(execution, "Received ACKT"));

    }

    @Override
    public void trigger(DelegateExecution execution, String signalEvent, Object signalData) {

        log.info(fpHelper.logFormatter(execution, "Received ACKT Trigger"));

    }

}