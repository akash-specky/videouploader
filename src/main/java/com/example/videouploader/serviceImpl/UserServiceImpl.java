package com.example.videouploader.serviceImpl;

import com.example.videouploader.model.User;
import com.example.videouploader.repo.UserRepository;
import com.example.videouploader.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public User registerUser(User user) throws Exception {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Email already exists");
        }

        if (userRepository.findByMobileNo(user.getMobileNo()).isPresent()) {
            throw new Exception("Mobile number already exists");
        }

        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {

            user.setPassword(null);
        }


        return userRepository.save(user);
    }

    @Override
    public User registerUser(Object principal) throws Exception {
        String email = null;
        String name = null;
        String ssoId = null;

        if (principal instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) principal;
            email = oidcUser.getEmail();
            name = oidcUser.getFullName();
            ssoId = oidcUser.getSubject();
        } else if (principal instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) principal;
            email = (String) oauthUser.getAttributes().get("email");
            name = (String) oauthUser.getAttributes().get("name");
            ssoId = (String) oauthUser.getAttributes().get("sub");
        }
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setSsoId(ssoId);
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Email already exists");
        }

        if (userRepository.findByMobileNo(user.getMobileNo()).isPresent()) {
            throw new Exception("Mobile number already exists");
        }
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {

            user.setPassword(null);
        }


        return userRepository.save(user);
    }
}
