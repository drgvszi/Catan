package catan.API.controller;

import catan.API.request.ManagerRequest;
import catan.API.request.UserRequest;
import catan.API.response.ManagerResponse;
import catan.API.response.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Catan")
public class Controller {
    private static final String username = "catan";
    private static final String password = "catan";

    @PostMapping(value = "/userRequest")
    public UserResponse processRequest(@RequestBody UserRequest request) {
        return request.run();
    }

    @PostMapping(value = "/managerRequest")
    public ManagerResponse processRequest(@RequestBody ManagerRequest request) {
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
