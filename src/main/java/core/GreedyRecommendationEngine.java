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

    public static List<Product> sortByHighestPriceAndGoodRating(List<Product> products) {

        List<Product> filteredAndSorted = new ArrayList<>();

        for (Product p : products) {
            if (p.getRating() >= 4.0) {
                filteredAndSorted.add(p);
            }
        }

        filteredAndSorted.sort(
                Comparator.comparingInt(Product::getPrice)
                        .reversed()
        );

        return filteredAndSorted;
    }

    // proses memilih 2 produk dengan harga tertinggi dan rating bagus yang sesuai budget
    public static GreedyResult recommendProducts(List<Product> products, int budget) {

        List<Product> sortedProducts =
                sortByHighestPriceAndGoodRating(products);

        List<Product> selectedProducts = new ArrayList<>();

        int totalPrice = 0;
        double totalRating = 0;
        int remainingBudget = budget;

        for (Product product : sortedProducts) {
            if (selectedProducts.size() >= 2) break;

            if (product.getPrice() <= remainingBudget) {
                selectedProducts.add(product);
                totalPrice += product.getPrice();
                totalRating += product.getRating();
                remainingBudget -= product.getPrice();
            }
        }

        return new GreedyResult(
                selectedProducts,
                totalPrice,
                totalRating,
                remainingBudget
        );
    }
}