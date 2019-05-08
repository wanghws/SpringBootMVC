package com.demo.api.controller;

import com.demo.api.commons.controller.BaseController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by wanghw on 2019-04-04.
 */
@ApiIgnore
@RestController
public class StatusController extends BaseController {
    @Value("${demo.api.release.build}")
    private String version;
    @Value("${demo.api.release.time}")
    private String time;


    @RequestMapping("status")
    public String status(){
        return "Build: "+time+"."+version;
    }
}
