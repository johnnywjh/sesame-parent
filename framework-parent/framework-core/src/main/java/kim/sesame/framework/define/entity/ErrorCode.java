package kim.sesame.framework.define.entity;

import lombok.Data;

@Data
public class ErrorCode {
    private int code;
    private String name;
    private String message;

    public ErrorCode(int code, String name, String message) {
        this.code = code;
        this.name = name;
        this.message = message;
    }
}
