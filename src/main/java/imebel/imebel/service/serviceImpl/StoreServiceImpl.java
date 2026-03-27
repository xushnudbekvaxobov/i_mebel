package imebel.imebel.service.serviceImpl;

import com.cloudinary.Cloudinary;
import imebel.imebel.dto.request.StoreDto;
import imebel.imebel.dto.response.ProductResponseDto;
import imebel.imebel.dto.response.StoreResponseDto;
import imebel.imebel.dto.response.PageResponseDto;
import imebel.imebel.entity.CategoryEntity;
import imebel.imebel.entity.ProductEntity;
import imebel.imebel.entity.StoreEntity;
import imebel.imebel.entity.UserEntity;
import imebel.imebel.exception.AppBadException;
import imebel.imebel.exception.DataNotFoundException;
import imebel.imebel.mapper.ProductMapper;
import imebel.imebel.mapper.StoreMapper;
import imebel.imebel.repository.CategoryRepository;
import imebel.imebel.repository.ProductRepository;
import imebel.imebel.repository.StoreRepository;
import imebel.imebel.repository.UserRepository;
import imebel.imebel.service.StoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreServiceImpl implements StoreService {

    private final UserRepository userRepository;
    private final StoreMapper storeMapper;
    private final StoreRepository storeRepository;
    private final Cloudinary cloudinary;
    private final ProductsServiceImpl productsServiceImpl;
    private final ProductMapper productMapper;

    public StoreServiceImpl(UserRepository userRepository, StoreMapper storeMapper, StoreRepository storeRepository, Cloudinary cloudinary, ProductsServiceImpl productsServiceImpl, ProductMapper productMapper) {
        this.userRepository = userRepository;
        this.storeMapper = storeMapper;
        this.storeRepository = storeRepository;
        this.cloudinary = cloudinary;
        this.productsServiceImpl = productsServiceImpl;
        this.productMapper = productMapper;
    }

    @Override
    public StoreResponseDto getMyStore(String email) {
        StoreEntity storeEntity = storeRepository.findByUserEntity_Email(email).orElseThrow(()-> new DataNotFoundException("Master Profile not found"));
        return storeMapper.toDto(storeEntity);
    }

    @Override
    public PageResponseDto<StoreResponseDto> getAllStores(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<StoreEntity> masterProfileEntities = storeRepository.findAll(pageRequest);
        List<StoreResponseDto> storeResponseDtoList = masterProfileEntities.map(storeMapper::toDto).toList();
        return PageResponseDto.<StoreResponseDto>builder()
                .content(storeResponseDtoList)
                .page(masterProfileEntities.getNumber())
                .size(masterProfileEntities.getSize())
                .totalElements(masterProfileEntities.getTotalElements())
                .totalPages(masterProfileEntities.getTotalPages())
                .last(masterProfileEntities.isLast())
                .build();
    }

    @Override
    public StoreResponseDto createMyStore(StoreDto masterProfileCreateDto, MultipartFile file, String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        Map<String, String> options = new HashMap<>();
        options.put("folder", "imebel/stores/banner");
        productsServiceImpl.validateImage(file);
        new StoreEntity();
        StoreEntity storeEntity;
        try {
            Map uploadResults = cloudinary.uploader().upload(file.getBytes(), options);
            String imageUrl = uploadResults.get("secure_url").toString();
            String publicId = uploadResults.get("public_id").toString();
            storeEntity = storeMapper.toEntity(masterProfileCreateDto, userEntity, imageUrl, publicId);
            storeRepository.save(storeEntity);
        }catch (IOException e) {
                throw new AppBadException("Image upload failed");
        }
        return getStoreById(storeEntity.getId());
    }

    @Override
    public StoreResponseDto updateMyStore(StoreDto storeDto, String email) {
       StoreEntity storeEntity = storeRepository.findByUserEntity_Email(email).orElseThrow(() -> new DataNotFoundException("User not found"));
       StoreEntity updatedProfile = storeMapper.toEntity(storeEntity, storeDto);
       storeRepository.save(updatedProfile);
        return storeMapper.toDto(updatedProfile);
    }

    @Override
    public StoreResponseDto getStoreById(Long id) {
        StoreEntity storeEntity = storeRepository.findById(id).orElseThrow(() -> new DataNotFoundException("MasterProfile not found"));
        return storeMapper.toDto(storeEntity);
    }

    @Override
    public String updateStoreBannerImage(MultipartFile file, String email) {
        StoreEntity storeEntity = storeRepository.findByUserEntity_Email(email).orElseThrow(() -> new DataNotFoundException("Store not found"));
        Map<String, String> options = new HashMap<>();
        options.put("folder", "imebel/stores/banner");
        ProductsServiceImpl.validateImage(file);
        String imageUrl;
        try{
            if (storeEntity.getBannerImageId() != null) {
                cloudinary.uploader().destroy(storeEntity.getBannerImageId(), Map.of("invalidate", "true"));
            }
            Map<?, ?> uploadResults = cloudinary.uploader().upload(file.getBytes(), options);
            imageUrl = uploadResults.get("secure_url").toString();
            String publicId = uploadResults.get("public_id").toString();
            storeEntity.setBannerImageUrl(imageUrl);
            storeEntity.setBannerImageId(publicId);
            storeRepository.save(storeEntity);
        }catch (IOException e){
            throw new AppBadException("Image upload failed");
        }
        return imageUrl;
    }

    @Transactional
    @Override
    public void deleteBannerImage(String imageUrl, String email) {
        StoreEntity storeEntity = storeRepository.findByUserEntity_Email(email).orElseThrow(() -> new DataNotFoundException("Store not found"));
        if (storeEntity.getBannerImageUrl() != null && storeEntity.getBannerImageUrl().equals(imageUrl)) {
            try {
                cloudinary.uploader().destroy(storeEntity.getBannerImageId(), Map.of("invalidate", "true"));
                storeEntity.setBannerImageUrl(null);
                storeEntity.setBannerImageId(null);
                storeRepository.save(storeEntity);
            } catch (IOException e) {
                throw new AppBadException("Failed to delete banner image");
            }
        } else {
            throw new DataNotFoundException("Banner image not found for this store");
        }
    }

    @Override
    public List<ProductResponseDto> getStoreProducts(Long id) {
       StoreEntity storeEntity =  storeRepository.findById(id).orElseThrow(() -> new DataNotFoundException("StoreProfile not found"));
        return storeEntity.getProductList().stream().map(productMapper::toDto).toList();
    }

    @Override
    public List<StoreResponseDto> searchStore(String name, Long categoryId) {
        List<StoreEntity> storeEntityList = storeRepository.searchStores(name, categoryId);
        return storeEntityList.stream().map(storeMapper::toDto).toList();
    }
}
