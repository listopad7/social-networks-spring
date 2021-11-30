package hu.listopad.socialnetworksspring.controller;

import hu.listopad.socialnetworksspring.controller.CommunityDetectionController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CommunityDetectionControllerTest {

    String testString = "{name:test}";
    String postResult="Working on it";

    @Autowired
    CommunityDetectionController communityDetectionController;

    @ParameterizedTest
    @ValueSource(strings = {"{name:testName}"})
    public void postControllerTest(String payload){

        assertEquals(postResult, communityDetectionController.newCalculation(payload));

    }

}
