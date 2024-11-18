package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.entity.enumeration.AuthorityName;
import it.cgmconsulting.myblog.exception.ConflictException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.response.TagResponse;
import it.cgmconsulting.myblog.repository.TagRepository;
import it.cgmconsulting.myblog.utils.Msg;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag create(String id){
        Optional<Tag> tag = tagRepository.findById(id);
        if(tag.isEmpty())
            return tagRepository.save(new Tag(id));
        throw new ConflictException(Msg.TAG_ALREADY_PRESENT);
    }

    public Tag switchVisibility(String id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
        tag.setVisible(!tag.isVisible());
        return tagRepository.save(tag);
    }

    public List<TagResponse> getTags(Optional<Boolean> visible) {
        if(visible.isPresent())
            return tagRepository.getAll(visible.get());
        return tagRepository.getAll();
    }
}
