package com.company.dto;

import com.company.enums.ArticleStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleFilterDTO {
    private String id;
    private String title;
    private String description;
    private Integer regionId;
    private Integer categoryId;

    private String publishedDateFrom;
    private String publishedDateTo;

    private Integer moderatorId;
    private Integer publisherId;

    private ArticleStatus status;
}
