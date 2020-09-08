package com.sbm.modules.consent.controller;

import com.sbm.modules.consent.dto.AppDto;
import com.sbm.modules.consent.service.app.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/admin/app")
public class AppController {

    @Autowired
    private AppService appService;

    @RequestMapping(value = "/all", method = GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<List<AppDto>> getApps() {
        List<AppDto> apps = appService.getAllApps();
        return new ResponseEntity<List<AppDto>>(apps, HttpStatus.OK);
    }
}
