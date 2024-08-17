package ca.mgis.tms2flow.service;

import ca.mgis.tms2flow.controller.pojo.BiometricEnrolment;
import ca.mgis.tms2flow.helper.FlowableProcessHelper;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Component
public class EnrolmentProcessingTreService implements JavaDelegate {

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    FlowableProcessHelper fpHelper;


    Logger log = LoggerFactory.getLogger(EnrolmentProcessingTreService.class);

    @Override
    public void execute(DelegateExecution execution) {

        BiometricEnrolment biometricEnrolment =
                (BiometricEnrolment) execution.getVariableInstance(BiometricEnrolment.class
                        .getName()).getCachedValue();

        log.info(fpHelper.logFormatter(execution, String.format("Received TRE Id: %s",
                biometricEnrolment.getBiometricId())));

    }
}
