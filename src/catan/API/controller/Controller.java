package catan.API.controller;

import catan.API.request.ManagerRequest;
import catan.API.request.UserRequest;
import catan.API.response.ManagerResponse;
import catan.API.response.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Catan")
public class Controller {
    private static final String username = "catan";
    private static final String password = "catan";

    @RequestMapping(value = "/userRequest", method = RequestMethod.POST)
    public UserResponse sendResponse(@RequestBody UserRequest request) {
        return request.run();
    }

    @RequestMapping(value = "/managerRequest", method = RequestMethod.POST)
    public ManagerResponse sendResponse(@RequestBody ManagerRequest request) {
        if (request.getUsername().equals(username) && request.getPassword().equals(password)) {
            try {
                return request.run();
            } catch (JsonProcessingException exception) {
                exception.printStackTrace();
                return null;
            }
        }
        return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The credentials are wrong.", "");
    }
}
