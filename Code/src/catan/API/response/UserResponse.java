package catan.API.response;

import java.util.Map;

public class UserResponse implements GameResponse {
    private Integer code;
    private String status;
    private Map<String, Object> arguments;

    public UserResponse(Integer code, String status, Map<String, Object> arguments) {
        this.code = code;
        this.status = status;
        this.arguments = arguments;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", arguments='" + arguments + '\'' +
                '}';
    }
}
