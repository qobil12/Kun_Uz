package com.company.repository;

import com.company.entity.ArticleEntity;
import com.company.entity.CategoryEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ArticleStatus;
import com.company.mapper.ArticleShortInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends PagingAndSortingRepository<ArticleEntity, String> {

    Iterable<ArticleEntity> findAllByVisible(boolean visible);

    Optional<ArticleEntity> findById(String id);
    Optional<ArticleEntity> findByIdAndVisibleTrue(String id);


//        Optional<ArticleEntity> findByTitleAndContent(String title, String content);
//        Optional<ArticleEntity> findById(String value);
//        List<ArticleEntity> findTopByVisible(Boolean b);

    @Query(value = "SELECT art.* " +
            " FROM article_type as a " +
            " inner join article as art on art.id = a.article_id " +
            " inner join types as t on t.id = a.types_id " +
            " Where  t.key =:key  " +
            " order by art.publish_date " +
            " limit 5",
            nativeQuery = true)
    List<ArticleEntity> findTop5ByArticleNative(@Param("key") String key);

    @Modifying
    @Transactional
    @Query("update ArticleEntity set status=:status , publishDate=:publish , publisher=:publisher where id=:id")
    void updateArticleToPublish(@Param("status") ArticleStatus status
            , @Param("publish") LocalDateTime time
            , @Param("publisher") ProfileEntity entity
            , @Param("id") String uuid);

    @Modifying
    @Transactional
    @Query("update ArticleEntity set status=:status where id=:id")
    void updateArticleToNotPublish(@Param("status") ArticleStatus status
            , @Param("id") String uuid);


    //BY CATEGORY
    @Query(value = "select art.* " +
            " from article as art " +
            " inner join category as cat on cat.id = art.category_id " +
            " where cat.key=:key and art.status='PUBLISHED' and art.visible= true " +
            " order by art.publish_date limit 5",
            nativeQuery = true)
    List<ArticleEntity> findTop5ByArticleByCategory(@Param("key") String key);

    List<ArticleEntity> findTop5ByCategoryAndStatusAndVisibleTrueOrderByCreatedDateDesc(CategoryEntity category, ArticleStatus status);

    @Query("select new ArticleEntity(art.id,art.title , art.description , art.publishDate)  " +
            "from ArticleEntity as art" +
            " where art.category.key=:key and art.status=:status and " +
            "art.visible = true" +
            " order by art.publishDate")
    Page<ArticleEntity> findLast5ByCategoryKey(@Param("key") String categoryKey,
                                               @Param("status") ArticleStatus status,
                                               Pageable pageable);


    @Query(value = "select  art.id as id ,art.title as title, art.description as description,art.publish_date as publishDate" +
            "   from article as art " +
            "   inner join category as cat on art.category_id = cat.id " +
            " where cat.key=:key and art.status='PUBLISHED' and art.visible=true  " +
            " order by art.publish_date limit 5 ",
            nativeQuery = true)
    List<ArticleShortInfo> findTop5ByArticleByCategory2(@Param("key") String key);


    @Query(value = "SELECT  art.id as id ,art.title as title, art.description as description,art.publish_date as publishDate " +
            " FROM article as art " +
            " inner join article_type as a on a.article_id = art.id " +
            " inner join types as t on t.id = a.types_id " +
            " where  t.key =:key and art.visible = true and art.status = 'PUBLISHED' and art.id not in (:id) " +
            " order by art.publish_date " +
            " limit 5 ",
            nativeQuery = true)
    List<ArticleShortInfo> findLast5ByType(@Param("key") String key, @Param("id") String id);

    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishDate) " +
            " from ArticleEntity as art " +
            " Where art.visible = true and art.status = 'PUBLISHED' and art.id not in (:idList) " +
            " order by art.publishDate ")
    Page<ArticleEntity> findLast8NotIn(@Param("idList") List<String> idList, Pageable pageable);

    @Query(value = "select  art.id as id ,art.title as title, art.description as description,art.publish_date as publishDate" +
            "   from article as art " +
            " where art.status='PUBLISHED' and art.visible=true  " +
            " order by art.view_count desc limit 4 ",
            nativeQuery = true)
    List<ArticleShortInfo> getMostRead4ArticleByViewCount();


    @Query(value = "select art.id,art.title,art.description,art.publish_date from article_type as a " +
            "inner join types as t on a.types_id = t.id " +
            "inner join article as art on art.id = a.article_id " +
            "inner join region as r on r.id = art.region_id " +
            "where r.key=:rKey and t.key =:tKey " +
            "and art.status='PUBLISHED' and art.visible=true " +
            "order by art.publish_date limit 5",nativeQuery = true)
    List<ArticleShortInfo> getLast5ByTypesAndRegionKey(@Param("tKey") String typesKey,
                                                       @Param("rKey") String regionKey);



    @Query(value = "select art.id as id, art.title as title, art.description as description, art.publish_date as publishDate " +
            "from article as art " +
            " inner join region as reg on art.region_id = reg.id " +
            "where reg.key = 'tashkent_city' " +
            "  and art.status = 'PUBLISHED' " +
            "  and art.visible = true " +
            "order by art.publish_date ",
            nativeQuery = true)
    Page<ArticleShortInfo> findLast5ByRegionKey(@Param("key") String regionKey,
                                                Pageable pageable);



    @Query(value = "SELECT art.id as id, art.title as title, art.description as description, art.publish_date as publishDate " +
            " FROM article_tag as a " +
            " inner join article as art on art.id = a.article_id " +
            " inner join tag as t on t.id = a.tag_id " +
            " Where  t.name =:name  " +
            " order by art.publish_date " +
            " limit 5",
            nativeQuery = true)
    List<ArticleShortInfo> get5ArticlesByTagName(@Param("name") String key);

@Query("update ArticleEntity set viewCount = viewCount+1 where id=:id")
    ArticleEntity increaseViewCountId(@Param("id") String id);



}