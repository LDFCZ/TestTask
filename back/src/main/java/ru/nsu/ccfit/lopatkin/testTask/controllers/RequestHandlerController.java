package ru.nsu.ccfit.lopatkin.testTask.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.ccfit.lopatkin.testTask.services.RequestService;
import ru.nsu.ccfit.lopatkin.testTask.services.CategoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
public class RequestHandlerController {

    private final RequestService requestService;

    private final CategoryService categoryService;

    @Autowired
    public RequestHandlerController(RequestService requestService, CategoryService categoryService) {
        this.requestService = requestService;
        this.categoryService = categoryService;
    }

    @GetMapping("bid")
    public ResponseEntity<?> getBanner(
            @RequestHeader(value = HttpHeaders.USER_AGENT) String userAgent,
            @RequestParam(value = "cat") List<String> requestCategoryIdList,
            HttpServletRequest ipAddress) {
        try {
            return requestService.getBannerByCategories(userAgent, requestCategoryIdList, ipAddress);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("bid/categories")
    public ResponseEntity<?> getCategories() {
        try {
            return categoryService.getAllNotDeletedCategories();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
