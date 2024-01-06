package com.mock.interview.interview.domain.category;

import com.mock.interview.interview.domain.exception.MissingRequiredDepartmentCategoryException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.PERSIST)
    @JoinColumn(name = "job_parent_id")
    private JobCategory department;

    /**
     * 분야 - ex) IT, 기획, 인사 등등
     * @param name IT, 기획, 인사
     * @return
     */
    public static JobCategory createDepartmentCategory(String name) {
        JobCategory category = new JobCategory();
        insertName(category, name);
        return category;
    }

    /**
     * 필드 생성 - 백엔드, 프론트엔드, 안드로이드 등등
     * @param name 백엔드, 프론트엔드, 안드로이드
     * @param relatedDepartmentCategory IT, 기획, 인사
     * @return
     */
    public static JobCategory createFieldCategory(String name, JobCategory relatedDepartmentCategory) {
        System.out.println(relatedDepartmentCategory);
        if (relatedDepartmentCategory == null || relatedDepartmentCategory.isField()) {
            throw new MissingRequiredDepartmentCategoryException();
        }

        JobCategory category = new JobCategory();
        insertName(category, name);
        category.department = relatedDepartmentCategory;
        return category;
    }

    /**
     * Name은 항상 대문자로 저장
     */
    private static void insertName(JobCategory category, String name) {
        category.name = name.toUpperCase();
    }

    public boolean isDepartment() {
        return department == null;
    }

    public boolean isField() {
        return department != null;
    }
}
