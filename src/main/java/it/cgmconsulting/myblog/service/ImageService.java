package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Avatar;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.BadRequestException;
import it.cgmconsulting.myblog.repository.AvatarRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.utils.Msg;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;


    public Avatar addAvatar(UserDetails userDetails, MultipartFile file,
                            long size, int width, int height, String[] extensions)
            throws IOException {
        if (checkSize(file, size) && checkDimensions(file, width, height) && checkExtension(file, extensions)) {
            User user = (User) userDetails;
            Avatar avatar = Avatar.builder()
                    .id(user.getId())
                    .filename(file.getOriginalFilename())
                    .filetype(file.getContentType())
                    .data(file.getBytes())
                    .build();
            user.setAvatar(avatar);
            userRepository.save(user);
            return avatar;
        }

        return null;
    }

    private boolean checkSize(MultipartFile file, long size) {
        if (file.getSize() > size || file.isEmpty())
            throw new BadRequestException(Msg.FILE_TOO_LARGE);
        return true;
    }

    private BufferedImage fromMultipartFileToBufferedImage(MultipartFile file) {
        BufferedImage bf;
        try {
            bf = ImageIO.read(file.getInputStream());
            return bf;
        } catch (IOException e) {
            return null;
        }
    }

    private boolean checkDimensions(MultipartFile file, int width, int height){
        BufferedImage bf = fromMultipartFileToBufferedImage(file);
        if (bf == null) {
            throw new BadRequestException(Msg.FILE_NOT_VALID_IMAGE);
        }
        if (bf.getWidth() <= width && bf.getHeight() <= height)
            return true;
        else
            throw new BadRequestException(Msg.IMAGE_DIMENSIONS_NOT_VALID);
    }

    private boolean checkExtension(MultipartFile file, String[] extensions) {
        String filename = file.getOriginalFilename();
        assert filename != null;
        String ext = filename.substring(filename.lastIndexOf(".") + 1);
        return Arrays.stream(extensions).anyMatch(ext::equalsIgnoreCase);
    }
}