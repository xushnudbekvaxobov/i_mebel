package imebel.imebel.controller;

import imebel.imebel.dto.request.ProductCreateDto;
import imebel.imebel.dto.response.ApiResponse;
import imebel.imebel.dto.response.PageResponse;
import imebel.imebel.service.ProductsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductsService productsService;

    public ProductController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @PreAuthorize("hasRole('MASTER')")
    @PostMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> addProduct(@Valid @ModelAttribute ProductCreateDto productDto,
                                                     @RequestParam(value = "images")List<MultipartFile> images,
                                                     Authentication authentication) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "successfully", productsService.createProducts(productDto, images, authentication.getName()),201));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PageResponse<?>>> getProducts(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    Authentication authentication){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "successfully", productsService.getMyProducts(page, size, authentication.getName()),200));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getProductById(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "successfully", productsService.getProductById(id),200));
    }

    @PreAuthorize("hasRole('MASTER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateProduct(@PathVariable Long id,
                                                        @Valid @RequestBody ProductCreateDto productDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "successfully", productsService.updateProduct(id, productDto),200));
    }

    @PreAuthorize("hasRole('MASTER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable Long id){
        productsService.deleteProduct(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(true, "successfully", null,204));
    }

}
