package com.company.dto.article;

import com.company.dto.CategoryDTO;
import com.company.dto.ProfileDTO;
import com.company.dto.RegionDTO;
import com.company.entity.CategoryEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.RegionEntity;
import com.company.enums.ArticleStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {

    private String uuid ;
    private String title;
    private String content;
    private RegionDTO region;
    private CategoryDTO category;
    private ProfileDTO moderatorId;
    private ProfileDTO publisherId;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime publishDate;
    private Integer viewCount;
    private Integer sharedCount;
    private ArticleStatus status;
    private Boolean visible = Boolean.TRUE;


//    private String uuid;
//    private String title;
//    private String content;
//    private RegionEntity regionEntity;
//    private CategoryEntity categoryEntity;
//    private ProfileEntity moderatorId;
//    private ProfileEntity publisherId;
//    private String description;
//    private LocalDateTime createdDate;
//    private LocalDateTime publishDate;
//    private Integer viewCount;
//    private Integer sharedCount;
//    private ArticleStatus status;
//    private Boolean visible = Boolean.TRUE;
}
