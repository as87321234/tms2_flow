package ca.mgis.tms2flow.controller;

import ca.mgis.tms2flow.controller.pojo.BiometricEnrolment;
import ca.mgis.tms2flow.service.HelloFlowableService;
import jakarta.servlet.http.HttpServletResponse;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/flow-api")

public class BiometricCollectionController {

    Logger log = LoggerFactory.getLogger(BiometricCollectionController.class);

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    private HelloFlowableService service;

    @RequestMapping(value="/send-biometric-collection", method = RequestMethod.POST)
    public  @ResponseBody
    void sendBiometricCollection(HttpServletResponse response, @RequestBody BiometricEnrolment biometricEnrolment ) {

        log.info(String.format("Biometric Enrolment received: %s", biometricEnrolment ));
        log.debug(biometricEnrolment.toString());

        Map<String, Object> map = new HashMap<String, Object>();

        map.put(BiometricEnrolment.class.getName(), biometricEnrolment);

        ProcessInstance instance = runtimeService.startProcessInstanceByKey("id-enrolment-process-tre", map);

    }

    @RequestMapping(value="/wait-user-response", method = RequestMethod.POST)
    public  @ResponseBody
    void waitUserResponse(HttpServletResponse response) {

        log.info(String.format("Hello World Wait for user response"));

        ProcessInstance instance = runtimeService.startProcessInstanceByKey("ca.mgis.tms2flow.processes.helloworld");

    }

//    @PostMapping("/submit")
//    public void submit(@RequestBody Article article) {
//        service.startProcess(article);
//    }
//
//    @GetMapping("/tasks")
//    public List<Article> getTasks(@RequestParam String assignee) {
//        return service.getTasks(assignee);
//    }

    //    @PostMapping("/review")
//    public void review(@RequestBody Approval approval) {
//        service.submitReview(approval);
//    }
}
