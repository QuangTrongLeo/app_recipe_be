package recipe_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import recipe_be.dto.request.auth.RegisterRequest;
import recipe_be.dto.request.user.UpdateProfileRequest;
import recipe_be.dto.response.StripeCustomerResponse;
import recipe_be.dto.response.UserResponse;
import recipe_be.entity.Cart;
import recipe_be.entity.Image;
import recipe_be.entity.User;
import recipe_be.enums.ErrorCode;
import recipe_be.enums.Role;
import recipe_be.exception.AppException;
import recipe_be.mapper.UserMapper;
import recipe_be.repository.UserRepository;
import recipe_be.utils.CurrentUserUtils;
import recipe_be.utils.DateTimeUtils;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final UserMapper userMapper;
    private final CartService cartService;
    private final StripeService stripeService;

    public void createUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.RESOURCE_ALREADY_EXISTS);
        }
        User user = buildUser(request);
        
        Cart cart = new Cart();
        cart.setUserId(user.getId());
        cartService.create(cart);

        StripeCustomerResponse  customer = stripeService.createCustomer(request.getUsername(), request.getEmail());
        user.setCustomerId(customer.getId());

        save(user);
    }
    
    public UserResponse updateProfileByUserId(UpdateProfileRequest request) {
        String userId = CurrentUserUtils.getUserId();
        User user = getUserById(userId);

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


    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public Boolean deleteUser(String userId) {
        User user = getUserById(userId);

        // Nếu có avatar, xóa trên cloud
        if (StringUtils.hasText(user.getAvatar())) {
            imageService.deleteByUrl(user.getAvatar());
        }

        cartService.deleteByUserId(userId);
        userRepository.delete(user);
        return true;
    }

    public Long totalUsers() {
        return userRepository.count();
    }

    public UserResponse getUserById(){
        String userId = CurrentUserUtils.getUserId();
        return userMapper.toUserResponse(getUserById(userId));
    }

    public User getUserById(String userId){
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User không tồn tại!"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }


    public void save(User user) {
        userRepository.save(user);
    }
    
    private User buildUser(RegisterRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
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
