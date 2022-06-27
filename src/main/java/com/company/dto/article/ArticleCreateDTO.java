package com.company.dto.article;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
@Getter
@Setter
@NotNull
public class ArticleCreateDTO {

    private String uuid;

    @NotNull(message = "title null ku bratishka")
    @Size(min = 10, max = 255, message = "Size ni inobatga ol bratishka")
    private String title;
    @NotNull(message = "Content qaniiiiiii")
    private String content;
    private String description;
    private Integer regionId;
    private Integer categoryId;
    private List<Integer> typesList;
    private List<String> tagList;
    private String imageId;

}
