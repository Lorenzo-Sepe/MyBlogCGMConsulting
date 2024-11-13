package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.payload.response.TagResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, String> {

    @Query(value = "SELECT new it.cgmconsulting.myblog.payload.response.TagResponse(t.id, t.visible) FROM Tag t ORDER BY t.id")
    List<TagResponse> getAll();
}
