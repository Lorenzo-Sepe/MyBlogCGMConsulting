package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.entity.enumeration.ReactionName;
import it.cgmconsulting.myblog.exception.ConflictException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.response.CommentReactionResponse;
import it.cgmconsulting.myblog.payload.response.CommentReactionResponseInterface;
import it.cgmconsulting.myblog.repository.CommentReactionRepository;
import it.cgmconsulting.myblog.repository.CommentRepository;
import it.cgmconsulting.myblog.repository.ReactionRepository;
import it.cgmconsulting.myblog.utils.Msg;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentReactionService {

    private final CommentReactionRepository commentReactionRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;

    public List<CommentReactionResponseInterface> getReactions(){
        return commentReactionRepository.getReactions();
    }

    public List<CommentReactionResponse> getReactions_(){
        List<CommentReactionResponse> list  = commentReactionRepository.getReactions_();
        return list;
    }


    public List<CommentReactionResponse> addReaction(UserDetails userDetails, String reactionName, int commentId) {
        Comment comment = commentRepository.getValidComment(commentId);
        if(comment == null)
            throw new ResourceNotFoundException("Comment", "id", commentId);

        User user = (User) userDetails;
        if(comment.getUser().equals(user))
            throw new ConflictException(Msg.REACTION_SAME_COMMENT_AUTHOR);

        Reaction reaction = reactionRepository.findByReactionName(ReactionName.valueOf(reactionName.toUpperCase()))
                .orElseThrow(()-> new ResourceNotFoundException("Reaction", "name", reactionName));

        CommentReaction commentReaction = new CommentReaction(
                new CommentReactionId(comment, user),
                reaction
        );
        commentReactionRepository.save(commentReaction);
        return commentReactionRepository.getReactionsByComment(commentId);
    }

    public List<String> getUsersByReaction(int commentId, String reactionName) {
        return commentReactionRepository.getUsernameByCommentReaction(commentId, reactionName);
    }
}
