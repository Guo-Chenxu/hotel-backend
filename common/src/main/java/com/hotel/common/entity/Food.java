package com.hotel.common.entity;


import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;


/**
 * 食物表(Food)表实体类
 *
 * @author: guochenxu
 * @create: 2024-03-24 14:01:11
 * @version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 食物名
     */

    private String name;
    /**
     * 价格
     */

    private String price;
    /**
     * 照片
     */

    private String img;

    /**
     * 软删除
     */
    private Boolean deleted;
}

