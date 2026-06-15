package core;

import Database.Product;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GreedyRecommendationEngine {

    // produk akan difilter berdasarkan jenis kulit customer/pembeli
    public static List<Product> filterBySkinType(List<Product> products, String skinType) {
        List<Product> filtered = new ArrayList<>();
        for (Product product : products) {
            if (product.getSkinType().equalsIgnoreCase(skinType) || product.getSkinType().equalsIgnoreCase("Semua Kulit")) {
                filtered.add(product);
            }
        }
        return filtered;
    }

    // proses mengurutkan produk sesuai rating tertinggi
    public static List<Product> sortByHighestRating(List<Product> products) {
        List<Product> sorted = new ArrayList<>(products);
        sorted.sort(Comparator.comparingDouble(Product::getRating).reversed());
        return sorted;
    }

    // Di sini algoristma greedy sudah jalan (sistem ambil 2 produk yg premium)
    public static GreedyResult recommendProducts(List<Product> products, int budget) {
        List<Product> sortedProducts = sortByHighestRating(products);
        List<Product> selectedProducts = new ArrayList<>();

        int remainingBudget = budget;
        int totalPrice = 0;
        double totalRating = 0;
        int maxPremiumItems = 2;
        for (Product product : sortedProducts) {
            if (product.getPrice() <= remainingBudget && selectedProducts.size() < maxPremiumItems) {
                selectedProducts.add(product);
                remainingBudget -= product.getPrice();
                totalPrice += product.getPrice();
                totalRating += product.getRating();
            }
        }
        return new GreedyResult(selectedProducts, totalPrice, totalRating, remainingBudget);
    }
}