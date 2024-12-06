package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.MadeByYou;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MadeByYouResponse {

    private int id;
    private String image;
    private String description;
    private String username;

    public static MadeByYouResponse fromEntityToDto(MadeByYou madeByYou){
        return MadeByYouResponse.builder()
                .id(madeByYou.getId())
                .image(madeByYou.getImage())
                .description(madeByYou.getDescription())
                .username(madeByYou.getUser().getUsername())
                .build();
    }

}
