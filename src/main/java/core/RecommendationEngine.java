package core;

import Database.Product;
import java.util.ArrayList;
import java.util.List;

public class RecommendationEngine {
    public static List<Product> recommendProducts(List<Product> products, int maxBudget) {
        if (products == null || products.isEmpty() || maxBudget <= 0) {
            return new ArrayList<>();
        }

        int n = products.size();
        
        // Menggunakan batas budget dari user
        int W = maxBudget / 1000;
        
        // dp[i][w][k] stores the maximum rating for the first i products with weight limit w and EXACTLY/AT MOST k items
        double[][][] dp = new double[n + 1][W + 1][3];
        boolean[][][] keep = new boolean[n + 1][W + 1][3];

        for (int i = 1; i <= n; i++) {
            Product currentProduct = products.get(i - 1);
            int weight = currentProduct.getPrice() / 1000;
            double value = currentProduct.getRating();

            for (int w = 0; w <= W; w++) {
                for (int k = 1; k <= 2; k++) { // Constraint: Maximum 2 items
                    if (weight <= w) {
                        double valueIfIncluded = dp[i - 1][w - weight][k - 1] + value;
                        double valueIfExcluded = dp[i - 1][w][k];

                        if (valueIfIncluded > valueIfExcluded) {
                            dp[i][w][k] = valueIfIncluded;
                            keep[i][w][k] = true;
                        } else {
                            dp[i][w][k] = valueIfExcluded;
                            keep[i][w][k] = false;
                        }
                    } else {
                        dp[i][w][k] = dp[i - 1][w][k];
                        keep[i][w][k] = false;
                    }
                }
            }
        }

        List<Product> recommended = new ArrayList<>();
        int w = W;
        int k = 2;
        
        // Determine if picking 1 item is somehow better than 2 items (unlikely, but safe)
        if (dp[n][W][1] > dp[n][W][2]) {
            k = 1;
        }

        for (int i = n; i > 0 && k > 0; i--) {
            if (keep[i][w][k]) {
                Product chosenProduct = products.get(i - 1);
                recommended.add(chosenProduct);
                w -= chosenProduct.getPrice() / 1000;
                k--;
            }
        }

        return recommended;
    }
}
