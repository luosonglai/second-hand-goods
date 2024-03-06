package com.second.hand.trading.server.controller;

import com.second.hand.trading.server.enums.ErrorMsg;
import com.second.hand.trading.server.model.FavoriteModel;
import com.second.hand.trading.server.service.FavoriteService;
import com.second.hand.trading.server.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RestController
@RequestMapping("/favorite")
@Api(tags = "收藏管理")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/add")
    @ApiOperation("添加收藏")
    public ResultVo addFavorite(@CookieValue("shUserId")
                                    @NotNull(message = "登录异常 请重新登录")
                                    @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                    @RequestBody FavoriteModel favoriteModel){
        favoriteModel.setUserId(Long.valueOf(shUserId));
        favoriteModel.setCreateTime(new Date());
        if(favoriteService.addFavorite(favoriteModel)){
            return ResultVo.success(favoriteModel.getId());
        }
        return ResultVo.fail(ErrorMsg.FAVORITE_EXIT);
    }

    @GetMapping("/delete")
    @ApiOperation("删除收藏")
    public ResultVo deleteFavorite(@CookieValue("shUserId")
                                       @NotNull(message = "登录异常 请重新登录")
                                       @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                       @RequestParam Long id){
        if(favoriteService.deleteFavorite(id)){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("/check")
    @ApiOperation("判断是否收藏")
    public ResultVo checkFavorite(@CookieValue("shUserId")
                                      @NotNull(message = "登录异常 请重新登录")
                                      @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                  @RequestParam Long idleId){
        return ResultVo.success(favoriteService.isFavorite(Long.valueOf(shUserId),idleId));
    }

    @GetMapping("/my")
    @ApiOperation("获取用户收藏列表")
    public ResultVo getMyFavorite(@CookieValue("shUserId")
                                    @NotNull(message = "登录异常 请重新登录")
                                    @NotEmpty(message = "登录异常 请重新登录") String shUserId){
        return ResultVo.success(favoriteService.getAllFavorite(Long.valueOf(shUserId)));
    }
}
