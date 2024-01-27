package com.mock.interview.review;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempController {
    @GetMapping("tech")
    public String loginPage(Model model) {
        model.addAttribute("headerActiveTap", "tech");
        return "/tech/tech";
    }

    @GetMapping("history")
    public String signUpPage(Model model) {
        model.addAttribute("headerActiveTap", "history");
        return "/history/history";
    }
}
