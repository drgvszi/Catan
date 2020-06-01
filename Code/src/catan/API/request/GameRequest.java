package catan.API.request;

import catan.API.response.GameResponse;
import catan.util.RandomString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface GameRequest {
    RandomString randomString = new RandomString();

    GameResponse run() throws JsonProcessingException;

    static Map<String, String> getMapFromData(String data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (data != null && !data.equals("")) {
                return mapper.readValue(data, new TypeReference<HashMap<String, String>>() {
                });
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
