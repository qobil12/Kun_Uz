package com.company.service;

import com.company.dto.ArticleFilterDTO;
import com.company.dto.article.AttachUpdateDTO;
import com.company.enums.Language;
import com.company.exps.ItemNotFoundEseption;
import com.company.mapper.ArticleShortInfo;
import com.company.repository.ArticleRepository;
import com.company.repository.custome.CustomeArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.company.dto.CategoryDTO;
import com.company.dto.ProfileDTO;
import com.company.dto.article.ArticleCreateDTO;
import com.company.dto.article.ArticleDTO;
import com.company.entity.*;
import com.company.enums.ArticleStatus;
import com.company.exps.BadRequestException;
import com.company.exps.ItemNotFoundException;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private RegionService regionService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleTypeService articleTypeService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private CustomeArticleRepository customeArticleRepository;




    public ArticleCreateDTO create(ArticleCreateDTO dto, Integer profileId) {
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setDescription(dto.getDescription());

        RegionEntity region = regionService.get(dto.getRegionId());
        entity.setRegion(region);

        CategoryEntity category = categoryService.get(dto.getCategoryId());
        entity.setCategory(category);

        ProfileEntity moderator = new ProfileEntity();
        moderator.setId(profileId);
        entity.setModerator(moderator);
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        entity.setPhoto(attachService.get(dto.getImageId()));

        articleRepository.save(entity);


        articleTypeService.create(entity, dto.getTypesList()); // type

        articleTagService.create(entity, dto.getTagList());  // tag

        return dto;
    }

    public List<ArticleDTO> getArticles() {
        Iterable<ArticleEntity> all = articleRepository.findAllByVisible(true);

        List<ArticleDTO> articleDTOS = new LinkedList<>();
        all.forEach(categoryEntity -> articleDTOS.add(toDTO(categoryEntity)));
        return articleDTOS;
    }

    public ArticleDTO getArticleById(String id, Language lang) {

        Optional<ArticleEntity> optional = articleRepository.findByIdAndVisibleTrue(id);

        if (optional.isEmpty()) {
            throw new BadRequestException("Article Not found");
        }

        ArticleEntity entity = optional.get();

        ArticleDTO dto = new ArticleDTO();

        dto.setUuid(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setDescription(entity.getDescription());
        dto.setSharedCount(entity.getSharedCount());
        dto.setRegion(regionService.get(entity.getRegion(), lang));
        dto.setCategory(categoryService.get(entity.getCategory(), lang));
        dto.setPublishDate(entity.getPublishDate());
        dto.setViewCount(entity.getViewCount());


        return dto;
    }


    public ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setUuid(entity.getId());
        dto.setContent(entity.getContent());
        dto.setDescription(entity.getDescription());
        dto.setVisible(entity.getVisible());
        dto.setViewCount(entity.getViewCount());
        dto.setTitle(entity.getTitle());
        dto.setStatus(entity.getStatus());
        dto.setPublishDate(entity.getPublishDate());
        dto.setCreatedDate(entity.getCreatedDate());


        dto.setRegion(regionService.toDTORegion(entity.getRegion()));
        dto.setCategory(toDTOCategory(entity.getCategory()));
        dto.setModeratorId(toDTOModerator(entity.getModerator()));

        return dto;
    }

    public void delete(String id) {
        Optional<ArticleEntity> article = articleRepository.findById(id);
        if (article.isEmpty()) {
            throw new ItemNotFoundException("Mazgi bu id li article yo'q");
        }
        ArticleEntity entity = article.get();
        entity.setVisible(false);
        articleRepository.save(entity);
    }


    public void updateArticleImage(AttachUpdateDTO dto, String articleId){
        AttachEntity newAttachEntity = attachService.get(dto.getNewId());
        AttachEntity oldAttachEntity = attachService.get(dto.getOldId());

        Optional<ArticleEntity> optional = articleRepository.existsByPhotoId(articleId, oldAttachEntity.getId());
        if(optional.isEmpty()){
            throw new ItemNotFoundEseption("Bu rasm bu articlega tegishli emas");
        }
        ArticleEntity article = optional.get();

        article.setPhoto(newAttachEntity);
        articleRepository.save(article);
        attachService.delete(oldAttachEntity.getId());
    }

    public String update(String id, ArticleCreateDTO dto) {
        isValid(dto);

        Optional<ArticleEntity> optional = articleRepository.findById(id);

        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Bunday id li article yo'q");
        }


        ArticleEntity entity = optional.get();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setDescription(dto.getDescription());

        RegionEntity region = regionService.get(dto.getRegionId());
        entity.setRegion(region);

        CategoryEntity category = categoryService.get(dto.getCategoryId());
        entity.setCategory(category);

        articleRepository.save(entity);

        articleTypeService.create(entity, dto.getTypesList()); // type

        articleTagService.create(entity, dto.getTagList());  // tag

        return "Successfully updated";

    }

    public ArticleEntity get(String id) {
        return articleRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("article not found");
        });
    }


    public ProfileDTO toDTOPublisher(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurName(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        return dto;
    }

    public ProfileDTO toDTOModerator(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurName(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        return dto;
    }

    public CategoryDTO toDTOCategory(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setKey(entity.getKey());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setNameUz(entity.getNameUz());
        return dto;
    }

    public PageImpl pagination(int page, int size) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ArticleEntity> all = articleRepository.findAll(pageable);

        List<ArticleEntity> list = all.getContent();

        List<ArticleDTO> dtoList = new LinkedList<>();

        list.forEach(typesEntity -> {
            ArticleDTO dto = new ArticleDTO();
            dto.setModeratorId(toDTOModerator(typesEntity.getModerator()));
            dto.setCategory(toDTOCategory(typesEntity.getCategory()));
            dto.setRegion(regionService.toDTORegion(typesEntity.getRegion()));
            dto.setStatus(typesEntity.getStatus());
            dto.setTitle(typesEntity.getTitle());
            dto.setVisible(typesEntity.getVisible());
            dto.setUuid(typesEntity.getId());
            dto.setPublishDate(typesEntity.getPublishDate());
            dto.setSharedCount(typesEntity.getSharedCount());
            dto.setViewCount(typesEntity.getViewCount());
            dto.setContent(typesEntity.getContent());
            dto.setCreatedDate(typesEntity.getCreatedDate());
            dto.setDescription(typesEntity.getDescription());

            dtoList.add(dto);
        });

        return new PageImpl(dtoList, pageable, all.getTotalElements());
    }

    public List<ArticleDTO> findTop5ArticlesByTypeKey(String key) {


        List<ArticleEntity> articleList = articleRepository.findTop5ByArticleNative(key);


        List<ArticleDTO> dtoList = new LinkedList<>();

        articleList.forEach(articleEntity -> {

            ArticleDTO dto = new ArticleDTO();

            dto.setUuid(articleEntity.getId());
            dto.setTitle(articleEntity.getTitle());
            dto.setDescription(articleEntity.getDescription());
            dto.setPublishDate(articleEntity.getPublishDate());
            dtoList.add(dto);
        });

        return dtoList;
    }

    public List<ArticleDTO> findTop5ByArticleByCategory(String key) {

        List<ArticleShortInfo> articleList = articleRepository.findTop5ByArticleByCategory2(key);

        List<ArticleDTO> dtoList = new LinkedList<>();

        articleList.forEach(articleEntity -> {
            dtoList.add(shortDTOInfoMapper(articleEntity));
        });

        return dtoList;
    }

    public List<ArticleDTO> findTop5ByArticleByCategory1(String key) {
        CategoryEntity category = categoryService.get(key);

        List<ArticleEntity> articleList = articleRepository.findTop5ByCategoryAndStatusAndVisibleTrueOrderByCreatedDateDesc(category, ArticleStatus.PUBLISHED);

        List<ArticleDTO> dtoList = new LinkedList<>();
        articleList.forEach(articleEntity -> {
            dtoList.add(shortDTOInfo(articleEntity));
        });

        return dtoList;

    }

    public List<ArticleDTO> get5ArticlesByTagName(String tagName) {

        String s = "#";
        String sub = s.concat(tagName);

        List<ArticleShortInfo> articleList = articleRepository.get5ArticlesByTagName(sub);

        List<ArticleDTO> dtoList = new LinkedList<>();
        articleList.forEach(articleEntity -> {
            dtoList.add(shortDTOInfoMapper(articleEntity));
        });

        return dtoList;

    }


    public ArticleDTO shortDTOInfo(ArticleEntity article) {
        ArticleDTO dto = new ArticleDTO();

        dto.setUuid(article.getId());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setPublishDate(article.getPublishDate());
        return dto;
    }

    public ArticleDTO shortDTOInfoMapper(ArticleShortInfo article) {
        ArticleDTO dto = new ArticleDTO();

        dto.setUuid(article.getId());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setPublishDate(article.getPublishDate());
        return dto;
    }

    public void updateByStatus(String articleId, Integer publisherId) {

        Optional<ArticleEntity> optional = articleRepository.findById(articleId);

        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Article not found ");
        }

        ArticleEntity articleEntity = optional.get();
        if (articleEntity.getStatus().equals(ArticleStatus.NOT_PUBLISHED)) {

            articleRepository.updateArticleToPublish(ArticleStatus.PUBLISHED
                    , LocalDateTime.now()
                    , new ProfileEntity(publisherId)
                    , articleId);

        } else if (articleEntity.getStatus().equals(ArticleStatus.PUBLISHED)) {

            articleRepository.updateArticleToNotPublish(ArticleStatus.NOT_PUBLISHED, articleId);
        }
    }


    public List<ArticleDTO> filter(ArticleFilterDTO dto){
        customeArticleRepository.filter(dto);

        return null;
    }

    public List<ArticleDTO> getCategoryByPage(String key) {
        Pageable page = PageRequest.of(0, 5);

        Page<ArticleEntity> articlePage = articleRepository.
                findLast5ByCategoryKey(key,
                        ArticleStatus.PUBLISHED,
                        page);
        List<ArticleDTO> dtoList = new LinkedList<>();

        articlePage.getContent().forEach(articleEntity -> {
            dtoList.add(shortDTOInfo(articleEntity));
        });

        return dtoList;

    }

    public List<ArticleDTO> getLast5ArticleByType(String typeKey, String id) {
        List<ArticleShortInfo> articleList = articleRepository.findLast5ByType(
                typeKey, id);

        List<ArticleDTO> dtoList = new LinkedList<>();
        articleList.forEach(article -> {
            dtoList.add(shortDTOInfoMapper(article));
        });
        return dtoList;
    }

    public PageImpl<ArticleDTO> getLast5ArticleByRegionKey(String regionKey, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleShortInfo> articlePage = articleRepository.findLast5ByRegionKey(
                regionKey, pageable);

        List<ArticleDTO> dtoList = new LinkedList<>();
        articlePage.getContent().forEach(article -> {
            dtoList.add(shortDTOInfoMapper(article));
        });

        return new PageImpl(dtoList, pageable, articlePage.getTotalElements());
    }

    public List<ArticleDTO> getMost4ReadArticleByViewCount() {

        List<ArticleShortInfo> entityList = articleRepository.getMostRead4ArticleByViewCount();

        List<ArticleDTO> dtoList = new LinkedList<>();
        entityList.forEach(article -> {
            dtoList.add(shortDTOInfoMapper(article));
        });

        return dtoList;
    }

    public List<ArticleDTO> getMost4ReadArticleByTypesAndRegion(String typeKey, String regionKey) {
        List<ArticleShortInfo> articleList = articleRepository.getLast5ByTypesAndRegionKey(typeKey, regionKey);
        List<ArticleDTO> dtoList = new LinkedList<>();
        articleList.forEach(article -> {
            dtoList.add(shortDTOInfoMapper(article));
        });
        return dtoList;
    }

    public List<ArticleDTO> getLat8ArticleNotIn(List<String> articleIdList) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<ArticleEntity> articlePage = articleRepository.findLast8NotIn(articleIdList, pageable);

        List<ArticleDTO> dtoList = new LinkedList<>();
        articlePage.getContent().forEach(article -> {
            dtoList.add(shortDTOInfo(article));
        });
        return dtoList;
    }

    public void increaseViewCountById(String id) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);

        if (optional.isEmpty()) {
            throw new BadRequestException("Article is null");
        }

        ArticleEntity entity = optional.get();
        entity.setViewCount(entity.getViewCount() + 1);

        articleRepository.save(entity);
    }

    private void isValid(ArticleCreateDTO dto) {
        if (dto.getContent() == null) {
            throw new BadRequestException("Content is  Wrong.");
        }
        if (dto.getDescription() == null) {
            throw new BadRequestException("Description is  Wrong.");
        }
        if (dto.getTitle() == null) {
            throw new BadRequestException("Title is  Wrong");
        }

    }
}
