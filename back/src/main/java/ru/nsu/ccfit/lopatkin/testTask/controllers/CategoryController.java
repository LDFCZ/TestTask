package ru.nsu.ccfit.lopatkin.testTask.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.ccfit.lopatkin.testTask.models.Category;
import ru.nsu.ccfit.lopatkin.testTask.services.CategoryService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RolesAllowed("ROLE_ADMIN")
@RequestMapping("/")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("category")
    public ResponseEntity<List<Category>> getCategories(@RequestParam(required = false) String name) {
        try {
            if (name != null)
                return categoryService.findCategoryByName(name);
            else
                return categoryService.getAllNotDeletedCategories();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("category/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        try {
            return categoryService.getCategoryById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("category")
    public ResponseEntity<?> createNewCategory(@RequestBody @Valid Category category) {
        try {
            return categoryService.createNewCategory(category);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("category/{id}")
    public ResponseEntity<?> editCategory(@RequestBody @Valid Category editedCategory, @PathVariable Long id) {
        try {
            return categoryService.editCategory(editedCategory, id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            return categoryService.deleteCategory(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
