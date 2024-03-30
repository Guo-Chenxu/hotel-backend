package com.hotel.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 顾客空调关系类
 *
 * @author: 郭晨旭
 * @create: 2024-03-30 16:08
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class CustomerAC implements Serializable {
    private static final long serialVersionUID = 78134902L;

    // todo 详细内容
}
