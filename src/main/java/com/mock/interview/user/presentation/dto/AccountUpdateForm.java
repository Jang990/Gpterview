package com.mock.interview.user.presentation.dto;

import com.mock.interview.category.presentation.dto.JobCategorySelectedIds;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateForm {
    private long userId;
    private String username;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JobCategorySelectedIds categories = new JobCategorySelectedIds();

    public Long getCategoryId() {return categories.getCategoryId();}
    public Long getPositionId() {return categories.getPositionId();}

    public void setCategoryId(Long categoryId) {categories.setCategoryId(categoryId);}
    public void setPositionId(Long positionId) {this.categories.setPositionId(positionId);}
}
