package com.mock.interview.category.domain.model;

import com.mock.interview.category.domain.exception.MissingRequiredDepartmentCategoryException;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.PERSIST)
    @JoinColumn(name = "job_parent_id")
    private JobCategory department;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @Cascade(CascadeType.ALL)
    private List<JobPosition> relatedPosition;

    /**
     * 분야 - ex)
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
        if (relatedDepartmentCategory == null || relatedDepartmentCategory.isField()) {
            throw new MissingRequiredDepartmentCategoryException();
        }

        JobCategory category = new JobCategory();
        insertName(category, name);
        category.department = relatedDepartmentCategory;
        return category;
    }

    /** Name은 항상 대문자로 저장 */
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
