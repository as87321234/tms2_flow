package ca.mgis.tms2flow.controller;

import ca.mgis.tms2flow.controller.pojo.BiometricEnrolment;
import jakarta.servlet.http.HttpServletResponse;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/flow-api")

public class BiometricCollectionController {

    private final Logger log = LoggerFactory.getLogger(BiometricCollectionController.class);

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @RequestMapping(value = "/send-biometric-collection", method = RequestMethod.POST)
    public @ResponseBody
    void sendBiometricCollection(HttpServletResponse response, @RequestBody BiometricEnrolment biometricEnrolment) {

        log.info(String.format("Biometric Enrolment received: %s", biometricEnrolment));
        log.debug(biometricEnrolment.toString());

        Map<String, Object> map = new HashMap<String, Object>();

        map.put(BiometricEnrolment.class.getName(), biometricEnrolment);

        runtimeService.startProcessInstanceByKey("id-enrolment-process-tre", map);

    }
}
