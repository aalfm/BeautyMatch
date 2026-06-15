package core;

import java.util.ArrayList;
import java.util.List;

import Database.Product;

public class HybridRecommendationEngine {

    public static List<Product> recommendProducts(List<Product> products, int budget, String skinType) {
        List<Product> hasilMix = new ArrayList<>();
        List<String> kategoriTersimpan = new ArrayList<>();
        int sisaBudget = budget;

        // tahap 1 - saring awal: buang semua barang yang dari awal udah lebih mahal dari budget pelanggan
        List<Product> produkMasukBudget = new ArrayList<>();
        for (Product p : products) {
            if (p.getPrice() <= sisaBudget) {
                produkMasukBudget.add(p);
            }
        }

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

        // tahap 7 - antrean 3: masukkan produk kedua greedy ke barisan bawah (jika masih muat di dompet)
        if (listGreedy.size() > 1) {
            Product g2 = listGreedy.get(1);
            if (g2.getPrice() <= sisaBudget && !kategoriTersimpan.contains(g2.getCategory().toLowerCase())) {
                hasilMix.add(g2);
                sisaBudget -= g2.getPrice();
                kategoriTersimpan.add(g2.getCategory().toLowerCase());
            }
        }

        // tahap 8 - antrean 4: masukkan sisa produk dari dp ke barisan bawah (jika masih muat)
        for (int i = 1; i < listDP.size(); i++) {
            Product sisaD = listDP.get(i);
            if (sisaD.getPrice() <= sisaBudget && !kategoriTersimpan.contains(sisaD.getCategory().toLowerCase())) {
                hasilMix.add(sisaD);
                sisaBudget -= sisaD.getPrice();
                kategoriTersimpan.add(sisaD.getCategory().toLowerCase());
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