package com.kon.EShop.controller;

import com.kon.EShop.AuthorizedUser;
import com.kon.EShop.model.userPack.User;
import com.kon.EShop.repository.userPack.RegisterStep;
import com.kon.EShop.repository.userPack.UpdateStap;
import com.kon.EShop.service.UserService;
import com.kon.EShop.to.UserTo;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import static com.kon.EShop.util.UserUtil.createNewFromTo;

@Controller
@RequestMapping()
public class ProfileController {

    private final UserService service;

    public ProfileController(UserService service) {
        this.service = service;
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal AuthorizedUser authUser, ModelMap model) {
        model.addAttribute("userTo", authUser.getUserTo());
        model.addAttribute("register", false);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Validated(UpdateStap.class) UserTo userTo,
                                BindingResult result,
                                ModelMap model,
                                @AuthenticationPrincipal AuthorizedUser authUser) throws NotFoundException {

        model.addAttribute("register", false);

        if (result.hasErrors()) return "profile";

        service.update(userTo, authUser.getId());
        authUser.update(userTo);
        return "redirect:/";
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Validated(RegisterStep.class) UserTo userTo, BindingResult result, ModelMap model) {
        model.addAttribute("register", true);
        User byEmail = service.getByEmail(userTo.getEmail());
        if (byEmail != null) model.addAttribute("exist", true);
        if (result.hasErrors()) return "profile";
        service.create(createNewFromTo(userTo));
        return "redirect:/login?message=app.registered&username=" + userTo.getEmail();
    }

    @DeleteMapping(value = "/profile/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) throws NotFoundException {
        service.delete(userId);
    }
}