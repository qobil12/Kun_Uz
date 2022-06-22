package com.company.controller;

import com.company.dto.CategoryDTO;
import com.company.dto.JwtDTO;
import com.company.enums.Language;
import com.company.enums.ProfileRole;
import com.company.service.CategoryService;
import com.company.util.HttpHeaderUtil;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/category")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    // PUBLIC
    @GetMapping("/public")
    public ResponseEntity<List<CategoryDTO>> getCategoryListByLanguage(@RequestHeader(value = "Accept-Language", defaultValue = "uz")
                                                                       Language language) {
        List<CategoryDTO> list = categoryService.getList(language);
        return ResponseEntity.ok().body(list);
    }


    // SECURED
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CategoryDTO categoryDto,
                                    HttpServletRequest request) {

        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);

        categoryService.create(categoryDto);
        return ResponseEntity.ok().body("Successfully created");
    }

    @GetMapping("/admin")
    public ResponseEntity<List<CategoryDTO>> getList(HttpServletRequest request) {

        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        List<CategoryDTO> list = categoryService.getListOnlyForAdmin();
        return ResponseEntity.ok().body(list);
    }


    @PutMapping("/update/{id}")
    private ResponseEntity<?> update(@PathVariable("id") Integer id,
                                     @RequestBody CategoryDTO dto,
                                     HttpServletRequest request) {

        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        categoryService.update(id, dto);
        return ResponseEntity.ok().body("Succsessfully updated");
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                     HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);

        categoryService.delete(id);
        return ResponseEntity.ok().body("Sucsessfully deleted");
    }


}
