package imebel.imebel.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreDto {
        @NotBlank(message = "Store name required")
        @Size(min = 2, max = 60, message = "Store name must be between 2 and 60 characters")
        private String name;
        @Size(max = 400, message = "Description must be at most 400 characters")
        private String description;
        @Size(max = 100, message = "Telegram link must be at most 100 characters")
        @Pattern(
                regexp = "^(https?:\\/\\/)?(t\\.me\\/)[A-Za-z0-9_]{5,32}\\/?$",
                message = "Telegram link is invalid"
        )
        private String telegramLink;
        @Size(max = 100, message = "Instagram link must be at most 100 characters")
        @Pattern(
                regexp = "^(https?:\\/\\/)?(www\\.)?instagram\\.com\\/[A-Za-z0-9_.]+\\/?$",
                message = "Instagram link is invalid"
        )
        private String instagramLink;
        @Size(max = 255, message = "Address must be at most 255 characters")
        private String address;
        @DecimalMin(value = "-90.0", inclusive = true, message = "Latitude must be greater than or equal to -90")
        @DecimalMax(value = "90.0", inclusive = true, message = "Latitude must be less than or equal to 90")
        private BigDecimal latitude;
        @DecimalMin(value = "-180.0", inclusive = true, message = "Longitude must be greater than or equal to -180")
        @DecimalMax(value = "180.0", inclusive = true, message = "Longitude must be less than or equal to 180")
        private BigDecimal longitude;
    }
