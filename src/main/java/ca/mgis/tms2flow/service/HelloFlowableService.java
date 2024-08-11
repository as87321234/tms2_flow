package ca.mgis.tms2flow.service;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class HelloFlowableService implements JavaDelegate {

    Logger log = LoggerFactory.getLogger(HelloFlowableService.class);
    @Override
    public void execute(DelegateExecution execution) {


        log.info(String.format("HelloFlowableService action: %s",execution.getVariable("action")));
        log.info(String.format("HelloFlowableService b key : %s",execution.getProcessInstanceBusinessKey()));

    }
}



