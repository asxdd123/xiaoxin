package com.heima.api.admin;

import com.heima.model.admin.dtos.ChannelDto;
import com.heima.model.admin.pojos.AdChannel;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "频道管理", tags = "channel", description = "频道管理API")
public interface AdChannelControllerApi {

    @ApiOperation("频道分页列表查询")
    public ResponseResult findByNameAndPage(ChannelDto dto);


    @ApiOperation("频道新增")
    public ResponseResult saveAdChannel(AdChannel channel);


    @ApiOperation("频道修改")
    public ResponseResult updateAdChannel(AdChannel channel);


    @ApiOperation("频道删除")
    public ResponseResult deleteAdChannel(Integer id);
}