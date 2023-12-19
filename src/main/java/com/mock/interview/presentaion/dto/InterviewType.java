package com.mock.interview.presentaion.dto;

import lombok.Getter;

@Getter
public enum InterviewType {
    TECHNICAL("기술 면접"), // 기술(Skills 관련 + 기반 지식)
    EXPERIENCE("경험 면접"),
    PERSONALITY("인성 면접"),
    TECHNICAL_EXPERIENCE("기술+경험 면접"),
    COMPOSITE("복합 면접(기술,경험,인성)");
    private final String name;

    InterviewType(String name) {
        this.name = name;
    }
}