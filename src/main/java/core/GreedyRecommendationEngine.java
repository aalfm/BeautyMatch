package core;

import Database.Product;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GreedyRecommendationEngine {

    //tahap 1 disini filter produk brdsrkn skin type si cust
    public static List<Product> filterBySkinType(
            List<Product> products,
            String skinType) {

        List<Product> filtered = new ArrayList<>();

        for (Product product : products) {

            if (product.getSkinType()
                    .equalsIgnoreCase(skinType)) {

                filtered.add(product);
            }
        }

        return filtered;
    }

    //tahap 2 disini sort brdsrkn harga tertinggi dengan rating bagus (>= 4.0)
    public static List<Product> sortByHighestPriceAndGoodRating(
            List<Product> products) {

        List<Product> filteredAndSorted = new ArrayList<>();
        
        // Hanya ambil yang ratingnya bagus (>= 4.0)
        for (Product p : products) {
            if (p.getRating() >= 4.0) {
                filteredAndSorted.add(p);
            }
        }

        // Urutkan dari harga paling mahal (tertinggi) ke paling murah
        filteredAndSorted.sort(
                Comparator.comparingInt(Product::getPrice)
                          .reversed()
        );

        return filteredAndSorted;
    }

    //tahap 3 disni mi bekerja ki greedy selection nya
    public static GreedyResult recommendProducts(
            List<Product> products) {

        List<Product> sortedProducts =
                sortByHighestPriceAndGoodRating(products);

        List<Product> selectedProducts =
                new ArrayList<>();

        int totalPrice = 0;
        double totalRating = 0;

        for (Product product : sortedProducts) {
            if (selectedProducts.size() >= 2) break; // Limit to 2 products

            // TIDAK ADA LAGI PERBANDINGAN DENGAN BUDGET!
            // Langsung ambil 2 produk termahal
            selectedProducts.add(product);
            totalPrice += product.getPrice();
            totalRating += product.getRating();
        }

        return new GreedyResult(
                selectedProducts,
                totalPrice,
                totalRating,
                0 // Sisa budget tidak relevan lagi
        );
    }

    //buat kasi tmpil hsil nya
    public static void printResult(
            GreedyResult result) {

        System.out.println();
        System.out.println("=================================");
        System.out.println("     GREEDY RECOMMENDATION");
        System.out.println("=================================");

        for (Product p :
                result.getSelectedProducts()) {

            System.out.printf(
                    "- %s | Rp%,d | Rating %.1f%n",
                    p.getProductName(),
                    p.getPrice(),
                    p.getRating()
            );
        }

        System.out.println("---------------------------------");

        System.out.printf(
                "Total Harga     : Rp%,d%n",
                result.getTotalPrice()
        );

        System.out.printf(
                "Total Rating    : %.1f%n",
                result.getTotalRating()
        );

        System.out.printf(
                "Sisa Budget     : Rp%,d%n",
                result.getRemainingBudget()
        );

        System.out.println("=================================");
    }
}
