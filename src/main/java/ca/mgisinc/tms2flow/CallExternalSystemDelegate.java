package ca.mgisinc.tms2flow;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallExternalSystemDelegate implements JavaDelegate {

    Logger log = LoggerFactory.getLogger(CallExternalSystemDelegate.class);
    public void execute(DelegateExecution execution) {
        log.info(String.format("Calling the external system for employee: %s ",
                 execution.getVariable("employee")));
    }

}