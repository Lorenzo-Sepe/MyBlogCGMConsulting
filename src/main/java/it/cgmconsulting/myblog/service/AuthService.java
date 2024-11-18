package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Authority;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.entity.enumeration.AuthorityName;
import it.cgmconsulting.myblog.exception.BadCredentialsException;
import it.cgmconsulting.myblog.exception.ConflictException;
import it.cgmconsulting.myblog.exception.DisabledException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.SignInRequest;
import it.cgmconsulting.myblog.payload.request.SignUpRequest;
import it.cgmconsulting.myblog.payload.response.JwtAuthenticationResponse;
import it.cgmconsulting.myblog.repository.AuthorityRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.utils.GenericMail;
import it.cgmconsulting.myblog.utils.Msg;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String signup(SignUpRequest request){
        if(userRepository.existsByUsernameOrEmail(request.username(), request.email()))
            throw new ConflictException(Msg.USER_ALREADY_PRESENT);
        Authority authority = authorityRepository.findByDefaultAuthorityTrueAndVisibleTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Authority", "defaultAuthority", true));
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(false)
                .authority(authority)
                .confirmCode(UUID.randomUUID().toString())
                .build();
        userRepository.save(user);
        GenericMail mail = new GenericMail(Msg.MAIL_SIGNUP_SUBJECT, Msg.MAIL_SIGNUP_BODY.concat(user.getConfirmCode()), user.getEmail());
        emailService.sendVerificationMail(mail);
        return Msg.USER_SIGNUP_FIRST_STEP;
    }

    public String verifyEmail(String confirmCode){
        // ricerca user per codice conferma
        User user = userRepository.findByConfirmCode(confirmCode)
                .orElseThrow(() -> new ResourceNotFoundException("User", "confirm code", confirmCode));
        // abilito lo user e gli cambio il ruolo da quello di default a MEMBER; e resetto il confirmCode
        user.setEnabled(true);
        user.setConfirmCode(null);
        user.setAuthority(authorityRepository.findByAuthorityNameAndVisibleTrue(AuthorityName.MEMBER)
                .orElseThrow(()-> new ResourceNotFoundException("Authority", "name", AuthorityName.MEMBER.name())));
        userRepository.save(user);
        return Msg.USER_SIGNUP_SECOND_STEP;
    }

    public JwtAuthenticationResponse signin(SignInRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.usernameOrEmail(), request.usernameOrEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username or email", request.usernameOrEmail()));
        if(!user.isEnabled()) {
            Authority authority = authorityRepository.findByDefaultAuthorityTrueAndVisibleTrue()
                    .orElseThrow(() -> new ResourceNotFoundException("Authority", "defaultAuthority", true));
            if (authority.getAuthorityName().name().equals(user.getAuthority().getAuthorityName().name()))
                throw new DisabledException("Please check your email to activate your account");
            throw new DisabledException("You are banned");
        }
        if(!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new BadCredentialsException("Bad credentials");

        String authority = user.getAuthority().getAuthorityName().name();
        String jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authority(authority)
                .token(jwt)
                .build();
    }

    public String modifyUserAuthority(int id, String auth) {
        // trovare l'utente
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User", "id", id));
        // verificare che il ruolo dell'utente non sia quello di default
        if(user.getAuthority().isDefaultAuthority())
            throw new ConflictException(Msg.USER_HAS_DEFAULT_AUTHORITY);
        try{
            Authority authority = authorityRepository.findByAuthorityNameAndVisibleTrue(AuthorityName.valueOf(auth.toUpperCase()))
                    .orElseThrow(()-> new ResourceNotFoundException("Authority", "name", auth));
            if(user.getAuthority().equals(authority))
                throw new ConflictException(Msg.USER_HAS_SAME_AUTHORITY);
            user.setAuthority(authority);
        } catch (IllegalArgumentException ex){
            return Msg.INVALID_AUTHORITY;
        }
        // salvare
        userRepository.save(user);
        return Msg.AUTHORITY_CHANGED;
    }
}
