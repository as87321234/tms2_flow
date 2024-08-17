package ca.mgis.tms2flow.controller.pojo;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class BiometricEnrolment implements Serializable {

    String biometricId;
    String enrolmentBase64;

}
