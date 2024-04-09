package com.mock.interview.user.presentation;

import com.mock.interview.user.presentation.dto.InfoPageDto;
import org.springframework.ui.Model;

public final class InfoPageInitializer {

    private InfoPageInitializer() {}

    public static void initInterviewInfoPage(Model model, String title, String description, String redirectLinkUrl) {
        model.addAttribute("info", new InfoPageDto(title, description, redirectLinkUrl));
    }
}
