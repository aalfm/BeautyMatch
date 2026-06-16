package core;

import java.util.ArrayList;
import java.util.List;

import Database.Product;

public class HybridRecommendationEngine {

    public static List<Product> recommendProducts(List<Product> products, int budget) {
        List<Product> hasilMix = new ArrayList<>();
        List<String> kategoriTersimpan = new ArrayList<>();
        int sisaBudget = budget;

        //tahap 1 - menjalankan algoritma Greedy untuk mendapatkan daftar barang termahal yang muat di budget
        GreedyResult hasilGreedy = GreedyRecommendationEngine.recommendProducts(products, budget);

        // tahap 2 - lempar data yang udah aman ke greedy buatan tim lu
        GreedyResult hasilGreedy = GreedyRecommendationEngine.recommendProducts(produkMasukBudget);
        List<Product> listGreedy = new ArrayList<>();
        if (hasilGreedy != null && !hasilGreedy.getSelectedProducts().isEmpty()) {
            listGreedy = hasilGreedy.getSelectedProducts();
        }

        // tahap 3 - antrean 1: ambil jagoan pertama greedy (paling mahal tapi masuk budget)
        if (listGreedy.size() > 0) {
            Product g1 = listGreedy.get(0);
            if (g1.getPrice() <= sisaBudget) {
                hasilMix.add(g1);
                sisaBudget -= g1.getPrice();
                kategoriTersimpan.add(g1.getCategory().toLowerCase());
            }
        }

        // tahap 4 - siapkan data buat dp: buang kategori yang udah diambil greedy dan pastikan masuk sisa budget
        List<Product> sisaKatalogDP = new ArrayList<>();
        for (Product p : produkMasukBudget) {
            if (!kategoriTersimpan.contains(p.getCategory().toLowerCase()) && p.getPrice() <= sisaBudget) {
                sisaKatalogDP.add(p);
            }
        }

        // tahap 5 - lempar sisa data ke dp buatan tim lu
        List<Product> listDP = new ArrayList<>();
        if (!sisaKatalogDP.isEmpty() && sisaBudget > 0) {
            listDP = RecommendationEngine.recommendProducts(sisaKatalogDP);
        }

        // tahap 6 - antrean 2: ambil jagoan pertama dp (kualitas menengah terbaik untuk sisa budget)
        if (listDP.size() > 0) {
            Product d1 = listDP.get(0);
            if (d1.getPrice() <= sisaBudget && !kategoriTersimpan.contains(d1.getCategory().toLowerCase())) {
                hasilMix.add(d1);
                sisaBudget -= d1.getPrice();
                kategoriTersimpan.add(d1.getCategory().toLowerCase());
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

    // tahap 9 - fungsi cetak untuk melihat hasilnya di terminal
    public static void printResult(List<Product> resultList, int initialBudget) {
        System.out.println("\n=======================================================");
        System.out.println("  REKOMENDASI HYBRID (Sesuai Budget)");
        System.out.println("=======================================================");

        int totalPrice = 0;

        System.out.println("\n[BARISAN ATAS] Jagoan Utama:");
        
        for (int i = 0; i < Math.min(2, resultList.size()); i++) {
            Product p = resultList.get(i);
            System.out.printf(" - %-25s | Rp%,d | Rating %.1f%n", p.getProductName(), p.getPrice(), p.getRating());
            totalPrice += p.getPrice();
        }

        System.out.println("\n[BARISAN BAWAH] Produk Pelengkap:");
        boolean adaPelengkap = false;
        
        for (int i = 2; i < resultList.size(); i++) {
            Product p = resultList.get(i);
            System.out.printf(" - %-25s | Rp%,d | Rating %.1f%n", p.getProductName(), p.getPrice(), p.getRating());
            totalPrice += p.getPrice();
            adaPelengkap = true;
        }

        if (!adaPelengkap) {
            System.out.println("   (Tidak ada barang pelengkap tambahan)");
        }

        System.out.println("-------------------------------------------------------");
        System.out.printf("Total Harga     : Rp%,d%n", totalPrice);
        System.out.printf("Sisa Budget     : Rp%,d%n", (initialBudget - totalPrice));
        System.out.println("=======================================================");
    }
}