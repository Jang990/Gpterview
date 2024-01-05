package com.mock.interview.interview.domain;

import com.mock.interview.interview.domain.category.JobCategory;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class InterviewJobCategoryLink {
    @Id
    @Column(name = "job_link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id")
    private JobCategory jobCategory;
}
