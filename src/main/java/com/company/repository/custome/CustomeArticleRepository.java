package com.company.repository.custome;

import com.company.dto.ArticleFilterDTO;
import com.company.entity.ArticleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CustomeArticleRepository  {
    @Autowired
    private EntityManager entityManager;

    public List<ArticleEntity> filter(ArticleFilterDTO dto){
        Query query = entityManager.createQuery("select a from ArticleEntity as a");
        List<ArticleEntity> articletList = query.getResultList();
        return articletList;
    }
}
