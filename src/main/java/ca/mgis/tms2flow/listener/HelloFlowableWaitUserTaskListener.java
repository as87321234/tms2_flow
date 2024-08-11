package ca.mgis.tms2flow.listener;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloFlowableWaitUserTaskListener
        implements ExecutionListener {

    Logger log = LoggerFactory.getLogger(HelloFlowableWaitUserTaskListener.class);

    @Override
    public void notify(DelegateExecution execution) {

        log.info("Hello Flowable Listener notification: Wait User Task");

    }
}
