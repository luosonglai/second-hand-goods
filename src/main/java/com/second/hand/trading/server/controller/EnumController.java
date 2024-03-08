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
    @ApiOperation("获取所有枚举")
    public List<Map<String, Object>> allEnums() {
        List<Map<String, Object>> result = new ArrayList<>();

        // 获取所有枚举类
        Class<?>[] enumClasses = {Degree.class}; // 在此处添加你要获取的枚举类

        // 遍历枚举类
        for (Class<?> enumClass : enumClasses) {
            Map<String, Object> enumMap = new HashMap<>();
            enumMap.put("name", enumClass.getSimpleName());

            List<Map<String, Object>> enums = new ArrayList<>();
            // 获取枚举值
            Object[] enumConstants = enumClass.getEnumConstants();
            for (Object constant : enumConstants) {
                try {
                    Field displayField = constant.getClass().getDeclaredField("display");
                    Field valueField = constant.getClass().getDeclaredField("value"); // 添加数字类型的字段
                    displayField.setAccessible(true);
                    valueField.setAccessible(true);

                    String display = (String) displayField.get(constant);
                    int value = (int) valueField.get(constant); // 读取数字类型的值

                    Map<String, Object> enumInfo = new HashMap<>();
                    enumInfo.put("display", display);
                    enumInfo.put("value", value); // 添加数字类型的值到Map中

                    enums.add(enumInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            enumMap.put("enums", enums);
            result.add(enumMap);
        }
        return result;
    }

}
