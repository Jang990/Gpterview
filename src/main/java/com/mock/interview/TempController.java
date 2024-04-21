package com.mock.interview;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TempController {
    @GetMapping("/category")
    public String tempCategoryPage(Model model) {
        model.addAttribute("headerActiveTap", "category");
        return "/temp/category";
    }
    }
}
