package ca.mgis.tms2flow.service;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class HelloFlowableService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {


        System.out.println(String.format("HelloFlowableService: %s","testParam"));

    }
}



