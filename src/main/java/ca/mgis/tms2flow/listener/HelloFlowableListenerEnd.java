package ca.mgis.tms2flow.listener;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloFlowableListenerEnd
        implements ExecutionListener {

    Logger log = LoggerFactory.getLogger(HelloFlowableListenerEnd.class);

    @Override
    public void notify(DelegateExecution execution) {

        log.info("Hello Flowable Listener notification: End Event");

    }
}
