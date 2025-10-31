package recipe_be.mb_gr03.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import recipe_be.mb_gr03.dto.request.auth.RegisterRequest;
import recipe_be.mb_gr03.dto.request.user.UpdateProfileRequest;
import recipe_be.mb_gr03.dto.response.user.UserResponse;
import recipe_be.mb_gr03.entity.Image;
import recipe_be.mb_gr03.entity.User;
import recipe_be.mb_gr03.enums.EnumRole;
import recipe_be.mb_gr03.mapper.user.UserMapper;
import recipe_be.mb_gr03.repository.user.UserRepository;
import recipe_be.mb_gr03.service.file.ImageService;
import recipe_be.mb_gr03.utils.CurrentUserUtils;
import recipe_be.mb_gr03.utils.DateTimeUtils;
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

    // ===== Lấy User bằng email =====
    protected User getUserByEmail(String email) {
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
                .role(EnumRole.USER)
                .createdAt(DateTimeUtils.nowVietnamTime())
                .favoriteRecipes(List.of())
                .build();
    }
}
