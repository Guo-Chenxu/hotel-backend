package com.hotel.server.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.R;
import com.hotel.common.dto.request.SaveFoodReq;
import com.hotel.common.dto.request.SaveStaffReq;
import com.hotel.common.dto.response.PageFoodResp;
import com.hotel.common.dto.response.PageStaffResp;
import com.hotel.common.entity.Food;
import com.hotel.common.service.server.FoodService;
import com.hotel.server.annotation.CheckPermission;
import com.hotel.server.mapper.FoodMapper;
import com.hotel.server.service.QiniuCloudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
@Api(tags = "食物表(Food)表控制层")
// @CrossOrigin
public class FoodController {
    @DubboReference
    private FoodService foodService;

    @Resource
    private QiniuCloudService qiniuCloudService;

    @PostMapping("/saveFood")
    @ApiOperation("新增食物")
    @SaCheckLogin
    @CheckPermission({Permission.FOOD})
    public R saveFood(@RequestBody SaveFoodReq saveFoodReq) {
        return foodService.saveFood(saveFoodReq)
                ? R.success()
                : R.error();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    @SaCheckLogin
    @CheckPermission({Permission.FOOD})
    public R<Page<PageFoodResp>> pageFood(@RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
                                           @RequestParam(value = "pageSize", defaultValue = "20")  @Min(1) @Max(100) Integer pageSize) {
        return R.success(foodService.pageFood(page, pageSize));
    }

    @PostMapping("/deleteFood")
    @ApiOperation("删除食物")
    @SaCheckLogin
    @CheckPermission({Permission.FOOD})
    public R deleteFood(@RequestBody List<String> ids) {
        return foodService.deleteFood(ids)
                ? R.success()
                : R.error();
    }

    @PostMapping("/upload")
    @ApiOperation("上传食物照片")
    @SaCheckLogin
    @CheckPermission({Permission.FOOD})
    public R upload(@RequestParam("file") @NotNull MultipartFile file) {
        int idx = Optional.ofNullable(file.getOriginalFilename()).orElse(".").lastIndexOf('.');
        String imageName = "hotel/" + UUID.randomUUID().toString().replace("-", "")
                + file.getOriginalFilename().substring(idx);
        String url = qiniuCloudService.fileUpload(file, imageName);
        if (StringUtils.isBlank(url)) {
            return R.error("文件上传失败");
        }
        return R.success(url);
    }
}

