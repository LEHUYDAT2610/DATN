package vn.fs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.fs.dto.CommentDto;
import vn.fs.repository.CommentRespository;
import vn.fs.service.ICommentService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceIml implements ICommentService {
    @Autowired
    private CommentRespository commentRespository;
    @Override
    public void addComment(String content, Long productId, Long userId) {
        commentRespository.addComment(content, productId, userId);
    }

    @Override
    public List<CommentDto> getComment(Long productId) {

        List<Object[]> commentObjects = commentRespository.getCommentsByProductId(productId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Object[] commentObject : commentObjects) {
            BigInteger id=(BigInteger) commentObject[0];
            String name = (String) commentObject[1];
            String content = (String) commentObject[2];
            Date dateRate = (Date) commentObject[3];
            Double rate = (Double) commentObject[4];
            Integer intValue=rate.intValue();
            CommentDto commentDto = new CommentDto(id,name, content, dateRate, intValue);
            commentDtos.add(commentDto);
        }
        return commentDtos;
    }

    @Override
    public void rating(Long commentId) {
        commentRespository.rating(commentId);
    }

    @Override
    public Long getProductId(Long commentId) {
       return commentRespository.getProductId(commentId);
    }
}
