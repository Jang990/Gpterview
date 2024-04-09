package com.mock.interview.user.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 성공 or 실패 페이지 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoPageDto {
    private String title;
    private String description;
    private String redirectLinkUrl;
}
