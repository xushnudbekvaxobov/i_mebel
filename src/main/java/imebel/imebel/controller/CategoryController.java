package imebel.imebel.controller;

import imebel.imebel.dto.response.ApiResponse;
import imebel.imebel.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
   // @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'CLIENT')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllCategories() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Getting categories", categoryService.getAllCategories(), 200));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{name}")
    public ResponseEntity<ApiResponse<?>> createCategory(@PathVariable String name) {
        categoryService.createCategory(name);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Creating categories", null, 201));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateCategory(@PathVariable UUID id, @RequestParam String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Updating categories",  categoryService.updateCategory(id, name), 200));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Getting categories",  categoryService.getCategoryById(id), 200));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Deleting categories", null, 204));
    }

}
