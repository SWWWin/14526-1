package com.back.domain.post.post.entity;

import com.back.domain.post.post.dto.PostDto;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor
public class Post extends BaseEntity {
    private String title;
    private String content;

    /**
     * mappedBy = "post" : 관계의 주인은 PostComment.post (자식이 FK 보유)
     * fetch = LAZY        : post.getComments() 접근 시점에 쿼리 실행(지연 로딩)
     * cascade = PERSIST   : 부모 저장 시 컬렉션에 포함된 새 댓글도 함께 persist
     * cascade = REMOVE    : 부모 삭제 시 자식 댓글 전체 자동 삭제
     * orphanRemoval = true: 컬렉션에서 자식을 제거하거나 child.post=null 하면 해당 자식 DELETE
     */
    @OneToMany(mappedBy = "post", fetch = LAZY, cascade = {PERSIST, REMOVE})
    private List<PostComment> comments = new ArrayList<>();

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostComment addComment(String content) {
        PostComment postComment = new PostComment(this, content);
        comments.add(postComment);

        return postComment;
    }

    public Optional<PostComment> findCommentById(long id) {
        return comments
                .stream()
                .filter(comment -> comment.getId() == id)
                .findFirst();
    }

    public boolean deleteComment(PostComment postComment) {
        if(postComment == null) return false;

        return comments.remove(postComment);
    }


    public void createComment(Post post, String content) {
        post.addComment(content);
    }

    public boolean deleteComment(Post post, PostComment postComment) {
        return post.deleteComment(postComment);
    }

    public void modify(PostComment postComment, String content) {
        postComment.modify(content);
    }

}
