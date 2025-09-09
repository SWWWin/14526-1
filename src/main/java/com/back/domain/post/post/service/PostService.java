package com.back.domain.post.post.service;

import com.back.domain.post.post.dto.PostDto;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.repository.PostRepository;
import com.back.domain.post.postComment.entity.PostComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post create(String title, String content) {
        return postRepository.save(new Post(title, content));
    }

    public long count() {
        return postRepository.count();
    }

    public void update(Post post, String title, String content) {
        post.modify(title, content);
    }

    public List<Post> getList() {
        return postRepository.findAll();
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
    }

    public Post findByTitle(String title) {
        return postRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
    }

    public void createComment(Post post, String content) {
        post.addComment(content);
    }

    public boolean deleteComment(Post post, PostComment postComment) {
        return post.deleteComment(postComment);
    }

    public void modifyComment(PostComment postComment, String content) {
        postComment.modify(content);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public Optional<Post> findLatest() {
        return postRepository.findFirstByOrderByIdDesc();
    }
}
