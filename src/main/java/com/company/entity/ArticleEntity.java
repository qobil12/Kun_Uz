package com.company.entity;

import com.company.enums.ArticleStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "article")
public class ArticleEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "shared_count")
    private Integer sharedCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id")
    private ProfileEntity moderator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private ProfileEntity publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private RegionEntity region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;



    @ManyToMany
    @JoinTable(name = "article_type",
    joinColumns = @JoinColumn(name = "article_id"),
    inverseJoinColumns = @JoinColumn(name = "types_id"))
    private List<TypesEntity> typesList;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    @Column(nullable = false)
    private Boolean visible = Boolean.TRUE;

    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column( name = "publish_date")
    private LocalDateTime publishDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    private List<ArticleLikeEntity> articleLikeList;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "article")
    private List<CommentEntity> commentList;

    @JoinColumn(name = "photo_id")
    @OneToOne(fetch = FetchType.LAZY)
    private AttachEntity photo;



    public ArticleEntity() {
    }

    public ArticleEntity(String id) {
        this.id = id;
    }

    public ArticleEntity(String id, String title, String description, LocalDateTime publishDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.publishDate = publishDate;
    }
}
