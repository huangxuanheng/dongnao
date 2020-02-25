package edu.dongnao.rental.web.controller;

import java.util.Date;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.http.HttpStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.dongnao.rental.house.api.IHouseService;
import edu.dongnao.rental.house.domain.HouseSubscribeDTO;
import edu.dongnao.rental.house.domain.HouseSubscribeStatus;
import edu.dongnao.rental.lang.ApiResponse;
import edu.dongnao.rental.lang.ServiceMultiResult;
import edu.dongnao.rental.lang.ServiceResult;
import edu.dongnao.rental.uc.api.ISmsService;
import edu.dongnao.rental.uc.api.IUserService;
import edu.dongnao.rental.web.base.LoginUserUtil;
/**
 * 用户访问控制层
 */
@Controller
public class UserController {
    @Reference
    private IUserService userService;
    
    @Reference
    private ISmsService smsService;
    
    @Reference
    private IHouseService houseService;

    @GetMapping("/user/login")
    public String loginPage() {
        return "user/login";
    }

    @GetMapping("/user/center")
    public String centerPage() {
        return "user/center";
    }

    @PostMapping(value = "api/user/info")
    @ResponseBody
    public ApiResponse updateUserInfo(@RequestParam(value = "profile") String profile,
                                      @RequestParam(value = "value") String value) {
        if (value.isEmpty()) {
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }

        if ("email".equals(profile) && !LoginUserUtil.checkEmail(value)) {
            return ApiResponse.ofMessage(HttpStatus.SC_BAD_REQUEST, "不支持的邮箱格式");
        }

        ServiceResult<Boolean> result = userService.modifyUserProfile(profile, value, LoginUserUtil.getLoginUserId());
        if (result.isSuccess()) {
            return ApiResponse.ofSuccess("");
        } else {
            return ApiResponse.ofMessage(HttpStatus.SC_BAD_REQUEST, result.getMessage());
        }

    }
    
    @GetMapping(value = "sms/code")
    @ResponseBody
    public ApiResponse smsCode(@RequestParam("telephone") String telephone) {
        if (!LoginUserUtil.checkTelephone(telephone)) {
            return ApiResponse.ofMessage(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "请输入正确的手机号");
        }
        ServiceResult<String> result = smsService.sendSms(telephone);
        if (result.isSuccess()) {
            return ApiResponse.ofSuccess("");
        } else {
            return ApiResponse.ofMessage(org.springframework.http.HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }
    }
    
    /**======================================预约看房操作========================================*/
    
    @ResponseBody
    @PostMapping(value = "api/user/house/subscribe")
    public ApiResponse subscribeHouse(@RequestParam(value = "house_id") Long houseId) {
        ServiceResult<Boolean> result = houseService.addSubscribeOrder(houseId, LoginUserUtil.getLoginUserId());
        if (result.isSuccess()) {
            return ApiResponse.ofSuccess("");
        } else {
            return ApiResponse.ofMessage(HttpStatus.SC_BAD_REQUEST, result.getMessage());
        }
    }

    @ResponseBody
    @GetMapping(value = "api/user/house/subscribe/list")
    public ApiResponse subscribeList(
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "size", defaultValue = "3") int size,
            @RequestParam(value = "status") int status) {

        ServiceMultiResult<HouseSubscribeDTO> result = houseService.querySubscribeList(HouseSubscribeStatus.of(status), LoginUserUtil.getLoginUserId(), start, size);
        
        if (result.getResultSize() == 0) {
            return ApiResponse.ofSuccess(result.getResult());
        }
        ApiResponse response = ApiResponse.ofSuccess(result.getResult());
        response.setMore(result.getTotal() > (start + size));
        return response;
    }

    @ResponseBody
    @PostMapping(value = "api/user/house/subscribe/date")
    public ApiResponse subscribeDate(
            @RequestParam(value = "houseId") Long houseId,
            @RequestParam(value = "orderTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderTime,
            @RequestParam(value = "desc", required = false) String desc,
            @RequestParam(value = "telephone") String telephone
            ) {
        if (orderTime == null) {
            return ApiResponse.ofMessage(HttpStatus.SC_BAD_REQUEST, "请选择预约时间");
        }

        if (!LoginUserUtil.checkTelephone(telephone)) {
            return ApiResponse.ofMessage(HttpStatus.SC_BAD_REQUEST, "手机格式不正确");
        }

        ServiceResult<?> serviceResult = houseService.subscribe(houseId, LoginUserUtil.getLoginUserId(), orderTime, telephone, desc);
        if (serviceResult.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.SC_BAD_REQUEST, serviceResult.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping(value = "api/user/house/subscribe")
    public ApiResponse cancelSubscribe(@RequestParam(value = "houseId") Long houseId) {
        ServiceResult<?> serviceResult = houseService.cancelSubscribe(houseId, LoginUserUtil.getLoginUserId());
        if (serviceResult.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.SC_BAD_REQUEST, serviceResult.getMessage());
        }
    }
}
