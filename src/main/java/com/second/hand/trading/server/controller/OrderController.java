package com.second.hand.trading.server.controller;

import com.second.hand.trading.server.enums.ErrorMsg;
import com.second.hand.trading.server.model.OrderModel;
import com.second.hand.trading.server.service.OrderService;
import com.second.hand.trading.server.utils.IdFactoryUtil;
import com.second.hand.trading.server.utils.OrderTaskHandler;
import com.second.hand.trading.server.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RestController
@RequestMapping("/order")
@Api(tags = "订单管理")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    @ApiOperation("添加订单")
    public ResultVo addOrder(@CookieValue("shUserId")
                             @NotNull(message = "登录异常 请重新登录")
                             @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                             @RequestBody OrderModel orderModel){
        if(OrderTaskHandler.orderService==null){
            OrderTaskHandler.orderService=orderService;
        }
        orderModel.setOrderNumber(IdFactoryUtil.getOrderId());
        orderModel.setCreateTime(new Date());
        orderModel.setUserId(Long.valueOf(shUserId));
        orderModel.setOrderStatus((byte) 0);
        orderModel.setPaymentStatus((byte)0);
        if(orderService.addOrder(orderModel)){
            return ResultVo.success(orderModel);
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("/info")
    @ApiOperation("获取订单信息")
    public ResultVo getOrderInfo(@CookieValue("shUserId")
                                 @NotNull(message = "登录异常 请重新登录")
                                 @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                 @RequestParam Long id){
        OrderModel orderModel=orderService.getOrder(id);
        if(orderModel.getUserId().equals(Long.valueOf(shUserId))||
                orderModel.getIdleItem().getUserId().equals(Long.valueOf(shUserId))){
            return ResultVo.success(orderModel);
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @PostMapping("/update")
    @ApiOperation("更新订单信息")
    public ResultVo updateOrder(@CookieValue("shUserId")
                             @NotNull(message = "登录异常 请重新登录")
                             @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                             @RequestBody OrderModel orderModel){
        if(orderModel.getPaymentStatus()!=null&&orderModel.getPaymentStatus().equals((byte) 1)){
            orderModel.setPaymentTime(new Date());
        }
        if(orderService.updateOrder(orderModel)){
            return ResultVo.success(orderModel);
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("/my")
    @ApiOperation("获取某个用户买到的闲置的订单列表")
    public ResultVo getMyOrder(@CookieValue("shUserId")
                                 @NotNull(message = "登录异常 请重新登录")
                                 @NotEmpty(message = "登录异常 请重新登录") String shUserId){
        return ResultVo.success(orderService.getMyOrder(Long.valueOf(shUserId)));
    }

    @GetMapping("/my-sold")
    @ApiOperation("获取某个用户卖出的闲置的订单信息")
    public ResultVo getMySoldIdle(@CookieValue("shUserId")
                               @NotNull(message = "登录异常 请重新登录")
                               @NotEmpty(message = "登录异常 请重新登录") String shUserId){
        return ResultVo.success(orderService.getMySoldIdle(Long.valueOf(shUserId)));
    }
}
