package com.api.routes;

import com.api.Application;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Automations {

  private final static Logger LOGGER = Logger.getLogger(Application.class.getName());

  @GetMapping("/healthcheck")
  public String healthcheck() {
    return "OK";
  }

}
