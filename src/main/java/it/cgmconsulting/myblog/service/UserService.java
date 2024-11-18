package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.ConflictException;
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
}
