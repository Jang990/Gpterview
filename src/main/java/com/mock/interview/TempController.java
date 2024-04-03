package com.mock.interview;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TempController {
    @GetMapping("tech")
    public String loginPage(Model model) {
        model.addAttribute("headerActiveTap", "tech");
        return "/temp/tech";
    }

    @GetMapping("history")
    public String signUpPage(Model model) {
        model.addAttribute("headerActiveTap", "history");
        return "/temp/history";
    }

    @GetMapping("/auth/{username}")
    public String myPage(@PathVariable(value = "username") String username) {
        // TODO: 세션정보와 일치하는지 확인할 것
        return "/temp/my-page";
    }
}
