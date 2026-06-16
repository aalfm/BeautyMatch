package core;

import Database.Product;
import java.util.ArrayList;
import java.util.List;

public class HybridRecommendationEngine {

    public static List<Product> recommendProducts(List<Product> products, int budget) {
        List<Product> hasilMix = new ArrayList<>();

        //tahap 1 - menjalankan algoritma Greedy untuk mendapatkan daftar barang termahal yang muat di budget
        GreedyResult hasilGreedy = GreedyRecommendationEngine.recommendProducts(products, budget);

        if (hasilGreedy != null && !hasilGreedy.getSelectedProducts().isEmpty()) {

            //tahap 2 - ambil 1 barang idaman dari hasil Greedy (Sultan item)
            Product barangIdaman = hasilGreedy.getSelectedProducts().get(0);
            hasilMix.add(barangIdaman);

            //tahap 3 - bikin katalog sisa tanpa menyertakan barang idaman tadi (Mencegah duplikasi)
            List<Product> sisaKatalog = new ArrayList<>();
            for (Product p : products) {
                if (p.getId() != barangIdaman.getId()) {
                    sisaKatalog.add(p);
                }
            }

            int sisaBudget = budget - barangIdaman.getPrice();
            if (sisaBudget > 0) {
                //tahap 4 - Lempar sisa katalog ke DP dengan sisa budget untuk mencari barang terjangkau pendamping
                List<Product> tambahanDP = RecommendationEngine.recommendProducts(sisaKatalog, sisaBudget);
                if (tambahanDP != null && !tambahanDP.isEmpty()) {
                    // DP mengembalikan max 2 barang, kita cukup ambil 1 untuk melengkapi Hybrid menjadi total 2 barang
                    hasilMix.add(tambahanDP.get(0));
                }
            }
        }

        return hasilMix;
    }
}
