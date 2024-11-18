package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.payload.response.TagResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TagRepository extends JpaRepository<Tag, String> {


    // JPQL -> Java Persistent Query Language
    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.TagResponse(" +
            "t.id, " +
            "t.visible" +
            ") FROM Tag t " +
            "WHERE t.visible = :visible " +
            "ORDER BY t.id")
    List<TagResponse> getAll(boolean visible);

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.TagResponse(" +
            "t.id, " +
            "t.visible" +
            ") FROM Tag t " +
            "ORDER BY t.id")
    List<TagResponse> getAll(); // SELECT t.id, t.visible FROM tag t ORDER BY t.id
}
