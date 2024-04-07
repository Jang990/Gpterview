package com.mock.interview;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TempController {
    @GetMapping("history")
    public String signUpPage(Model model) {
        model.addAttribute("headerActiveTap", "history");
        return "/temp/history";
    }
}
