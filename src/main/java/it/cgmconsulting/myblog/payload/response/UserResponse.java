package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.Avatar;
import it.cgmconsulting.myblog.entity.User;
import lombok.*;

import java.time.LocalDate;

@Data @AllArgsConstructor @NoArgsConstructor
public class UserResponse {

    private int id;
    private String username;
    private String email;
    private Avatar avatar;
    private LocalDate createdAt;

    public static UserResponse fromEntityToDTO(User user){
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatar(),
                user.getCreatedAt().toLocalDate()
        );
    }
}
