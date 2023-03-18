package vn.fs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fs.entities.Comment;

import java.util.List;

@Repository
public interface CommentRespository extends JpaRepository<Comment,Long> {
    @Procedure("proc_addComment")
    void addComment(String content, Long productId, Long userId);
    @Query(value = "SELECT c.id,u.name, c.content, c.rate_date, c.rating "
            + "FROM user u "
            + "JOIN comments c ON u.user_id = c.user_id "
            + "WHERE c.product_id = :productId",
            nativeQuery = true)
    List<Object[]> getCommentsByProductId(@Param("productId") Long productId);
    @Procedure("proc_updaterate")
    void rating(Long comentId);

    @Procedure(".proc_getproductid")
    Long getProductId(Long comentId);
}
