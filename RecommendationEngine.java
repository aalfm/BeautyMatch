import Database.Product;
import java.util.ArrayList;
import java.util.List;

public class RecommendationEngine {
    public static List<Product> recommendProducts(List<Product> products, int maxBudget) {
        if (products == null || products.isEmpty() || maxBudget <= 0) {
            return new ArrayList<>();
        }

        int n = products.size();
        
        // Optimize knapsack array size by dividing budget and prices by 1000.
        // Assuming all prices in IDR are multiples of 1000.
        int W = maxBudget / 1000;
        
        // dp[i][w] stores the maximum rating for the first i products with weight limit w
        double[][] dp = new double[n + 1][W + 1];
        boolean[][] keep = new boolean[n + 1][W + 1];

        for (int i = 1; i <= n; i++) {
            Product currentProduct = products.get(i - 1);
            int weight = currentProduct.getPrice() / 1000;
            double value = currentProduct.getRating();

            for (int w = 0; w <= W; w++) {
                if (weight <= w) {
                    double valueIfIncluded = dp[i - 1][w - weight] + value;
                    double valueIfExcluded = dp[i - 1][w];

                    if (valueIfIncluded > valueIfExcluded) {
                        dp[i][w] = valueIfIncluded;
                        keep[i][w] = true;
                    } else {
                        dp[i][w] = valueIfExcluded;
                        keep[i][w] = false;
                    }
                } else {
                    dp[i][w] = dp[i - 1][w];
                    keep[i][w] = false;
                }
            }
        }

        List<Product> recommended = new ArrayList<>();
        int w = W;
        for (int i = n; i > 0; i--) {
            if (keep[i][w]) {
                Product chosenProduct = products.get(i - 1);
                recommended.add(chosenProduct);
                w -= chosenProduct.getPrice() / 1000;
            }
        }

        return recommended;
    }
}
