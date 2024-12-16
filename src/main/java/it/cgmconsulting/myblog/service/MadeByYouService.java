package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.MadeByYou;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Report;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.entity.enumeration.ReportStatus;
import it.cgmconsulting.myblog.exception.ConflictException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.response.MadeByYouResponse;
import it.cgmconsulting.myblog.repository.MadeByYouRepository;
import it.cgmconsulting.myblog.repository.PostRepository;
import it.cgmconsulting.myblog.repository.ReportRepository;
import it.cgmconsulting.myblog.utils.Msg;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MadeByYouService {

    private final MadeByYouRepository madeByYouRepository;
    private final ImageService imageService;
    private final PostRepository postRepository;
    private final ReportRepository reportRepository;

    @Transactional(rollbackOn = Exception.class)
    public MadeByYouResponse addArtifact(UserDetails userDetails, int postId, MultipartFile file, String description, long size, int width, int height, String[] extensions, String imagePath) throws IOException {
        if(madeByYouRepository.existsByPostIdAndUserId(postId, ((User)userDetails).getId()))
            throw new ConflictException(Msg.MADE_BY_YOU_EXISTS);

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
                madeByYou.setImage(newFilename);
                madeByYou.setCensored(false);
                madeByYou.setDescription(description);
                madeByYou.setPost(post);
                madeByYou.setUser((User) userDetails);
                madeByYouRepository.save(madeByYou);
                Files.write(p, file.getBytes());
            } catch(Exception e){
                throw new ConflictException(Msg.FILE_ERROR_UPLOAD);
            }
        }
        return MadeByYouResponse.fromEntityToDto(madeByYou, imagePath);

    }

    public List<MadeByYouResponse> getArtifacts(int postId, int pageNumber, int pageSize, String sortBy, String direction, String imagePath) {
        Post post = postRepository.findByIdAndPublishedAtIsNotNullAndPublishedAtLessThanEqual(postId, LocalDate.now())
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        return madeByYouRepository.getArtifacts(postId, pageable, imagePath).getContent();
    }

    @Transactional(rollbackOn = Exception.class)
    public String deleteArtifact(UserDetails userDetails, int id, String imagePath) {
        // Cercare MadeByYou
        MadeByYou madeByYou = madeByYouRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Artifact", "id", id));
        // Verificare che userDetails = autore del MadeByYou
        if(!madeByYou.getUser().equals((User) userDetails))
            throw new ConflictException(Msg.MADE_BY_YOU_UNAUTHORIZED_ACCESS);
        // Il MadeByou non deve essere censurato
        if(madeByYou.isCensored())
            throw new ConflictException(Msg.MADE_BY_YOU_CENSORED);
        //    e può trovarsi tra le segnalazioni a patto che lo status sia CLOSED_WITHOUT_BAN
        Optional<Report> report = reportRepository.findByMadeByYou(madeByYou);
        if(report.isPresent() && !report.get().getStatus().equals(ReportStatus.CLOSED_WITHOUT_BAN))
            throw new ConflictException(Msg.MADE_BY_YOU_UNDER_CONTROL);
        report.ifPresent(reportRepository::delete);

        try {
            // Cancellare dal db
            madeByYouRepository.delete(madeByYou);
            // Cancellazione fisica del file
            Path p = Paths.get(imagePath + madeByYou.getImage());
            Files.delete(p);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ConflictException(Msg.FILE_ERROR_DELETE);
        }

        return Msg.MADE_BY_YOU_DELETE_OK;
    }
}
