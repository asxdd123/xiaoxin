package com.heima.user.controller.v1;

import com.heima.api.admin.ApUserRealnameControllerApi;
import com.heima.common.constants.user.AdminConstans;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dto.AuthDto;
import com.heima.user.service.ApUserRealnameService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@MapperScan("com.heima.user.mapper.ApUserRealnameMapper")
@RequestMapping("/api/v1/auth")
public class ApUserRealnameController implements ApUserRealnameControllerApi {

    @Autowired
    private ApUserRealnameService userRealnameService;


    @PostMapping("/list")
    @Override
    public ResponseResult loadListByStatus(@RequestBody AuthDto dto){
        return userRealnameService.loadListByStatus(dto);
    }


    /**
     * 审核通过
     * @param dto
     * @return
     */
    @PostMapping("/authPass")
    @Override
    public ResponseResult authPass(@RequestBody AuthDto dto) {
        return userRealnameService.updateStatusById(dto, AdminConstans.PASS_AUTH);
    }


    /**
     * 审核失败
     * @param dto
     * @return
     */
    @PostMapping("/authFail")
    @Override
    public ResponseResult authFail(@RequestBody AuthDto dto) {
        return userRealnameService.updateStatusById(dto, AdminConstans.FAIL_AUTH);
    }
}