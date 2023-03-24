package com.api.routes;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/array")
public class Array {

  private final static Logger LOGGER = Logger.getLogger(Array.class.getName());
  private final ArrayList<String> inMemoryArrayStorage = new ArrayList<String>();

  // curl localhost:8080/array
  @RequestMapping(method = RequestMethod.GET)
  public ArrayList<String> getArray() {
    return inMemoryArrayStorage;
  }

  //  curl -X POST -H "Content-type: application/json" -d "John Smith" localhost:8080/array
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<String> addToArray(@RequestBody String arrayItem) {
    if (inMemoryArrayStorage.contains(arrayItem)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(String.format("'%s' already in Array", arrayItem));
    }
    inMemoryArrayStorage.add(arrayItem);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  //  curl -X POST -H "Content-type: application/json" -d "John Smith" localhost:8080/array
  @RequestMapping(method = RequestMethod.DELETE)
  public ResponseEntity<String> removeFromArray(@RequestBody String arrayItem) {
    if (!inMemoryArrayStorage.contains(arrayItem)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(String.format("'%s' not in Array", arrayItem));
    }
    inMemoryArrayStorage.remove(arrayItem);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
