package ca.mgis.tms2flow.controller;

import ca.mgis.tms2flow.service.HelloFlowableService;
import jakarta.servlet.http.HttpServletResponse;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/flow-api")

public class HelloFlowableController {

    Logger log = LoggerFactory.getLogger(HelloFlowableController.class);

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    private HelloFlowableService service;

    @RequestMapping(value="/hello-world", method = RequestMethod.POST)
    public  @ResponseBody
    void helloWorld(HttpServletResponse response,  @RequestParam("test") String testParam ) {

        log.info(String.format("Hello World Service: %s",testParam));

        Map<String, Object> map = new HashMap<>();
        map.put("action","my action");

        ProcessInstance instance = runtimeService.startProcessInstanceByKey("ca.mgis.tms2flow.processes.helloworld",testParam, map);

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
