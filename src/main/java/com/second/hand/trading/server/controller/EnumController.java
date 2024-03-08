package com.second.hand.trading.server.controller;

import com.second.hand.trading.server.enums.Degree;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @Description
 * @Author:luosonglai
 * @CreateTime:2024/3/717:56
 */
@Slf4j
@RestController
@RequestMapping(value = "/enums")
@Api(value = "enums", tags = "状态枚举")
public class EnumController {


    @GetMapping("")
    public Map<String, Object> getDegreeEnums() {
        Map<String, Object> response = new HashMap<>();

        List<Map<String, Object>> enumsList = new ArrayList<>();
        for (Degree degree : Degree.values()) {
            Map<String, Object> enumMap = new HashMap<>();
            enumMap.put("display", degree.getDisplay());
            enumMap.put("value", degree.getValue()); // 添加数字类型字段
            enumsList.add(enumMap);
        }

        response.put("enums", enumsList);
        response.put("name", "Degree");

        return response;
    }



}





