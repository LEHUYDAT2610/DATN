package vn.fs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.fs.commom.CommomDataService;
import vn.fs.dto.CommentDto;
import vn.fs.entities.Favorite;
import vn.fs.entities.Product;
import vn.fs.entities.User;
import vn.fs.repository.FavoriteRepository;
import vn.fs.repository.ProductRepository;
import vn.fs.service.ICommentService;
import vn.fs.service.impl.IFavoriteSerice;

/**
 * @author DongTHD
 *
 */
@Controller
public class ProductDetailController extends CommomController{
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CommomDataService commomDataService;
	@Autowired
	ICommentService iCommentService;
	@Autowired
	IFavoriteSerice iFavoriteSerice;
	@Autowired
	FavoriteRepository favoriteRepository;
	@GetMapping(value = "productDetail")
	public String productDetail(@RequestParam("id") Long id, Model model, User user) {
		//lay comment

		Product product = productRepository.findById(id).orElse(null);
		model.addAttribute("product", product);
		commomDataService.commonData(model, user);
		listProductByCategory10(model, product.getCategory().getCategoryId());
		List<CommentDto> commentDtoList = iCommentService.getComment(id);
		model.addAttribute("comments", commentDtoList);
		Long countFavorites = iFavoriteSerice.getSumFavofiteProduct(id);
		// Truyền thông tin sản phẩm và số lượt yêu thích vào Model để hiển thị trên view
		model.addAttribute("countFavorites", countFavorites);
		return "web/productDetail";
	}
	
	// Gợi ý top 10 sản phẩm cùng loại
	public void listProductByCategory10(Model model, Long categoryId) {
		List<Product> products = productRepository.listProductByCategory10(categoryId);
		model.addAttribute("productByCategory", products);
	}
}
