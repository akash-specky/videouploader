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

        if (principal instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) principal;
            email = oidcUser.getEmail();
            name = oidcUser.getFullName();
            ssoId = oidcUser.getSubject();
            tokenValue = oidcUser.getIdToken().getTokenValue();
        } else if (principal != null) {
            email = (String) ((OAuth2User) principal).getAttributes().get("email");
            name = (String) ((OAuth2User) principal).getAttributes().get("name");
            ssoId = (String) ((OAuth2User) principal).getAttributes().get("sub");
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) principal;
            String registrationId = authToken.getAuthorizedClientRegistrationId();

//            // Retrieve the OAuth2AuthorizedClient
//            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
//                    registrationId,
//                    authToken.getName());
//
//            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
//            tokenValue = accessToken.getTokenValue();

        }
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setSsoId(ssoId);
        user.setTokenValue(tokenValue);
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExist("Email already exists");
        }

        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {

            user.setPassword(null);
        }


        return userRepository.save(user);
    }
}
