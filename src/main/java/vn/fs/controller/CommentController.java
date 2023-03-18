package vn.fs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.fs.dto.CommentDto;
import vn.fs.entities.Comment;
import vn.fs.entities.User;
import vn.fs.repository.UserRepository;
import vn.fs.service.ICommentService;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    ICommentService iCommentService;
    @Autowired
    UserRepository userRepository;
    @PostMapping("/product/{productId}/comment")
    public String addComment(@RequestParam("content") String content,
                             @PathVariable("productId") Long productId,Principal principal,
                             Model model) {
        //lay id user id
            model.addAttribute("user", new User());
            User user = userRepository.findByEmail(principal.getName());
            model.addAttribute("user", user);
            iCommentService.addComment(content,productId,user.getUserId());
            return "redirect:/productDetail?id=" + productId;
    }
    @GetMapping("/productDetail1")
    public String getComment(@RequestParam("productId") Long productId,
                             Model model) {
        List<CommentDto> comment=iCommentService.getComment(productId);
        model.addAttribute("comment",comment);
        return "web/productDetail";
    }
    @PostMapping("/comment/rate/{commentId}")
    public String rateComment(@PathVariable Long commentId) {
        iCommentService.rating(commentId);
        //Long id=iCommentService.getProductId(commentId);
        return "redirect:/"; // Chuyển hướng về trang sản phẩm có chứa comment
    }

}
