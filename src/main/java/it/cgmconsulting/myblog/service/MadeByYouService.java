package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.MadeByYou;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.ConflictException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.response.MadeByYouResponse;
import it.cgmconsulting.myblog.repository.MadeByYouRepository;
import it.cgmconsulting.myblog.repository.PostRepository;
import it.cgmconsulting.myblog.utils.Msg;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MadeByYouService {

    private final MadeByYouRepository madeByYouRepository;
    private final ImageService imageService;
    private final PostRepository postRepository;

    public MadeByYouResponse addArtifact(UserDetails userDetails, int postId, MultipartFile file, String description, long size, int width, int height, String[] extensions, String imagePath) throws IOException {
        Post post = postRepository.findByIdAndPublishedAtIsNotNullAndPublishedAtLessThanEqual(postId, LocalDate.now())
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));

        MadeByYou madeByYou = new MadeByYou();

        if(imageService.checkSize(file,size) &&
                imageService.checkDimension(file, width, height) &&
                imageService.checkExtension(file, extensions)) {
            // genero un nuovo nome per l'immagine da caricare
            String filename = file.getOriginalFilename();
            String ext = filename != null ? filename.substring(filename.lastIndexOf(".") + 1) : null;
            String newFilename = postId+"_"+ UUID.randomUUID().toString()+"."+ext;
            Path p = Paths.get(imagePath+newFilename);
            try{
                Files.write(p, file.getBytes());
                madeByYou.setImage(newFilename);
                madeByYou.setCensored(false);
                madeByYou.setDescription(description);
                madeByYou.setPost(post);
                madeByYou.setUser((User) userDetails);
                madeByYouRepository.save(madeByYou);
            } catch(Exception e){
                throw new ConflictException(Msg.FILE_ERROR_UPLOAD);
            }
        }
        return MadeByYouResponse.fromEntityToDto(madeByYou);

    }
}
