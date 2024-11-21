package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.Avatar;
import it.cgmconsulting.myblog.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class UserResponse {

    private int id;
    private String username;
    private String email;
    private AvatarResponse avatar;
    private LocalDate registeredAt;

    public static UserResponse fromEntityToDto(User user){
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                checkAvatar(user),
                user.getCreatedAt().toLocalDate()
        );
    }

    private static AvatarResponse checkAvatar(User user){
        if(user.getAvatar() != null)
            return AvatarResponse.fromEntityToResponse(user.getAvatar());
        return null;

    }

}
