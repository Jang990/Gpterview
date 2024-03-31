package com.mock.interview.category.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 백엔드, 프론트엔드, 인프라, 건축 디자인 등등 세부 포지션 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobPosition {
    @Id
    @Column(name = "job_role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_category_id")
    private JobCategory category;

    public static JobPosition create(String name, JobCategory category) {
        JobPosition jobPosition = new JobPosition();
        insertName(jobPosition, name);
        jobPosition.category = category;
        return jobPosition;
    }

    /** Name은 항상 대문자로 저장 */
    private static void insertName(JobPosition category, String name) {
        category.name = name.toUpperCase();
    }
}
