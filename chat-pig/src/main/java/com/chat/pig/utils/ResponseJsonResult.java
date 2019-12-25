package com.chat.pig.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 描述: 自定义响应数据结构
 *    200：表示成功
 *    500：表示错误，错误信息在msg字段中
 *    501：bean验证错误，不管多少个错误都以map形式返回
 *    502：拦截器拦截到用户token出错
 *    555：异常抛出信息
 *
 * @author li
 * @date 2019/12/25
 */
@Data
@AllArgsConstructor
public class ResponseJsonResult {

    /**
     * 响应业务状态
     */
     private Integer status;

     /**
      * 响应消息
      */
      private String msg;

    /**
     * 响应数据
     */
    private Object data;

    public static ResponseJsonResult build(Integer status,String msg,Object data){
        return new ResponseJsonResult(status,msg,data);
    }

    public static ResponseJsonResult ok(Object data) {
        return new ResponseJsonResult(200,"OK",data);
    }

    public static ResponseJsonResult ok() {
        return new ResponseJsonResult(200,"OK",null);
    }
    
    public static ResponseJsonResult errorMsg(String msg) {
        return new ResponseJsonResult(500, msg, null);
    }

    public static ResponseJsonResult errorMap(Object data) {
        return new ResponseJsonResult(501, "error", data);
    }

    public static ResponseJsonResult errorTokenMsg(String msg) {
        return new ResponseJsonResult(502, msg, null);
    }

    public static ResponseJsonResult errorException(String msg) {
        return new ResponseJsonResult(555, msg, null);
    } 
}
