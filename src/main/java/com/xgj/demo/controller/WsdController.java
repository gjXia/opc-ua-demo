package com.xgj.demo.controller;

import com.xgj.demo.base.GenericController;
import com.xgj.demo.base.Respond;
import com.xgj.demo.utils.WsdUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gjXia
 * @date 2021/2/23 10:42
 */
@RestController
@RequestMapping(value = "info")
public class WsdController extends GenericController {


    @GetMapping(value = "/getAll")
    public Respond getWsdInfo() {
        return buildSuccess(WsdUtil.getInstance().getWsdInfo());
    }

    @GetMapping(value = "/get")
    public Respond get(String key) {
        return buildSuccess(WsdUtil.getInstance().get(key));
    }

    @PostMapping(value = "/add")
    public Respond add(String key, String value) {
        WsdUtil.getInstance().add(key, value);
        return buildSuccess();
    }
}
