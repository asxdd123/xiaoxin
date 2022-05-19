package com.heima.model.admin.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * 前端对象,用于和后端对象作属性比较用
 */
@Data
public class ChannelDto extends PageRequestDto {

    /**
     * 频道名称
     */
    private String name;

}
