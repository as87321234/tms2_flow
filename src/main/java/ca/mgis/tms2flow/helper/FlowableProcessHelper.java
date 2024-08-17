package ca.mgis.tms2flow.helper;

import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ExecutionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlowableProcessHelper {

    Logger log = LoggerFactory.getLogger(FlowableProcessHelper.class);

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    RuntimeService rt;

    public Execution getExecutionByActivityForParentExecution(String processInstanceId, String activityId) {

        RuntimeService r = processEngine.getRuntimeService();
        ExecutionQuery eq = r.createExecutionQuery();
        return eq.processInstanceId(processInstanceId).activityId(activityId).singleResult();
    }

    public void triggerExecution(String processInstanceId, String activityId) {

        ExecutionEntityImpl e = (ExecutionEntityImpl) this.getExecutionByActivityForParentExecution(processInstanceId, activityId);

        if (e != null) {

            try {
                rt.triggerAsync(e.getId());
            } catch (FlowableException fe) {
                log.info(String.format("%s, %s - " + fe.getMessage(), activityId, processInstanceId));
            }
        } else {
            log.info(String.format("%s, %s  - Task already completed:  ",  processInstanceId, activityId));
        }
    }

    public String logFormatter(DelegateExecution execution, String message) {

        ExecutionEntityImpl ei = (ExecutionEntityImpl) execution;
        return String.format("%s %s, %s, %s - %s",ei.getRootProcessInstanceId(), ei.getId(),ei.getActivityName() ,ei.getEventName(), message);
    }
}
