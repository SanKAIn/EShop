package com.kon.EShop.controller;

import com.kon.EShop.model.userPack.UserActiveName;
import com.kon.EShop.repository.impl.UserActiveImpl;
import com.kon.EShop.to.PageTo;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/analy")
public class AnalyticController {
    private final UserActiveImpl rep;

    public AnalyticController(UserActiveImpl rep) {
        this.rep = rep;
    }

    @GetMapping
    public PageTo<UserActiveName> getTen(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "LocalDate.now")
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                         @RequestParam(defaultValue = "LocalDate.now")
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                         @RequestParam(defaultValue = "10") Integer count,
                                         @RequestParam(defaultValue = "-1") Long id) {
        if (id >= 0) return rep.pageId(start, end, id);
        return rep.page(start, end, page, count);
    }

    @GetMapping("/days")
    public PageTo<UserActiveName> getDays(HttpServletRequest request) {
        String header = request.getHeader("user-agent");
        return null;
    }
}







