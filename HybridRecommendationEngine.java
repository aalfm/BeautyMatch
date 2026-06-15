import Database.Product;
import java.util.ArrayList;
import java.util.List;

public class HybridRecommendationEngine {

    public static List<Product> recommendProducts(List<Product> products, int budget, String skinType) {
        List<Product> hasilMix = new ArrayList<>();

        //tahap 1 - menyaring data katalog berdasarkan tipe kulit dan meminjam fungsi dari file Greedy
        List<Product> filteredCatalog = GreedyRecommendationEngine.filterBySkinType(products, skinType);

        //tahap 2 - menjalankan algoritma Greedy untuk mendapatkan daftar barang berdasarkan rating tertinggi
        GreedyResult hasilGreedy = GreedyRecommendationEngine.recommendProducts(filteredCatalog, budget);

        //tahap 3 - cek apakah Greedy berhasil menemukan setidaknya 1 barang yang pas dengan budget
        if (hasilGreedy != null && !hasilGreedy.getSelectedProducts().isEmpty()) {

            //tahap 4 - ambil 1 barang idaman dari hasil Greedy (Index 0 = Rating tertinggi yang muat di budget)
            Product barangIdaman = hasilGreedy.getSelectedProducts().get(0);
            hasilMix.add(barangIdaman);

            //tahap 5 - bikin katalog sisa tanpa menyertakan barang idaman tadi (Mencegah duplikasi)
            List<Product> sisaKatalog = new ArrayList<>();
            for (Product p : filteredCatalog) {
                //pengecekan ID untuk memastikan barang yang sama tidak masuk lagi ke DP
                if (p.getId() != barangIdaman.getId()) {
                    sisaKatalog.add(p);
                }
            }

            //tahap 6 - Hitung sisa budget setelah membeli barang idaman
            int sisaBudget = budget - barangIdaman.getPrice();

            if (sisaBudget > 0) {
                //tahap 7 - Lempar sisa katalog dan sisa uang ke DP untuk optimasi kapasitas
                List<Product> tambahanDP = RecommendationEngine.recommendProducts(sisaKatalog, sisaBudget);
                hasilMix.addAll(tambahanDP);
            }
        }

        return hasilMix;
    }

    //tambahan untuk mencetak hasil Hybrid agar rapi
    public static void printResult(List<Product> resultList, int initialBudget) {
        System.out.println();
        System.out.println("=================================");
        System.out.println("     HYBRID RECOMMENDATION       ");
        System.out.println("=================================");

        int totalPrice = 0;
        double totalRating = 0;

        for (Product p : resultList) {
            System.out.printf("- %s | Rp%,d | Rating %.1f%n", 
                    p.getProductName(), p.getPrice(), p.getRating());
            totalPrice += p.getPrice();
            totalRating += p.getRating();
        }

        System.out.println("---------------------------------");
        System.out.printf("Total Harga     : Rp%,d%n", totalPrice);
        System.out.printf("Total Rating    : %.1f%n", totalRating);
        System.out.printf("Sisa Budget     : Rp%,d%n", (initialBudget - totalPrice));
        System.out.println("=================================");
    }
}