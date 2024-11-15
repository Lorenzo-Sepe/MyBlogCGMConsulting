package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Authority;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.entity.enumeration.AuthorityName;
import it.cgmconsulting.myblog.exception.ConflictException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.SignUpRequest;
import it.cgmconsulting.myblog.repository.AuthorityRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.utils.GenericMail;
import it.cgmconsulting.myblog.utils.Msg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final EmailService emailService;

    public String signup(SignUpRequest request){
        if(userRepository.existsByUsernameOrEmail(request.username(), request.email()))
            throw new ConflictException(Msg.USER_ALREADY_PRESENT);
        Authority authority = authorityRepository.findByDefaultAuthorityTrueAndVisibleTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Authority", "defaultAuthority", true));
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
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
}
