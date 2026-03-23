package imebel.imebel.controller;

import imebel.imebel.dto.response.ApiResponse;
import imebel.imebel.service.ProductImagesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products/images")
public class ProductImageController {

    private final ProductImagesService productImagesService;

    public ProductImageController(ProductImagesService productImagesService) {
        this.productImagesService = productImagesService;
    }

    @DeleteMapping("/{productId}/{imageId}")
    public ResponseEntity<ApiResponse<?>> deleteProductImage(@PathVariable Long productId, @PathVariable Long imageId) {
        productImagesService.deleteProductImage(productId, imageId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(true, "Cloudinary image deleted successfully", null,204));
    }

    @PostMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createImage(@PathVariable Long productId,
                                                      @RequestParam("files") MultipartFile file) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Cloudinary image created successfully", productImagesService.createProductImage(productId, file),201));
    }
}
