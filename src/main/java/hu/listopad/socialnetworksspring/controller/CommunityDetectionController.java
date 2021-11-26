package hu.listopad.socialnetworksspring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommunityDetectionController {

    @GetMapping("/graphs")
    public String index(){
        return "tadááámmmm";
    }
    
}
