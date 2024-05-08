package com.mock.interview;

import com.mock.interview.global.security.dto.LoginUserDetail;
import com.mock.interview.user.domain.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class TempController {
    private final AuthenticationManager authenticationManager;

    @GetMapping("/category")
    public String tempCategoryPage(Model model) {
        model.addAttribute("headerActiveTap", "category");
        return "temp/category";
    }

//    @GetMapping("/test/login")
//    @ResponseBody
//    public String testLogin(@RequestParam("id") String id) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(id);
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
//        SecurityContextHolder.setContext(context);
//        return "ok";
//    }

//    @GetMapping("/testLogin")
//    public String test() {
//        // 부트 3.x부터 안됨
//        UserDetails userDetails = new LoginUserDetail(27L, "test계정","abc123@test.com", UserRole.USER);
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
//        return "redirect:/";
//    }
}
