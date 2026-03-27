package imebel.imebel.service.serviceImpl;

import com.cloudinary.Cloudinary;
import imebel.imebel.dto.request.ProductCreateDto;
import imebel.imebel.dto.response.PageResponse;
import imebel.imebel.dto.response.ProductResponseDto;
import imebel.imebel.entity.StoreEntity;
import imebel.imebel.entity.ProductEntity;
import imebel.imebel.entity.ProductImageEntity;
import imebel.imebel.entity.CategoryEntity;
import imebel.imebel.exception.AppBadException;
import imebel.imebel.exception.DataNotFoundException;
import imebel.imebel.mapper.ProductMapper;
import imebel.imebel.repository.StoreRepository;
import imebel.imebel.repository.ProductImageRepository;
import imebel.imebel.repository.ProductRepository;
import imebel.imebel.repository.CategoryRepository;
import imebel.imebel.service.ProductsService;
import imebel.imebel.util.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ProductsServiceImpl implements ProductsService {
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final Cloudinary cloudinary;
    private final ProductImageRepository productImageRepository;

    public ProductsServiceImpl(StoreRepository storeRepository, CategoryRepository categoryRepository, ProductMapper productMapper, ProductRepository productRepository, Cloudinary cloudinary, ProductImageRepository productImageRepository) {
        this.storeRepository = storeRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.cloudinary = cloudinary;
        this.productImageRepository = productImageRepository;
    }

    @Override
    public ProductResponseDto createProducts(ProductCreateDto productCreateDto, List<MultipartFile> images, String email) {
        StoreEntity masterProfile = storeRepository.findByUserEntity_Email(email).orElseThrow(()-> new DataNotFoundException("master not found"));
        CategoryEntity categoryEntity = categoryRepository.findByName(productCreateDto.getCategory()).orElseThrow(()-> new DataNotFoundException("category not found"));
        if (images == null || images.size() > 3) {
            throw new AppBadException("Invalid number of images. You can upload up to max 3 images.");
        }
        ProductEntity productEntity = productMapper.toEntity(masterProfile, productCreateDto, categoryEntity);
        productRepository.save(productEntity);
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            validateImage(image);
           try {
               Map<String, String> options = new HashMap<>();
               options.put("folder", "imebel/products");
               Map uploadResult = cloudinary.uploader().upload(image.getBytes(), options);
               String imageUrl = uploadResult.get("secure_url").toString();
               String publicId = uploadResult.get("public_id").toString();
               ProductImageEntity productImageEntity = ProductImageEntity.builder()
                       .imageUrl(imageUrl)
                       .publicId(publicId)
                       .isMain(i == 0)
                       .productEntity(productEntity)
                       .build();
               productImageRepository.save(productImageEntity);
           }catch (IOException e){
                throw new AppBadException("Upload image failed.");
           }
        }
        return getProductById(productEntity.getId());
    }

    @Override
    public PageResponse<ProductResponseDto> getMyProducts(int page, int size, String email) {
        StoreEntity storeEntity = storeRepository.findByUserEntity_Email(email).orElseThrow(()-> new DataNotFoundException("master not found"));
        PageRequest pageRequest =  PageRequest.of(page, size);
        Page<ProductEntity> productEntityPage = productRepository.getAllByStoreEntity(storeEntity,pageRequest);
        List<ProductResponseDto> productResponseDtoList = productEntityPage.map(productMapper::toDto).toList();
        return PageResponse.<ProductResponseDto>builder()
                .content(productResponseDtoList)
                .page(productEntityPage.getNumber())
                .size(productEntityPage.getSize())
                .totalElements(productEntityPage.getTotalElements())
                .totalPages(productEntityPage.getTotalPages())
                .isLast(productEntityPage.isLast())
                .build();
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(()-> new DataNotFoundException("product not found"));
        return productMapper.toDto(productEntity);
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductCreateDto productCreateDto) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(()-> new DataNotFoundException("product not found"));
        CategoryEntity categoryEntity = categoryRepository.findByName(productCreateDto.getCategory()).orElseThrow(()-> new DataNotFoundException("category not found"));
        productMapper.toEntity(productCreateDto, categoryEntity);
        ProductEntity savedProduct = productRepository.save(productEntity);
        return productMapper.toDto(savedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> new DataNotFoundException("product not found"));
        productEntity.setStatus(ProductStatus.DELETED);
        productRepository.save(productEntity);
    }

    public static void validateImage(MultipartFile file) {
        if(file.isEmpty()) {
            throw new AppBadException("File is empty");
        }
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new AppBadException("Invalid file type. Only image files are allowed.");
        }
        if(file.getSize() > 10*1024 * 1024) {
            throw new AppBadException("File size exceeds the maximum limit of 10MB");
        }

    }


}
