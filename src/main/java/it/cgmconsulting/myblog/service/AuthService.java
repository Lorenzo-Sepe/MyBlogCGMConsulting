package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Authority;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.ConflictException;
import it.cgmconsulting.myblog.payload.request.SignUpRequest;
import it.cgmconsulting.myblog.repository.AuthorityRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.utils.Msg;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    public String signup(SignUpRequest request)
    {
        if(userRepository.existsByUsernameOrEmail(request.username(), request.email()))
            throw new ConflictException(Msg.USER_ALREADY_PRESENT);

        Authority authority = authorityRepository.findByDefaultAuthorityTrueAndVisibleTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Authority", "defaultAuthority", true));

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .enabled(false)
                .build();
        userRepository.save(user);
        return Msg.USER_SIGNUP_FIRST_STEP;
    }


}