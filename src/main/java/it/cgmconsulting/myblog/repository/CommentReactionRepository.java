package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.CommentReaction;
import it.cgmconsulting.myblog.entity.CommentReactionId;
import it.cgmconsulting.myblog.payload.response.CommentReactionResponse;
import it.cgmconsulting.myblog.payload.response.CommentReactionResponseInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, CommentReactionId> {


    @Query(value="SELECT * FROM vw_count_reaction_by_comment", nativeQuery = true)
    List<CommentReactionResponseInterface> getReactions();

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.CommentReactionResponse(" +
            "vw.vwCountReactionByCommentId.commentId, " +
            "vw.vwCountReactionByCommentId.reactionName, " +
            "vw.tot) " +
            "FROM VwCountReactionByComment vw")
    List<CommentReactionResponse> getReactions_();

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.CommentReactionResponse(" +
            "vw.vwCountReactionByCommentId.commentId, " +
            "vw.vwCountReactionByCommentId.reactionName, " +
            "vw.tot) " +
            "FROM VwCountReactionByComment vw " +
            "WHERE vw.vwCountReactionByCommentId.commentId = :commentId")
    List<CommentReactionResponse> getReactionsByComment(int commentId);

    @Query(value="SELECT username FROM vw_show_users_by_comment_reaction " +
            "WHERE comment_id = :commentId AND reaction_name = :reactionName", nativeQuery = true)
    List<String> getUsernameByCommentReaction(int commentId, String reactionName);
}
