package com.heima.admin.controller.v1;

import com.heima.admin.service.AdChannelService;
import com.heima.api.admin.AdChannelControllerApi;
import com.heima.model.admin.dtos.ChannelDto;
import com.heima.model.admin.pojos.AdChannel;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/channel")
public class AdChannelController implements AdChannelControllerApi {

    @Autowired
    private AdChannelService channelService;

    /**
     * 根据名称分页查询频道列表
     *
     * @param dto
     * @return
     */
    @PostMapping("/list")
    @Override
    public ResponseResult findByNameAndPage(@RequestBody ChannelDto dto) {
        return channelService.findByNameAndPage(dto);
    }


    /**
     * 新增
     *
     * @param channel
     * @return
     */
    @PostMapping("/save")
    @Override
    public ResponseResult saveAdChannel(@RequestBody AdChannel channel) {
        return channelService.saveAdChannel(channel);
    }

    /**
     * 修改
     *
     * @param channel
     * @return
     */
    @PostMapping("/update")
    @Override
    public ResponseResult updateAdChannel(@RequestBody AdChannel channel) {
        return channelService.updateAdChannel(channel);
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    @PostMapping("/del")
    public ResponseResult deleteAdChannel(@PathVariable("id") Integer id) {
        return channelService.deleteAdChannel(id);
    }
}
