package com.hotel.common.entity;


import java.io.Serializable;
import java.util.Date;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;


/**
 * 酒店服务人员表(Staff)表实体类
 *
 * @author: guochenxu
 * @create: 2024-03-19 22:09:21
 * @version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 创建日期
     */

    private Date createAt;
    /**
     * 用户名
     */

    private String username;
    /**
     * 员工密码
     */

    private String password;
    /**
     * 权限
     */

    private String permission;

    /**
     * 软删除
     */
    private Boolean deleted;

    /**
     * 密码加密
     */
    public void encryptPassword() {
        if (StringUtils.isBlank(this.password)) {
            throw new RuntimeException("密码不能为空");
        }
        this.password = DigestUtil.md5Hex(this.password);
    }

    /**
     * 密码校验
     */
    public boolean checkPassword(String password) {
        if (StringUtils.isBlank(password) || StringUtils.isBlank(this.password)) {
            return false;
        }
        return DigestUtil.md5Hex(password).equals(this.password);
    }
}

