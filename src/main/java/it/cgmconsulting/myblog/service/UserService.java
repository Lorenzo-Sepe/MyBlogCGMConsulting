package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.ConflictException;
import it.cgmconsulting.myblog.payload.response.UserResponse;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.utils.Msg;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String changePwd(UserDetails userDetails, String oldPwd, String newPwd,  String newPwd2) {
        User user = (User) userDetails;
        if(!passwordEncoder.matches(oldPwd, user.getPassword()))
            throw new ConflictException(Msg.PWD_INCORRECT);
        if(!newPwd.equals(newPwd2))
            throw new ConflictException(Msg.PASSWORD_MISMATCH);
        userRepository.updatePassword(passwordEncoder.encode(newPwd), user.getId());
        return Msg.PWD_CHANGED;
    }

    public UserResponse getMe(UserDetails userDetails) {
        User user = (User) userDetails;
        return UserResponse.fromEntityToDto(user);
    }

    public UserResponse changeUsernameAndEmail(UserDetails userDetails, String username, String email) {
        // siccome username e email sono unique sul db, devo verificare che eventuali username o email non siano già
        // in uso da un utente diverso da se stesso.
        User user = (User) userDetails;
        if(userRepository.existsByEmailAndIdNot(email, user.getId()))
            throw new ConflictException(Msg.EMAIL_ALREADY_PRESENT);
        if(userRepository.existsByUsernameAndIdNot(username, user.getId()))
            throw new ConflictException(Msg.USERNAME_ALREADY_PRESENT);
        user.setEmail(email);
        user.setUsername(username);
        userRepository.save(user);
        return UserResponse.fromEntityToDto(user);
    }
}
