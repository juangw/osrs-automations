package com.api;

import java.util.Arrays;
import java.util.logging.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableSwagger2
public class Application {

  private final static Logger LOGGER = Logger.getLogger(Application.class.getName());

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {

      LOGGER.info("Starting up API");

      String[] beanNames = ctx.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        LOGGER.info(beanName);
      }

    };
  }

  @Bean
  public Docket productApi() {
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.basePackage("com.api")).build();
  }
}