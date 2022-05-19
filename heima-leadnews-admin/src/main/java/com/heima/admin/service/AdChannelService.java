package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.ChannelDto;
import com.heima.model.admin.pojos.AdChannel;
import com.heima.model.common.dtos.ResponseResult;

public interface AdChannelService extends IService<AdChannel> {


    public ResponseResult findByNameAndPage(ChannelDto dto);

    ResponseResult saveAdChannel(AdChannel channel);

    ResponseResult updateAdChannel(AdChannel channel);

    ResponseResult deleteAdChannel(Integer id);
}
