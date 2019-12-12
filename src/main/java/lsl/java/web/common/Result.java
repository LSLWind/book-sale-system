package lsl.java.web.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Select;

/**
 * ajax响应的返回对象
 */
@Getter
@Setter
public class Result<T> {
    public static final int OK=200;
    public static final int CLIENT_ERROR=400;
    public static final int SERVER_ERROR=500;

    private String message;//附加消息
    private int statusCode;//状态码
    private T data;//传输对象

    public Result(String message,int statusCode){
        this.message=message;
        this.statusCode=statusCode;
    }
    public Result(String message,int statusCode,T data){
        this.message=message;
        this.statusCode=statusCode;
        this.data=data;
    }
}
