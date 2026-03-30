package imebel.imebel.service;

import imebel.imebel.dto.request.StoreDto;
import imebel.imebel.dto.response.ProductResponseDto;
import imebel.imebel.dto.response.StoreResponseDto;
import imebel.imebel.dto.response.PageResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface StoreService {
    StoreResponseDto getMyStore(String email);
    PageResponseDto<StoreResponseDto> getAllStores(int page, int size);
    StoreResponseDto createMyStore(StoreDto storeDto, MultipartFile file, String email);
    StoreResponseDto updateMyStore(StoreDto storeDto, String email);
    StoreResponseDto getStoreById(UUID id);
    String updateStoreBannerImage(MultipartFile file, String email);
    List<ProductResponseDto> getStoreProducts(UUID id);
    List<StoreResponseDto> searchStore(String name, UUID categoryId);
    void deleteBannerImage(String imageUrl, String email);

}
