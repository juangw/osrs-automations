package com.api.routes;

import com.api.Application;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class Automations {
    private final static Logger LOGGER = Logger.getLogger(Application.class.getName());

    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "OK";
    }

}
