package com.example.videouploader.serviceImpl;

import com.example.videouploader.exceptions.UserAlreadyExist;
import com.example.videouploader.model.User;
import com.example.videouploader.repo.UserRepository;
import com.example.videouploader.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.Optional;

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
    public User registerUser(OAuth2User principal) throws UserAlreadyExist {
        String email = null;
        String name = null;
        String ssoId = null;
        String tokenValue = null;
        String profile = null;
        String given_name = null;
        String family_name = null;

        if (principal instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) principal;
            email = oidcUser.getEmail();
            name = oidcUser.getFullName();
            ssoId = oidcUser.getSubject();
            tokenValue = oidcUser.getIdToken().getTokenValue();
            given_name = (String) oidcUser.getAttributes().get("given_name");
            family_name = (String) oidcUser.getAttributes().get("family_name");
            profile = (String) oidcUser.getAttributes().get("picture");
        } else if (principal != null) {
            email = (String) principal.getAttributes().get("email");
            name = (String) principal.getAttributes().get("name");
            ssoId = (String) principal.getAttributes().get("sub");
            given_name = (String) principal.getAttributes().get("given_name");
            family_name = (String) principal.getAttributes().get("family_name");
            profile = (String) principal.getAttributes().get("picture");
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) principal;
        }


        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }


        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setSsoId(ssoId);
        user.setTokenValue(tokenValue);
        user.setGiven_name(given_name);
        user.setFamily_name(family_name);
        user.setPictureUrl(profile);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());


        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }


        return userRepository.save(user);
    }

}
