package com.kevin.microservicebase.userservice.controller;

import com.kevin.microservicebase.commonsupport.dto.RespDTO;
import com.kevin.microservicebase.commonsupport.util.BPwdEncoderUtil;
import com.kevin.microservicebase.userservice.feign.AuthServiceClient;
import com.kevin.microservicebase.userservice.entity.JWTBean;
import com.kevin.microservicebase.userservice.entity.User;
import com.kevin.microservicebase.userservice.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: UserController
 * @Description:
 * @Author: Kevin
 * @Date: 2018/6/22 09:57
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AuthServiceClient authServiceClient;

    @ApiOperation(value = "注册", notes = "username和password为必选项")
    @PostMapping("/registry")
    public User createUser(@RequestBody User user) {
        //参数判读省略,判读该用户在数据库是否已经存在省略
        String entryPassword = BPwdEncoderUtil.BCryptPassword(user.getPassword());
        user.setPassword(entryPassword);
        return userService.createUser(user);
    }

    @ApiOperation(value = "登录", notes = "username和password为必选项")
    @PostMapping("/login")
    public RespDTO login(@RequestParam String username, @RequestParam String password) {
        //参数判读省略
        return userService.login(username, password);
    }

    @ApiOperation(value = "根据用户名获取用户", notes = "根据用户名获取用户")
    @RequestMapping(value = "/userinfo/{username}", method = RequestMethod.GET)
    public RespDTO getUserInfo(@PathVariable("username") String username) {
        //参数判读省略
        User user = userService.getUserInfo(username);
        return RespDTO.onSuc(user);
    }


    @ApiOperation(value = "test feign", notes = "test feign")
    @PostMapping("/testFeign")
    public RespDTO testFeign(@RequestParam String username, @RequestParam String password) {
        //参数判读省略
        JWTBean jwtBean = authServiceClient.getToken("authorization", "type", username, password);
        return RespDTO.onSuc(jwtBean);
    }
}
