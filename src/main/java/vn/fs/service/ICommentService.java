package vn.fs.service;

import vn.fs.dto.CommentDto;

import java.util.List;

public interface ICommentService {
    public void addComment(String content, Long productId, Long userId);
    public List<CommentDto> getComment(Long productId);
    public void rating(Long commentId);
    public Long getProductId(Long commentId);
}
