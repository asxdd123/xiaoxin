package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.user.AdminConstans;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.media.pojos.WmUser;
import com.heima.model.user.dto.AuthDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.pojos.ApUserRealname;
import com.heima.user.feign.ArticleFeign;
import com.heima.user.feign.WemediaFeign;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.mapper.ApUserRealnameMapper;
import com.heima.user.service.ApUserRealnameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname> implements ApUserRealnameService {

    @Autowired
    private ArticleFeign articleFeign;

    @Autowired
    private WemediaFeign wemediaFeign;

    @Autowired
    private ApUserMapper apUserMapper;


    /**
     * 按照状态分页查询用户列表
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult loadListByStatus(AuthDto dto) {
        //1.检查参数
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //分页检查
        dto.checkParam();

        //2.根据状态分页查询
        LambdaQueryWrapper<ApUserRealname> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (dto.getStatus() != null) {
            lambdaQueryWrapper.eq(ApUserRealname::getStatus, dto.getStatus());
        }
        //分页条件构建
        IPage pageParam = new Page(dto.getPage(), dto.getSize());
        IPage page = page(pageParam, lambdaQueryWrapper);

        //3.返回结果
        PageResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }


    /**
     * 根据状态进行审核
     *
     * @param dto
     * @param status
     * @return
     */
    @Override
    public ResponseResult updateStatusById(AuthDto dto, Short status) {
        //1.检查参数
        if (dto == null || dto.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //检查状态
        if (checkStatus(status)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.修改状态
        ApUserRealname apUserRealname = new ApUserRealname();
        apUserRealname.setId(dto.getId());
        apUserRealname.setStatus(status);
        if (dto.getMsg() != null) {
            apUserRealname.setReason(dto.getMsg());
        }

        updateById(apUserRealname);

        //3.如果审核状态是通过，创建自媒体账户，创建作者信息
        if (status.equals(AdminConstans.PASS_AUTH)) {
            //创建自媒体账户，创建作者信息
            ResponseResult result = createWmUserAndAuthor(dto);
            if (result != null) {
                return result;
            }
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


    /**
     * 创建自媒体账户，创建作者信息
     *
     * @param dto
     */
    private ResponseResult createWmUserAndAuthor(AuthDto dto) {
        //获取ap_user信息
        Integer apUserRealnameId = dto.getId();
        ApUserRealname apUserRealname = getById(apUserRealnameId);
        ApUser apUser = apUserMapper.selectById(apUserRealname.getUserId());
        if (apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        WmUser wmUser = wemediaFeign.findByName(apUser.getName());
        //创建自媒体账户
        if (wmUser == null) {
            wmUser = new WmUser();
            wmUser.setApUserId(apUser.getId());
            wmUser.setCreatedTime(new Date());
            wmUser.setName(apUser.getName());
            wmUser.setPassword(apUser.getPassword());
            wmUser.setSalt(apUser.getSalt());
            wmUser.setPhone(apUser.getPhone());
            wmUser.setStatus(9);
            wemediaFeign.save(wmUser);
        }
        //创建作者
        createAuthor(wmUser);

        apUser.setFlag((short) 1);
        apUserMapper.updateById(apUser);
        return null;
    }


    /**
     * 创建作者
     *
     * @param wmUser
     */
    private void createAuthor(WmUser wmUser) {
        Integer apUserId = wmUser.getApUserId();
        ApAuthor apAuthor = articleFeign.findByUserId(apUserId);
        if (apAuthor == null) {
            apAuthor = new ApAuthor();
            apAuthor.setName(wmUser.getName());
            apAuthor.setCreatedTime(new Date());
            apAuthor.setUserId(apUserId);
            apAuthor.setType(AdminConstans.AUTH_TYPE);
            articleFeign.save(apAuthor);
        }
    }


    /**
     * 检查状态
     *
     * @param status
     * @return
     */
    private boolean checkStatus(Short status) {
        if (status == null || (!status.equals(AdminConstans.FAIL_AUTH) && !status.equals(AdminConstans.PASS_AUTH))) {
            return true;
        }
        return false;
    }
}
