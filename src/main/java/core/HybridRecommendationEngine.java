package core;

import java.util.ArrayList;
import java.util.List;

import Database.Product;

public class HybridRecommendationEngine {

    public static HybridResult recommendProducts(List<Product> products, int budget) {
        List<Product> greedyPicks = new ArrayList<>();
        List<Product> dpPicks = new ArrayList<>();
        List<Product> otherPicks = new ArrayList<>();

        List<Integer> usedIds = new ArrayList<>();
        List<String> usedCategories = new ArrayList<>();

        // Tahap 1: Jalankan Greedy murni dengan full budget
        GreedyResult hasilGreedy = GreedyRecommendationEngine.recommendProducts(products, budget);
        if (hasilGreedy != null && !hasilGreedy.getSelectedProducts().isEmpty()) {
            for (Product p : hasilGreedy.getSelectedProducts()) {
                greedyPicks.add(p);
                usedIds.add(p.getId());
                usedCategories.add(p.getCategory().toLowerCase());
            }
        }

        // Tahap 2: Jalankan DP murni dengan full budget
        List<Product> hasilDP = RecommendationEngine.recommendProducts(products, budget);
        if (hasilDP != null && !hasilDP.isEmpty()) {
            for (Product p : hasilDP) {
                // Hindari produk duplikat yang sudah masuk di baris Greedy
                if (!usedIds.contains(p.getId())) {
                    dpPicks.add(p);
                    usedIds.add(p.getId());
                    usedCategories.add(p.getCategory().toLowerCase());
                }
            }
        }

        // Tahap 3: Cari sisa produk yang belum terpilih oleh Greedy dan DP
        for (Product p : products) {
            if (!usedIds.contains(p.getId())) {
                otherPicks.add(p);
                usedIds.add(p.getId());
            }
        }

        return new HybridResult(greedyPicks, dpPicks, otherPicks);
    }
}