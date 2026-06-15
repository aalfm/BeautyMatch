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

    //tahap 2 disini sort brdsrkn rating tertinggi
    public static List<Product> sortByHighestRating(
            List<Product> products) {

        List<Product> sorted = new ArrayList<>(products);

        sorted.sort(
                Comparator.comparingDouble(Product::getRating)
                          .reversed()
        );

        return sorted;
    }

    //tahap 3 disni mi bekerja ki greedy selection nya
    public static GreedyResult recommendProducts(
            List<Product> products,
            int budget) {

        List<Product> sortedProducts =
                sortByHighestRating(products);

        List<Product> selectedProducts =
                new ArrayList<>();

        int remainingBudget = budget;
        int totalPrice = 0;
        double totalRating = 0;

        for (Product product : sortedProducts) {

            if (product.getPrice() <= remainingBudget) {

                selectedProducts.add(product);

                remainingBudget -= product.getPrice();

                totalPrice += product.getPrice();

                totalRating += product.getRating();
            }
        }

        return new GreedyResult(
                selectedProducts,
                totalPrice,
                totalRating,
                remainingBudget
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
