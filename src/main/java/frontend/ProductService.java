package frontend;

import Database.Product;
import java.util.ArrayList;
import java.util.List;
import Database.ProductDAO;
import core.GreedyRecommendationEngine;
import core.GreedyResult;

public class ProductService {
    private static ProductDAO dao = new ProductDAO();

    public static List<ProductRow> getAllProducts() {
        return toRows(dao.getAllProducts());
    }

    public static List<ProductRow> getByCategory(String category) {
        return toRows(dao.getProductsByCategory(category));
    }

    public static GreedyResult recommendGreedy(String skinType, List<String> categories) {
        List<Product> candidates = dao.getProductsForRecommendation(skinType, categories);
        return GreedyRecommendationEngine.recommendProducts(candidates);
    }

    public static List<Product> recommendDP(String skinType, List<String> categories) {
        List<Product> candidates = dao.getProductsForRecommendation(skinType, categories);
        return core.RecommendationEngine.recommendProducts(candidates);
    }

    public static void addProduct(String brand, String name, String category,
                                  int price, double rating, String skinType) {
        Product newProduct = new Product(0, brand, name, category, price, rating, skinType);
        dao.addProduct(newProduct);
    }

    public static boolean deleteProduct(int id) {
        dao.deleteProduct(id);
        return true;
    }

    private static List<ProductRow> toRows(List<Product> products) {
        List<ProductRow> rows = new ArrayList<>();
        if (products == null) return rows;
        for (Product p : products) {
            rows.add(new ProductRow(
                p.getId(), p.getBrand(), p.getProductName(),
                p.getCategory(), p.getPrice(), p.getRating(), p.getSkinType()
            ));
        }
        return rows;
    }
}
