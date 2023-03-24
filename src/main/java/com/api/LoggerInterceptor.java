package com.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoggerInterceptor implements HandlerInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoggerInterceptor.class);

  @Override
  public boolean preHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler) throws Exception {
    switch (request.getMethod()) {
      case "POST":
        LOGGER.info("[request][" + request.getRequestURI() + "] " + "[" + request.getMethod()
            + "] " + new ObjectMapper().writeValueAsString(request.getParameterMap()));
        break;
      case "GET":
      default:
        LOGGER.info("[request][" + request.getRequestURI() + "] " + "[" + request.getMethod()
            + "] ");
        break;
    }
    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    if (ex != null) {
      ex.printStackTrace();
      LOGGER.error("[response][" + request.getRequestURI() + "][exception: " + ex + "]");
      return;
    }
    LOGGER.info(
        "[response][" + request.getRequestURI() + "][status: " + response.getStatus() + "]");
  }
}