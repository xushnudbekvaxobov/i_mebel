package imebel.imebel.controller;

import imebel.imebel.dto.request.UserUpdateDto;
import imebel.imebel.dto.response.ApiResponse;
import imebel.imebel.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PreAuthorize("hasRole('MASTER')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getCurrentUser() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"getting user by email",userService.getCurrentUser(),200));
    }

    @PreAuthorize("hasRole('MASTER')")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<?>> updateCurrentUser(@RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"updating user by email",userService.updateCurrentUser(userUpdateDto),200));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'CLIENT')")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<?>> deleteCurrentUser() {
        userService.deleteMyAccount();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "deleted successfully",null,200));
    }



}
