package imebel.imebel.controller;

import imebel.imebel.dto.response.ApiResponse;
import imebel.imebel.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/specializations")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllCategories() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Getting specializations", categoryService.getAllCategories(), 200));
    }
    @PreAuthorize("hasRole('MASTER')")
    @PostMapping("/{name}")
    public ResponseEntity<ApiResponse<?>> createCategory(@PathVariable String name) {
        categoryService.createCategory(name);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Creating specialization", null, 201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateCategory(@PathVariable Long id, @RequestParam String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Updating specialization",  categoryService.updateCategory(id, name), 200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getCategoryById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Getting specialization",  categoryService.getCategoryById(id), 200));
    }

}
