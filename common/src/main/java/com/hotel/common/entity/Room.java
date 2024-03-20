package com.hotel.common.entity;


import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;


/**
 * (Room)表实体类
 *
 * @author: guochenxu
 * @create: 2024-03-20 10:32:18
 * @version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 价格/晚
     */

    private String price;

    /**
     * 房间当前温度
     */
    private Double temperature;


    /**
     * 软删除
     */
    private Boolean deleted;
}

