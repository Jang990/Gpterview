package com.mock.interview.user.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnauthorizedPageInfo {
    private String title;
    private String element;
    private String redirectLinkUrl;
}
