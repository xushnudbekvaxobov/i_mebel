package imebel.imebel.controller;

import imebel.imebel.dto.response.ApiResponse;
import imebel.imebel.entity.StoreCategoriesEntity;
import imebel.imebel.service.StoreCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/store/categories")
public class StoreCategoryController {

    private final StoreCategoryService storeCategoryService;

    public StoreCategoryController(StoreCategoryService storeCategoryService) {
        this.storeCategoryService = storeCategoryService;
    }

    @PreAuthorize("hasRole('MASTER')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getMyCategories(Authentication  authentication) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "get master specializations", storeCategoryService.getMyCategories(authentication.getName()),200));
    }

    @PreAuthorize("hasRole('MASTER')")
    @PostMapping("/me/{name}")
    public ResponseEntity<ApiResponse<?>> createMyCategory(@PathVariable String name) {
        storeCategoryService.createMyCategory(name);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "created", null,201));
    }

    @PreAuthorize("hasRole('MASTER')")
    @DeleteMapping("/me/{name}")
    public ResponseEntity<ApiResponse<?>> deleteMyCategory(@PathVariable String name) {
    storeCategoryService.deleteMyCategory(name);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(true, "deleted", null,204));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'CLIENT')")
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponse<?>> getAllCategories(@PathVariable Long storeId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "success", storeCategoryService.getAllStoreCategories(storeId),200));
    }



}
