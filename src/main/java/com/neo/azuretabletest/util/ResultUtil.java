package com.neo.azuretabletest.util;

import com.neo.azuretabletest.entity.ResultEntity;

/**
 * Created by NeoJiang on 2018/9/4.
 */
public class ResultUtil {

    public static ResultEntity success(Object object){
        ResultEntity result=new ResultEntity();
        result.setCode(200);
        result.setMsg("success");
        result.setData(object);
        return result;
    }
    public static ResultEntity success(){
        return success(null);
    }

    public static ResultEntity error(Integer code,String resultMsg){
        ResultEntity result=new ResultEntity();
        result.setCode(code);
        result.setMsg(resultMsg);
        return result;
    }

}
