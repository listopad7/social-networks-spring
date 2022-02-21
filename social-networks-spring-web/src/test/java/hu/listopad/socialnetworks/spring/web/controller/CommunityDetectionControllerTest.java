package hu.listopad.socialnetworks.spring.web.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CommunityDetectionControllerTest {

    String testString = "{name:test}";
    String postResult="Working on it";

    @Value("${sqsUrl}")
    private String queueUrl;

    @Value("${localstack.endpoint}")
    private String endpointUrl;

    @Autowired
    CommunityDetectionController communityDetectionController;


    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {"{name:testName}"})
    public void postControllerTest(String payload){


    }
    @Test
    public void emptyTest(){

    }

}
