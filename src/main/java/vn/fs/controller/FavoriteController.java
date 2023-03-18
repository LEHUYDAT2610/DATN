package vn.fs.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.fs.commom.CommomDataService;
import vn.fs.entities.Favorite;
import vn.fs.entities.Product;
import vn.fs.entities.User;
import vn.fs.repository.FavoriteRepository;
import vn.fs.repository.ProductRepository;
import vn.fs.service.impl.IFavoriteSerice;

/**
 * @author DongTHD
 *
 */
@Controller
public class FavoriteController extends CommomController {

	@Autowired
	FavoriteRepository favoriteRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CommomDataService commomDataService;
	@Autowired
	IFavoriteSerice iFavoriteSerice;
	@GetMapping(value = "/favorite")
	public String favorite(Model model, User user) {
		List<Favorite> favorites = favoriteRepository.selectAllSaves(user.getUserId());
		commomDataService.commonData(model, user);
		model.addAttribute("favorites", favorites);
		return "web/favorite";
	}

	@GetMapping(value = "/doFavorite")
	public String doFavorite(Model model, Favorite favorite, User user, @RequestParam("id") Long id) {
		Product product = productRepository.findById(id).orElse(null);
		favorite.setProduct(product);
		favorite.setUser(user);
		product.setFavorite(true);
		favoriteRepository.save(favorite);
		commomDataService.commonData(model, user);
		return "redirect:/products";
	}
	@PostMapping("/product/favorite")
	public String addFavorite(@RequestParam("productId") Long productId, Model model, User user, Principal principal, RedirectAttributes redirectAttributes) {
		if (principal == null) {
			redirectAttributes.addFlashAttribute("message", "Bạn chưa đăng nhập");
			return "web/login";
		} else {
			Product product = productRepository.findById(productId).orElse(null);
			Favorite favorite = new Favorite();
			favorite.setProduct(product);
			favorite.setUser(user);
			favoriteRepository.save(favorite);
			product.setFavorite(true);
			productRepository.save(product);
			return "redirect:/productDetail?id=" + productId;
		}
	}
	@GetMapping(value = "/doUnFavorite")
	public String doUnFavorite(Model model, Product product, User user, @RequestParam("id") Long id) {
		Favorite favorite = favoriteRepository.selectSaves(id, user.getUserId());
		product = productRepository.findById(id).orElse(null);
		product.setFavorite(false);
		favoriteRepository.delete(favorite);
		commomDataService.commonData(model, user);
		return "redirect:/favorite";
	}
	/*@GetMapping("/productDetail")
	public String getProductDetail(@RequestParam("id") Long id, Model model) {
		// Lấy số lượt yêu thích của sản phẩm
		Long countFavorites = iFavoriteSerice.getSumFavofiteProduct(id);
		// Truyền thông tin sản phẩm và số lượt yêu thích vào Model để hiển thị trên view
		model.addAttribute("countFavorites", countFavorites);
		return "web/productDetail";
	}*/
}
