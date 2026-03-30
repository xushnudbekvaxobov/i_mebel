package imebel.imebel.controller;

import imebel.imebel.dto.response.ApiResponse;
import imebel.imebel.service.ProductImagesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/products/images")
public class ProductImageController {

    private final ProductImagesService productImagesService;

    public ProductImageController(ProductImagesService productImagesService) {
        this.productImagesService = productImagesService;
    }

    @PreAuthorize("hasRole('MASTER')")
    @DeleteMapping("/{productId}/{imageId}")
    public ResponseEntity<ApiResponse<?>> deleteProductImage(@PathVariable UUID productId, @PathVariable UUID imageId) {
        productImagesService.deleteProductImage(productId, imageId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(true, "Cloudinary image deleted successfully", null,200));
    }

    @PreAuthorize("hasRole('MASTER')")
    @PostMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createImage(@PathVariable UUID productId,
                                                      @RequestParam("files") MultipartFile file) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Cloudinary image created successfully", productImagesService.createProductImage(productId, file),201));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getProductImage(@PathVariable UUID id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Cloudinary image found successfully", productImagesService.getProductImage(id),200));
    }

    @PreAuthorize("hasRole('MASTER')")
    @PatchMapping("/main-image/{productId}/{imageId}")
    public ResponseEntity<ApiResponse<?>> changeMainImage(@PathVariable UUID productId, @PathVariable UUID imageId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Main image changed successfully", productImagesService.changeMainImage(productId, imageId),200));
    }
}
