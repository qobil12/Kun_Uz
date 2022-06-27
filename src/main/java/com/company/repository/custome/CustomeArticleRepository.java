package com.company.repository.custome;

import com.company.dto.ArticleFilterDTO;
import com.company.entity.ArticleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class CustomeArticleRepository  {
    @Autowired
    private EntityManager entityManager;

//    public List<ArticleEntity> filter(ArticleFilterDTO dto){
//        Query query = entityManager.createQuery("select a from ArticleEntity as a");
//        List<ArticleEntity> articletList = query.getResultList();
//        return articletList;
//    }
public List<ArticleEntity> filter(ArticleFilterDTO dto) {

    StringBuilder builder = new StringBuilder();
    builder.append(" SELECT a FROM ArticleEntity a ");
    builder.append(" where visible = true ");

    if (dto.getId() != null) {
        builder.append(" and a.id = '" + dto.getId() + "' ");
    }

    if (dto.getTitle() != null) {
        builder.append(" and a.title = '" + dto.getTitle() + "' ");
    }
    // Select a from ArticleEntity a where title = 'asdasd'; delete from sms-- fdsdfsdfs'
    if (dto.getStatus() != null) {
        builder.append(" and a.status = '" + dto.getStatus() + "' ");
    }

    if (dto.getDescription() != null) {
        builder.append(" and a.description like '%" + dto.getStatus() + "%' ");
    }

    if (dto.getCategoryId() != null) {
        builder.append(" and a.category.id=" + dto.getCategoryId() + " ");
    }

    if (dto.getRegionId() != null) {
        builder.append(" and a.region.id=" + dto.getRegionId() + " ");
    }

    if (dto.getModeratorId() != null) {
        builder.append(" and a.moderator.id=" + dto.getModeratorId() + " ");
    }

    if (dto.getPublishedDateFrom() != null && dto.getPublishedDateTo() == null) {
        // builder.append(" and a.publishDate = '" + dto.getPublishedDateFrom() + "' ");
        LocalDate localDate = LocalDate.parse(dto.getPublishedDateFrom());
        LocalDateTime fromTime = LocalDateTime.of(localDate, LocalTime.MIN); // yyyy-MM-dd 00:00:00
        LocalDateTime toTime = LocalDateTime.of(localDate, LocalTime.MAX); // yyyy-MM-dd 23:59:59
        builder.append(" and a.publishDate between '" + fromTime + "' and '" + toTime + "' ");
    } else if (dto.getPublishedDateFrom() != null && dto.getPublishedDateTo() != null) {
        builder.append(" and a.publishDate between '" + dto.getPublishedDateFrom() + "' and '" + dto.getPublishedDateTo() + "' ");
    }
    if (dto.getPublisherId() != null) {
        builder.append(" and a.publisher.id =" + dto.getPublisherId() + " ");
    }

//        entityManager.createQuery("select a from ArticleEntity a where a.moderator.id ")

    Query query = entityManager.createQuery(builder.toString());
    List<ArticleEntity> articleList = query.getResultList();

    return articleList;
}

}
