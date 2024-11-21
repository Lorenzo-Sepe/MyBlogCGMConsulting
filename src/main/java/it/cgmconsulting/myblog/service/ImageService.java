package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Avatar;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.BadRequestException;
import it.cgmconsulting.myblog.payload.response.AvatarResponse;
import it.cgmconsulting.myblog.payload.response.UserResponse;
import it.cgmconsulting.myblog.repository.AvatarRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.utils.Msg;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;
    public AvatarResponse addAvatar(UserDetails userDetails, MultipartFile file,
                                    long size, int width, int height, String[] extensions) throws IOException {

        if(checkSize(file,size) && checkDimension(file, width, height) && checkExtension(file, extensions)) {

            User user = (User) userDetails;
            Avatar avatar = Avatar.builder()
                    .id(user.getId())
                    .user(user)
                    .filename(file.getOriginalFilename())
                    .filetype(file.getContentType())
                    .data(file.getBytes())
                    .build();
            user.setAvatar(avatar);
            userRepository.save(user);
            return AvatarResponse.fromEntityToResponse(avatar);
        }
        return null;
    }

    private boolean checkSize(MultipartFile file, long size){
        if(file.getSize() > size || file.isEmpty())
            throw new BadRequestException(Msg.FILE_TOO_LARGE);
        return true;
    }

    private BufferedImage fromMultipartFileToBufferedImage(MultipartFile file){
        BufferedImage bf = null;
        try{
            bf = ImageIO.read(file.getInputStream());
            return bf;
        } catch (IOException ex){
            return null;
        }
    }

    private boolean checkDimension(MultipartFile file, int width, int height){
        BufferedImage bf = fromMultipartFileToBufferedImage(file);
        if(bf == null)
            throw new BadRequestException(Msg.FILE_NOT_VALID_IMAGE);
        if(bf.getWidth() <= width && bf.getHeight() <= height)
            return true;
        else
            throw new BadRequestException(Msg.FILE_INVALID_DIMENSIONS);
    }



    private boolean checkExtension(MultipartFile file, String[] extensions) throws IOException {
        if(file != null){
            if(!Objects.requireNonNull(file.getOriginalFilename()).contains("."))
                throw new BadRequestException(Msg.FILE_EXTENSION_MISSING);

            Tika tika = new Tika();
            String mime = tika.detect(file.getBytes());
            if(Arrays.stream(extensions).anyMatch(mime::equalsIgnoreCase)) {
                if(Objects.equals(file.getContentType(), mime))
                    return true;
            }
        }
        throw new BadRequestException(Msg.FILE_EXTENSION_NOT_ALLOWED);
    }



    public UserResponse removeAvatar(UserDetails userDetails) {
        User user = (User) userDetails;
        avatarRepository.deleteAvatar(user.getId());
        user.setAvatar(null);
        return UserResponse.fromEntityToDto(user);
    }
}
