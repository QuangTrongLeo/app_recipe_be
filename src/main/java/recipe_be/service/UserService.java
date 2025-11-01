package recipe_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import recipe_be.dto.request.auth.RegisterRequest;
import recipe_be.dto.request.user.UpdateProfileRequest;
import recipe_be.dto.response.UserResponse;
import recipe_be.entity.Image;
import recipe_be.entity.User;
import recipe_be.enums.Role;
import recipe_be.mapper.UserMapper;
import recipe_be.repository.UserRepository;
import recipe_be.utils.CurrentUserUtils;
import recipe_be.utils.DateTimeUtils;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final UserMapper userMapper;

    // ===== TẠO TÀI KHOẢN ===
    public void createUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Tài khoản này đã tồn tại!");
        }

        User user = buildUser(request);
        userRepository.save(user);
    }

    // ===== CẬP NHẬT PROFILE =====
    public UserResponse updateProfileByEmail(UpdateProfileRequest request) {
        String email = CurrentUserUtils.getEmail();
        User user = getUserByEmail(email);

        if (StringUtils.hasText(request.getUsername())) {
            user.setUsername(request.getUsername());
        }
        if (StringUtils.hasText(request.getBio())) {
            user.setBio(request.getBio());
        }

        MultipartFile avatarFile = request.getAvatar();
        if (avatarFile != null && !avatarFile.isEmpty()) {

            // Xóa avatar cũ nếu có
            if (StringUtils.hasText(user.getAvatar())) {
                imageService.deleteByUrl(user.getAvatar());
            }

            // Upload avatar mới
            Image image = imageService.uploadAndSave(avatarFile);
            user.setAvatar(image.getUrl());
        }

        // Lưu user
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    // ===== THÔNG TIN NGƯỜI DÙNG ĐANG ĐĂNG NHẬP =====
    public UserResponse getUserByEmail(){
        String email = CurrentUserUtils.getEmail();
        return userMapper.toUserResponse(getUserByEmail(email));
    }

    // ===== Lấy User bằng email =====
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));
    }

    // ===== Tạo User chuẩn hóa ===
    private User buildUser(RegisterRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .avatar(null)
                .bio(null)
                .role(Role.USER)
                .createdAt(DateTimeUtils.nowVietnamTime())
                .favoriteRecipes(List.of())
                .build();
    }
}
