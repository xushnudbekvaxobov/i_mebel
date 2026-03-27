package imebel.imebel.controller;

import imebel.imebel.dto.request.UserUpdateDto;
import imebel.imebel.dto.response.ApiResponse;
import imebel.imebel.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getCurrentUser(Authentication authentication) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"getting user by email",userService.getCurrentUser(authentication.getName()),200));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'CLIENT')")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<?>> updateCurrentUser(@Valid @RequestBody UserUpdateDto userUpdateDto,
                                                            Authentication authentication) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"updating user by email",userService.updateCurrentUser(userUpdateDto, authentication.getName()),200));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'CLIENT')")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<?>> deleteCurrentUser(Authentication authentication) {
        userService.deleteMyAccount(authentication.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "deleted successfully",null,204));
    }



}
