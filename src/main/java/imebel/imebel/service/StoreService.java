package imebel.imebel.service;

import imebel.imebel.dto.request.StoreDto;
import imebel.imebel.dto.response.ProductResponseDto;
import imebel.imebel.dto.response.StoreResponseDto;
import imebel.imebel.dto.response.PageResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface StoreService {
    StoreResponseDto getMyStore();
    PageResponseDto<StoreResponseDto> getAllStores(int page, int size);
    StoreResponseDto createMyStore(StoreDto storeDto, MultipartFile file);
    StoreResponseDto updateMyStore(StoreDto storeDto);
    StoreResponseDto getStoreById(Long id);
    String updateStoreBannerImage(MultipartFile file);
    List<ProductResponseDto> getStoreProducts(Long id);
    List<StoreResponseDto> searchStore(String name, Long categoryId);

}
