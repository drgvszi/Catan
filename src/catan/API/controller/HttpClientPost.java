package catan.API.controller;

import catan.API.request.GameRequest;
import catan.API.response.ManagerResponse;
import catan.API.response.UserResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpClientPost {
    private static final String localhostManagerUrl = "http://localhost:8080/Catan/managerRequest/";
    private static final String localhostUserUrl = "http://localhost:8080/Catan/userRequest/";

    private static final String herokuManagerUrl = "https://catan-engine.herokuapp.com/Catan/managerRequest/";
    private static final String herokuUserUrl = "https://catan-engine.herokuapp.com/Catan/userRequest/";

    public static ManagerResponse managerPost(GameRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString((request)));
        String requestJson = objectMapper.writeValueAsString(request);
        URL url = new URL(localhostManagerUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = requestJson.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }
        try (BufferedReader bufferedReader = new BufferedReader
                (new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = bufferedReader.readLine();
            while (responseLine != null) {
                response.append(responseLine.trim());
                responseLine = bufferedReader.readLine();
            }
            Map<String, String> responseJson = objectMapper.readValue(response.toString(),
                    new TypeReference<HashMap<String, String>>() {
                    });
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString((responseJson)));
            return new ManagerResponse(Integer.valueOf(responseJson.get("code")), responseJson.get("status"),
                    responseJson.get("arguments"));
        }
    }

    public static UserResponse userPost(GameRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString((request)));
        String requestJson = objectMapper.writeValueAsString((request));
        URL url = new URL(localhostUserUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = requestJson.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }
        try (BufferedReader bufferedReader = new BufferedReader
                (new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = bufferedReader.readLine();
            while (responseLine != null) {
                response.append(responseLine.trim());
                responseLine = bufferedReader.readLine();
            }
            Map<String, Object> responseJson = objectMapper.readValue(response.toString(),
                    new TypeReference<>() {
                    });
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString((responseJson)));
            return new UserResponse((Integer) responseJson.get("code"), (String) responseJson.get("status"),
                    objectMapper.convertValue(responseJson.get("arguments"),
                            new TypeReference<HashMap<String, Object>>() {
                            }));
        }
    }
}
