package com.kon.EShop.controller;

import com.kon.EShop.AuthorizedUser;
import com.kon.EShop.UserService;
import com.kon.EShop.to.UserTo;
import com.kon.EShop.util.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

import static com.kon.EShop.util.UserUtil.createNewFromTo;
import static com.kon.EShop.util.ValidationUtil.assureIdConsistent;

@Controller
@RequestMapping("/profile")
@Slf4j
public class ProfileController {

    private final UserService service;

    public ProfileController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String profile(ModelMap model, @AuthenticationPrincipal AuthorizedUser authUser) {
        model.addAttribute("register", false);
        model.addAttribute("userTo", authUser.getUserTo());
        model.addAttribute("err", false);
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid UserTo userTo,
                                BindingResult result,
                                SessionStatus status,
                                @AuthenticationPrincipal AuthorizedUser authUser,
                                ModelMap model) throws NotFoundException {
        if (result.hasErrors()) {
            model.addAttribute("register", false);
            model.addAttribute("err", true);
            return "profile";
        }
        if (userTo.getPassword().equals("old9]xt"))
            userTo.setPassword(authUser.getUserTo().getPassword());

        service.update(userTo, authUser.getId());
        authUser.update(userTo);
        status.setComplete();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        model.addAttribute("err", false);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("register", true);
            model.addAttribute("err", true);
            return "profile";
        }
        service.create(createNewFromTo(userTo));
        status.setComplete();
        return "redirect:/login?message=app.registered&username=" + userTo.getEmail();
    }

    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) throws NotFoundException {
        service.delete(userId);
    }
}