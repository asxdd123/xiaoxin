package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdChannelMapper;
import com.heima.admin.service.AdChannelService;
import com.heima.model.admin.dtos.ChannelDto;
import com.heima.model.admin.pojos.AdChannel;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AdChannelServiceImpl extends ServiceImpl<AdChannelMapper, AdChannel> implements AdChannelService {


    @Override
    public ResponseResult findByNameAndPage(ChannelDto dto) {

        //1.参数检测
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //分页参数检查
        dto.checkParam();
        //2.按照名称模糊分页查询
        Page page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<AdChannel> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(dto.getName())) {
            lambdaQueryWrapper.like(AdChannel::getName, dto.getName());
        }
        IPage result = page(page, lambdaQueryWrapper);
        //3.结果封装
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) result.getTotal());
        responseResult.setData(result.getRecords());
        return responseResult;
    }


    /**
     * 新增
     * @param channel
     * @return
     */
    @Override
    public ResponseResult saveAdChannel(AdChannel channel) {
        if(channel == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        channel.setCreatedTime(new Date());
        save(channel);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 编辑
     * @param channel
     * @return
     */
    @Override
    public ResponseResult updateAdChannel(AdChannel channel) {
        if(channel == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        UpdateWrapper<AdChannel> wrapper = new UpdateWrapper<>();
        wrapper.eq("name",channel.getName());
        update(channel,wrapper);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public ResponseResult deleteAdChannel(Integer id) {
        if(id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
