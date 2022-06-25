package com.company.controller;


import com.company.dto.ArticleFilterDTO;
import com.company.dto.article.ArticleCreateDTO;
import com.company.dto.article.ArticleDTO;
import com.company.dto.article.ArticleRequestDTO;
import com.company.dto.article.AttachUpdateDTO;
import com.company.enums.Language;
import com.company.enums.ProfileRole;
import com.company.service.ArticleService;
import com.company.util.HttpHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/article")
@RestController
public class ArticleController {
    @Autowired
    ArticleService articleService;

    //1
    @PostMapping("/adm/create")
    public ResponseEntity<?> create(@RequestBody ArticleCreateDTO articleDTO,
                                    HttpServletRequest request) {

        Integer profileId = HttpHeaderUtil.getId(request, ProfileRole.MODERATOR);

        ArticleCreateDTO dto = articleService.create(articleDTO, profileId);
        return ResponseEntity.ok().body(dto);
    }

    //2
    @PostMapping("/update/{description}")
    public ResponseEntity<?> update(@PathVariable("description") String description,
                                    @RequestBody ArticleCreateDTO articleDTO) {
        articleService.update(description, articleDTO);
        return ResponseEntity.ok().body("Successfully updated");
    }

    //3
    @DeleteMapping("/delete/{description}")
    public ResponseEntity<?> delete(@PathVariable("description") String description) {
        articleService.delete(description);
        ResponseEntity<Object> build = ResponseEntity.ok().body("Successfully deleted");
        return build;
    }

    //4
    @PutMapping("/adm/status/{id}")
    public ResponseEntity<Void> updateByStatus(@PathVariable("id") String id,
                                               HttpServletRequest request) {
        Integer profileId = HttpHeaderUtil.getId(request, ProfileRole.PUBLISHER);
        articleService.updateByStatus(id, profileId);
        return ResponseEntity.ok().build();
    }


    //5
    @GetMapping("/getListByTypeKey/{key}")
    public ResponseEntity<?> getListByTypeKey(@PathVariable("key") String key) {

        List<ArticleDTO> list = articleService.findTop5ArticlesByTypeKey(key);
        return ResponseEntity.ok().body(list);
    }

    //6


    //7
    @PostMapping("/last8")
    public ResponseEntity<List<ArticleDTO>> getLast8NotIn(@RequestBody ArticleRequestDTO dto) {
        List<ArticleDTO> response = articleService.getLat8ArticleNotIn(dto.getIdList());
        return ResponseEntity.ok().body(response);
    }

    //8
    @GetMapping("/getArticle/{id}")
    public ResponseEntity<?> getArticleList(@PathVariable("id") String id,
                                            @RequestHeader(value = "Accept-Language", defaultValue = "uz")
                                            Language lang) {
        ArticleDTO list = articleService.getArticleById(id,lang);
        return ResponseEntity.ok().body(list);
    }

    //9
    @GetMapping("/type/{key}")
    public ResponseEntity<?> getListByType(@PathVariable("key") String key,@PathVariable("id") String id) {

        List<ArticleDTO> list = articleService.getLast5ArticleByType(key,id);
        return ResponseEntity.ok().body(list);
    }


    //10
    @GetMapping("/mostRead")
    public ResponseEntity<?> getMostReadArticleByViewCount() {

        List<ArticleDTO> list = articleService.getMost4ReadArticleByViewCount();
        return ResponseEntity.ok().body(list);
    }

    //11
    @GetMapping("/byTagName/{tagName}")
    public ResponseEntity<?> get5ArticlesByTag(@PathVariable("tagName") String tagName) {

        List<ArticleDTO> list = articleService.get5ArticlesByTagName(tagName);
        return ResponseEntity.ok().body(list);
    }

    //12
    @GetMapping("/regionAndType/{typeKey}/{regionKey}")
    public ResponseEntity<?> get5ArticlesByRegionAndType(@PathVariable("typeKey") String typeKey,
                                                         @PathVariable("regionKey") String regionKey) {

        List<ArticleDTO> list = articleService.getMost4ReadArticleByTypesAndRegion(typeKey, regionKey);
        return ResponseEntity.ok().body(list);
    }

    //13
    @GetMapping("/byRegionKey/{key}")
    public ResponseEntity<?> getLastArticleByRegionKey(@PathVariable("key") String key,
                                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "size", defaultValue = "5") int size) {

        PageImpl<ArticleDTO> list = articleService.getLast5ArticleByRegionKey(key, page, size);
        return ResponseEntity.ok().body(list);
    }

    //14
    @GetMapping("/getListByCategoryKey/{key}")
    public ResponseEntity<?> getListByCategoryKey(@PathVariable("key") String key) {

        List<ArticleDTO> list = articleService.findTop5ByArticleByCategory(key);
        return ResponseEntity.ok().body(list);
    }


    //15

    @GetMapping("/getCategoryKey/{key}")
    public ResponseEntity<?> getListByCategory(@PathVariable("key") String key) {

        List<ArticleDTO> list = articleService.getCategoryByPage(key);
        return ResponseEntity.ok().body(list);
    }


    //16 ,17 , 18 in ArticleLikeController

    //19
    @PostMapping("/increaseViewCount/{id}")
    public ResponseEntity<?> increaseViewCountByArticleId(@PathVariable("id") String id) {
        articleService.increaseViewCountById(id);

        return ResponseEntity.ok().build();
    }


    //20


    @GetMapping("/getList")
    public ResponseEntity<?> getArticleList() {
        List<ArticleDTO> list = articleService.getArticles();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<ArticleDTO>> filter(@RequestBody ArticleFilterDTO dto) {
        List<ArticleDTO> response = articleService.filter(dto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/update2/{id}")
    private ResponseEntity<?> updateImage(@PathVariable("id") String id,
                                          @RequestBody AttachUpdateDTO dto){

        articleService.updateArticleImage(dto,id);
        return ResponseEntity.ok().body("Successfully updated2");
    }


}
