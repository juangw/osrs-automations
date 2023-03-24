package com.api.routes;

import java.util.logging.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class Healthcheck {

  private final static Logger LOGGER = Logger.getLogger(Array.class.getName());

  // curl localhost:8080/healthcheck
  @RequestMapping(method = RequestMethod.GET)
  public String healthcheck() {
    return "OK";
  }
}
