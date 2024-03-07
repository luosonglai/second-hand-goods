package com.second.hand.trading.server.controller;

import com.second.hand.trading.server.enums.ErrorMsg;
import com.second.hand.trading.server.model.IdleItemModel;
import com.second.hand.trading.server.service.IdleItemService;
import com.second.hand.trading.server.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RestController
@RequestMapping("idle")
@Api(tags = "闲置物品管理")
public class IdleItemController {

    @Autowired
    private IdleItemService idleItemService;

    @PostMapping("add")
    @ApiOperation("新增闲置物品")
    public ResultVo addIdleItem(@CookieValue("shUserId")
                                @NotNull(message = "登录异常 请重新登录")
                                @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                @RequestBody IdleItemModel idleItemModel) {
        idleItemModel.setUserId(Long.valueOf(shUserId));
        idleItemModel.setIdleStatus((byte) 1);
        idleItemModel.setReleaseTime(new Date());
        if (idleItemService.addIdleItem(idleItemModel)) {
            return ResultVo.success(idleItemModel);
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("info")
    @ApiOperation("获取单个闲置物品信息")
    public ResultVo getIdleItem(@RequestParam Long id) {
        return ResultVo.success(idleItemService.getIdleItem(id));
    }

    @GetMapping("all")
    @ApiOperation("获取所有闲置物品信息")
    public ResultVo getAllIdleItem(@CookieValue("shUserId")
                                   @NotNull(message = "登录异常 请重新登录")
                                   @NotEmpty(message = "登录异常 请重新登录") String shUserId) {
        return ResultVo.success(idleItemService.getAllIdelItem(Long.valueOf(shUserId)));
    }

    @GetMapping("find")
    @ApiOperation("搜索闲置")
    public ResultVo findIdleItem(@RequestParam(value = "findValue", required = false) String findValue,
                                 @RequestParam(value = "page", required = false) Integer page,
                                 @RequestParam(value = "nums", required = false) Integer nums) {
        if (null == findValue) {
            findValue = "";
        }
        int p = 1;
        int n = 8;
        if (null != page) {
            p = page > 0 ? page : 1;
        }
        if (null != nums) {
            n = nums > 0 ? nums : 8;
        }
        return ResultVo.success(idleItemService.findIdleItem(findValue, p, n));
    }


    @GetMapping("recommended")
    @ApiOperation("推荐算法获取推荐列表")
    public ResultVo getRecommendedBooks(@CookieValue("shUserId")
                                        @NotNull(message = "登录异常 请重新登录")
                                        @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                        @RequestParam(value = "page", required = false) Integer page,
                                        @RequestParam(value = "nums", required = false) Integer nums) {
        int p = 1;
        int n = 8;
        if (null != page) {
            p = page > 0 ? page : 1;
        }
        if (null != nums) {
            n = nums > 0 ? nums : 8;
        }
        return ResultVo.success(idleItemService.getRecommendedBooks(Long.valueOf(shUserId),p,n));
    }

    @GetMapping("lable")
    @ApiOperation("按分类获取闲置")
    public ResultVo findIdleItemByLable(@RequestParam(value = "idleLabel", required = true) Integer idleLabel,
                                        @RequestParam(value = "page", required = false) Integer page,
                                        @RequestParam(value = "nums", required = false) Integer nums) {
        int p = 1;
        int n = 8;
        if (null != page) {
            p = page > 0 ? page : 1;
        }
        if (null != nums) {
            n = nums > 0 ? nums : 8;
        }
        return ResultVo.success(idleItemService.findIdleItemByLable(idleLabel, p, n));
    }


    @PostMapping("update")
    @ApiOperation("更新闲置状态信息")
    public ResultVo updateIdleItem(@CookieValue("shUserId")
                                   @NotNull(message = "登录异常 请重新登录")
                                   @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                   @RequestBody IdleItemModel idleItemModel) {
        idleItemModel.setUserId(Long.valueOf(shUserId));
        if (idleItemService.updateIdleItem(idleItemModel)) {
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }
}
