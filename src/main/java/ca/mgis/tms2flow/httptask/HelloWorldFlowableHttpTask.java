package ca.mgis.tms2flow.httptask;

import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldFlowableHttpTask implements org.flowable.engine.delegate.TaskListener {

    Logger log = LoggerFactory.getLogger(HelloWorldFlowableHttpTask.class);

    @Override
    public void notify(DelegateTask delegateTask) {

        log.info("Hello Flowable Listener notification:Http Client");
    }
}
