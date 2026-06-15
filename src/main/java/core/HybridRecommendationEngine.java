package core;

import Database.Product;
import java.util.ArrayList;
import java.util.List;

public class HybridRecommendationEngine {

    public static List<Product> recommendProducts(List<Product> products) {
        List<Product> hasilMix = new ArrayList<>();

        //tahap 1 - menjalankan algoritma Greedy untuk mendapatkan daftar barang termahal
        GreedyResult hasilGreedy = GreedyRecommendationEngine.recommendProducts(products);

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

            //tahap 4 - Lempar sisa katalog ke DP untuk mencari barang terjangkau pendamping
            List<Product> tambahanDP = RecommendationEngine.recommendProducts(sisaKatalog);
            if (tambahanDP != null && !tambahanDP.isEmpty()) {
                // DP mengembalikan 2 barang, kita cukup ambil 1 untuk melengkapi Hybrid menjadi total 2 barang
                hasilMix.add(tambahanDP.get(0));
            }
        }

        return hasilMix;
    }
}
