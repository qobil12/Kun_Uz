package com.company.controller;

import com.company.dto.RegionDTO;
import com.company.dto.article.TypesDTO;
import com.company.enums.Language;
import com.company.enums.ProfileRole;
import com.company.service.TypesService;
import com.company.util.HttpHeaderUtil;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/types")
@RestController
public class ArticleTypeController {
    @Autowired
    private TypesService typesService;

    // PUBLIC

    @GetMapping("/public/lang")
    public ResponseEntity<List<TypesDTO>> getTypeListByLanguage(@RequestHeader(value = "Accept-Language", defaultValue = "uz")
                                                                Language language) {
        List<TypesDTO> list = typesService.getList(language);
        return ResponseEntity.ok().body(list);
    }

    // SECURED

    @PostMapping("/adm/create")
    public ResponseEntity<?> create(@RequestBody TypesDTO typesDto,
                                    HttpServletRequest request) {

        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        typesService.create(typesDto);
        return ResponseEntity.ok().body("Successfully updated");
    }


    @GetMapping("adm/list")
    public ResponseEntity<List<TypesDTO>> getList(HttpServletRequest request) {

        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);

        List<TypesDTO> list = typesService.getListOnlyForAdmin();
        return ResponseEntity.ok().body(list);
    }


    @PutMapping("/adm/{id}")
    private ResponseEntity<?> update(@PathVariable("id") Integer id,
                                     @RequestBody RegionDTO dto,
                                     HttpServletRequest request) {

        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        typesService.update(id, dto);
        return ResponseEntity.ok().body("Successfully updated");
    }

    @DeleteMapping("/adm/{id}")
    private ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                     HttpServletRequest request) {
        HttpHeaderUtil.getId(request ,ProfileRole.ADMIN);
        typesService.delete(id);
        return ResponseEntity.ok().body("Successfully deleted");
    }

    @GetMapping("/pagination")
    public ResponseEntity<PageImpl> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                               @RequestParam(value = "size", defaultValue = "5") int size) {

//        List<TypesDTO> list = typesService.getPagination(page , size);
        PageImpl response = typesService.paginationTypes(page, size);
        return ResponseEntity.ok().body(response);
    }


}
