package com.mock.interview.user.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mock.interview.category.presentation.dto.JobCategorySelectedIds;
import com.mock.interview.tech.presentation.dto.TechViewDto;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountForm {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private AccountDto account = new AccountDto();

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JobCategorySelectedIds categories = new JobCategorySelectedIds();

    private List<TechViewDto> tech;
    private List<String> experiences;

    public String getPassword() {return account.getPassword();}
    public String getUsername() {return account.getUsername();}
    public Long getCategoryId() {return categories.getCategoryId();}
    public Long getPositionId() {return categories.getPositionId();}

    public void setUsername(String username) {account.setUsername(username);}
    public void setPassword(String password) {account.setPassword(password);}
    public void setCategoryId(Long categoryId) {categories.setCategoryId(categoryId);}
    public void setPositionId(Long positionId) {this.categories.setPositionId(positionId);}

    public void setTech(String techString) {
        this.tech = TechViewDto.convert(techString);
    }

    @JsonIgnore
    public List<String> getTechName() {
        return getTech().stream().map(TechViewDto::getName).toList();
    }
}
