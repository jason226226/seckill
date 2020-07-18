package com.example.seckill.controller;

import com.example.seckill.dto.SeckillResult;
import com.example.seckill.pojo.Seckill;
import com.example.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private SeckillService seckillService;

    @RequestMapping("/")
    public String index(){
        return "redirect:/seckill/list";
    }

    @RequestMapping(value = "/seckill/list",method = RequestMethod.GET)
    public String SeckillList(Model model){
        List<Seckill> seckills = seckillService.findAll();
        model.addAttribute("list",seckills);
        return "view/list";
    }

    @RequestMapping("/seckill/{seckillId}/detail")
    public String detail(@PathVariable Long seckillId, Model model){
        Seckill seckill = seckillService.findById(seckillId);
        model.addAttribute("seckill",seckill);
        return "view/detail";
    }

    @RequestMapping(value = "/seckill/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult<>(true,now.getTime());
    }
}
