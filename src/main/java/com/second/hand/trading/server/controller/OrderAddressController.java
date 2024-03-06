package com.second.hand.trading.server.controller;

import com.second.hand.trading.server.enums.ErrorMsg;
import com.second.hand.trading.server.model.OrderAddressModel;
import com.second.hand.trading.server.service.OrderAddressService;
import com.second.hand.trading.server.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/order-address")
@Api(tags = "订单地址管理")
public class OrderAddressController {

    @Autowired
    private OrderAddressService orderAddressService;

    @PostMapping("/add")
    @ApiOperation("添加订单地址")
    public ResultVo addOrderAddress(@CookieValue("shUserId")
                                        @NotNull(message = "登录异常 请重新登录")
                                        @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                    @RequestBody OrderAddressModel orderAddressModel){
        return ResultVo.success(orderAddressService.addOrderAddress(orderAddressModel));
    }

    @PostMapping("/update")
    @ApiOperation("更改订单地址")
    public ResultVo updateOrderAddress(@CookieValue("shUserId")
                                       @NotNull(message = "登录异常 请重新登录")
                                       @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                       @RequestBody OrderAddressModel orderAddressModel){
        if(orderAddressService.updateOrderAddress(orderAddressModel)){
            return ResultVo.success(orderAddressModel);
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("/info")
    @ApiOperation("获取订单地址管理信息")
    public ResultVo getOrderAddress(@CookieValue("shUserId")
                                    @NotNull(message = "登录异常 请重新登录")
                                    @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                    @RequestParam Long orderId){
        return ResultVo.success(orderAddressService.getOrderAddress(orderId));
    }
}
