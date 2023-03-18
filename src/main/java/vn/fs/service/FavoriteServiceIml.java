package vn.fs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.fs.entities.Product;
import vn.fs.repository.FavoriteRepository;
import vn.fs.repository.ProductRepository;
import vn.fs.service.impl.IFavoriteSerice;

import java.util.Optional;

@Service
public class FavoriteServiceIml implements IFavoriteSerice {
    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    ProductRepository productRepository;
    @Override
    public long getSumFavofiteProduct(Long productId) {
        Long sum = favoriteRepository.countFavoritesByProductId(productId);
        return sum;
    }
}
