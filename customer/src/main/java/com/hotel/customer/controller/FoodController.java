package com.hotel.customer.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.R;
import com.hotel.common.dto.request.OrderFoodReq;
import com.hotel.common.dto.request.SaveFoodReq;
import com.hotel.common.dto.response.PageFoodResp;
import com.hotel.common.entity.CustomerFood;
import com.hotel.common.service.server.FoodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * 食物表(Food)表控制层
 *
 * @author: guochenxu
 * @create: 2024-03-24 14:01:11
 * @version: 1.0
 */
@RestController
@RequestMapping("food")
@Slf4j
@Api(tags = "食物控制接口")
public class FoodController {
    @DubboReference(check = false)
    private FoodService foodService;

    @GetMapping("/page")
    @ApiOperation("分页查询")
    @SaCheckLogin
    public R<Page<PageFoodResp>> pageStaff(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        return R.success(foodService.pageFood(page, pageSize));
    }

    @GetMapping("/history")
    @ApiOperation("查询历史订单")
    @SaCheckLogin
    public R<List<CustomerFood>> history() {
        return R.success(foodService.history(StpUtil.getLoginIdAsString()));
    }

    @PostMapping("/order")
    @ApiOperation("下单")
    @SaCheckLogin
    public R orderFood(@RequestBody OrderFoodReq orderFoodReq) {
        return foodService.saveOrder(StpUtil.getLoginIdAsString(), orderFoodReq)
                ? R.success("下单成功")
                : R.error();

    }
}
