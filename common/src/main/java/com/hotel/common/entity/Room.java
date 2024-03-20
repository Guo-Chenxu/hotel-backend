package com.hotel.common.entity;


import java.io.Serializable;

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
    // todo 设成自增id
    private Long id;
    /**
     * 房间号
     */
    // todo 删除字段
    private Integer no;
    /**
     * 房型
     */

    private String type;
    /**
     * 价格/晚
     */

    private String price;
    /**
     * 状态 0-空 1-有人
     */

    private Boolean status;

    /**
     * 房间当前温度
     */
    private Double temperature;
}

