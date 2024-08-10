package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.dto.CommentDTO;
import com.connect.acts.ActsConnectBackend.exception.ResourceNotFoundException;
import com.connect.acts.ActsConnectBackend.model.Comment;
import com.connect.acts.ActsConnectBackend.model.Post;
import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.repository.CommentRepository;
import com.connect.acts.ActsConnectBackend.repository.PostRepository;
import com.connect.acts.ActsConnectBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public CommentDTO createComment(CommentDTO commentDto) {
        // Retrieve the user and post
        User user = userRepository.findById(commentDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + commentDto.getUserId()));
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + commentDto.getPostId()));

        // Retrieve the parent comment if it exists
        Comment parentComment = null;
        if (commentDto.getParentCommentId() != null) {
            parentComment = commentRepository.findById(commentDto.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found with ID: " + commentDto.getParentCommentId()));
        }

        // Create the comment entity from the DTO
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setUser(user);
        comment.setPost(post);
        comment.setParentComment(parentComment);
        comment.setCreatedDate(new Date());
        comment.setModifiedDate(new Date());

        // Save the comment
        Comment savedComment = commentRepository.save(comment);

        // Return the DTO with the ID
        commentDto.setId(savedComment.getId());
        commentDto.setCreatedDate(savedComment.getCreatedDate());
        commentDto.setModifiedDate(savedComment.getModifiedDate());
        return commentDto;
    }

    public CommentDTO updateComment(UUID commentId, CommentDTO commentDto) {
        // Find the comment by ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID: " + commentId));

        // Update the comment entity with new details from DTO
        comment.setText(commentDto.getText());
        comment.setModifiedDate(new Date());

        // Save and return the updated comment
        Comment updatedComment = commentRepository.save(comment);
        commentDto.setCreatedDate(updatedComment.getCreatedDate());
        commentDto.setModifiedDate(updatedComment.getModifiedDate());
        return commentDto;
    }

    public void deleteComment(UUID commentId) {
        // Find the comment by ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID: " + commentId));

        // Delete the comment
        commentRepository.delete(comment);
    }

    public CommentDTO getCommentById(UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID: " + commentId));
        CommentDTO commentDto = new CommentDTO();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setPostId(comment.getPost().getId());
        commentDto.setUserId(comment.getUser().getId());
        if (comment.getParentComment() != null) {
            commentDto.setParentCommentId(comment.getParentComment().getId());
        }
        commentDto.setCreatedDate(comment.getCreatedDate());
        commentDto.setModifiedDate(comment.getModifiedDate());
        return commentDto;
    }
}
