package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.Avatar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
public class AvatarResponse {

    private String filename;
    private String filetype;
    private byte[] data;

    public static AvatarResponse fromEntityToResponse(Avatar avatar){
        return new AvatarResponse(
                avatar.getFilename(),
                avatar.getFiletype(),
                avatar.getData()
        );
    }
}
