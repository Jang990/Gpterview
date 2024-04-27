package com.mock.interview.category.domain.model;

import com.mock.interview.category.domain.exception.MissingRequiredCategoryException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;

/** IT, 기획, 인사, 디자인 등등 넓은 범위의 카테고리 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobCategory {
    @Id
    @Column(name = "job_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<JobPosition> relatedPosition;

    /**
     * 분야 - ex)
     * @param name IT, 기획, 인사
     * @return
     */
    public static JobCategory createCategory(String name) {
        JobCategory category = new JobCategory();
        insertName(category, name);
        return category;
    }

    /** Name은 항상 대문자로 저장 */
    private static void insertName(JobCategory category, String name) {
        category.name = name.toUpperCase();
    }
}
