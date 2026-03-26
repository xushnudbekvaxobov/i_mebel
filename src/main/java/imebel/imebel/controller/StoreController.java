package imebel.imebel.controller;

import imebel.imebel.dto.request.StoreDto;
import imebel.imebel.dto.response.ApiResponse;
import imebel.imebel.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PreAuthorize("hasRole('MASTER')")
    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<?>> getMyStore(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Getting store profile", storeService.getMyStore(), 200));
    }

    @PreAuthorize("hasRole('MASTER')")
    @PostMapping(value = "/me/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createMyStore(@ModelAttribute StoreDto storeDto,
                                                        @RequestPart(value = "image", required = false) MultipartFile file){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Creating store profile", storeService.createMyStore(storeDto,file), 201));
    }

    @PreAuthorize("hasRole('MASTER')")
    @PutMapping("/me/profile")
    public ResponseEntity<ApiResponse<?>> updateMyStore(@RequestBody StoreDto storeDto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Update store profile", storeService.updateMyStore(storeDto), 200));
    }
    @PreAuthorize("hasRole('MASTER')")
    @PatchMapping(value = "/me/profile/banner-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> updateMyStoreBannerImage(@RequestPart MultipartFile file){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Store bannerImage updated", storeService.updateStoreBannerImage(file), 200));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'CLIENT')")
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllStore(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Getting store profile", storeService.getAllStores(page,size), 200));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getStoreById(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Getting store profile", storeService.getStoreById(id), 200));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'CLIENT')")
    @GetMapping("/{storeId}/products")
    public ResponseEntity<ApiResponse<?>> getStoreProducts(@PathVariable Long storeId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Getting store profile", storeService.getStoreProducts(storeId), 200));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'CLIENT')")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchStore(@RequestParam(required = false) String name,
                                                      @RequestParam(required = false) Long categoryId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "successfully search store", storeService.searchStore(name, categoryId), 200));

    }
}
