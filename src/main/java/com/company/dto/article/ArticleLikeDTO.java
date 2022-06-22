package com.company.dto.article;

import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.LikeStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
public class ArticleLikeDTO {

    private String articleId;
}
