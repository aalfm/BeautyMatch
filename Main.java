import Database.Product;
import Database.ProductDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static ProductDAO dao = new ProductDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        dao.initializeDatabase();

        boolean running = true;
        while (running) {
            System.out.println("\n=== BeautyMatch: Sistem Rekomendasi Skincare & Kosmetik ===");
            System.out.println("1. Tampilkan Semua Produk");
            System.out.println("2. Tambah Produk Baru");
            System.out.println("3. Hapus Produk");
            System.out.println("4. Cari Produk Berdasarkan Kategori");
            System.out.println("5. Dapatkan Rekomendasi Produk (0/1 Knapsack)");
            System.out.println("6. Keluar");
            System.out.print("Pilih menu (1-6): ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Harap masukkan angka.");
                continue;
            }

            switch (choice) {
                case 1:
                    displayAllProducts();
                    break;
                case 2:
                    addNewProduct();
                    break;
                case 3:
                    deleteProduct();
                    break;
                case 4:
                    searchByCategory();
                    break;
                case 5:
                    getRecommendation();
                    break;
                case 6:
                    running = false;
                    System.out.println("Terima kasih telah menggunakan BeautyMatch!");
                    break;
                default:
                    System.out.println("Pilihan tidak ada. Silakan coba lagi.");
            }
        }
    }

    private static void displayAllProducts() {
        List<Product> products = dao.getAllProducts();
        printProductsTable(products);
    }

    private static void addNewProduct() {
        System.out.println("\n--- Tambah Produk Baru ---");
        try {
            System.out.print("Brand: ");
            String brand = InputUtils.formatCapitalize(scanner.nextLine());
            
            System.out.print("Nama Produk: ");
            String name = InputUtils.formatCapitalize(scanner.nextLine());
            
            System.out.print("Kategori (mis. Blush, Lip, Cushion, Mascara): ");
            String category = InputUtils.formatCapitalize(scanner.nextLine());
            
            System.out.print("Harga (Rp): ");
            int price = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Rating (1.0 - 5.0): ");
            double rating = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Jenis Kulit (mis. Semua Kulit, Normal, Kering, Berminyak): ");
            String skinType = InputUtils.formatCapitalize(scanner.nextLine());
            
            Product newProduct = new Product(0, brand, name, category, price, rating, skinType);
            dao.addProduct(newProduct);
        } catch (NumberFormatException e) {
            System.out.println("Input harga atau rating tidak valid. Penambahan produk dibatalkan.");
        }
    }

    private static void deleteProduct() {
        System.out.println("\n--- Hapus Produk ---");
        System.out.print("Masukkan ID produk yang akan dihapus: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            dao.deleteProduct(id);
        } catch (NumberFormatException e) {
            System.out.println("ID tidak valid.");
        }
    }

    private static void searchByCategory() {
        System.out.println("\n--- Cari Produk Berdasarkan Kategori ---");
        System.out.print("Masukkan nama kategori (mis. Lip, Blush): ");
        String category = InputUtils.formatCapitalize(scanner.nextLine());
        
        List<Product> products = dao.getProductsByCategory(category);
        if (products.isEmpty()) {
            System.out.println("Tidak ada produk dengan kategori: " + category);
        } else {
            printProductsTable(products);
        }
    }

    private static void getRecommendation() {
        System.out.println("\n--- Dapatkan Rekomendasi Produk ---");
        try {
            System.out.print("Masukkan budget maksimum (Rp): ");
            int budget = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Masukkan jenis kulit Anda (mis. Normal, Kering, Berminyak, Kombinasi, atau Semua Kulit): ");
            String skinType = InputUtils.formatCapitalize(scanner.nextLine());
            
            System.out.print("Masukkan kategori produk yang diinginkan (pisahkan dengan koma, mis. Lip,Cushion): ");
            String categoriesInput = scanner.nextLine();
            
            List<String> desiredCategories = new ArrayList<>();
            for (String cat : categoriesInput.split(",")) {
                desiredCategories.add(InputUtils.formatCapitalize(cat.trim()));
            }

            // Ambil produk yang sesuai dengan kriteria awal (filtering)
            List<Product> candidateProducts = dao.getProductsForRecommendation(skinType, desiredCategories);

            if (candidateProducts.isEmpty()) {
                System.out.println("Maaf, tidak ada produk yang sesuai dengan jenis kulit dan kategori tersebut.");
                return;
            }

            System.out.println("Menemukan " + candidateProducts.size() + " produk kandidat. Memulai kalkulasi Knapsack...");

            // Jalankan algoritma Dynamic Programming 0/1 Knapsack untuk mendapatkan rekomendasi terbaik
            List<Product> recommendedProducts = RecommendationEngine.recommendProducts(candidateProducts, budget);

            if (recommendedProducts.isEmpty()) {
                System.out.println("Maaf, budget Anda tidak cukup untuk membeli produk yang sesuai kriteria.");
                return;
            }

            System.out.println("\n=== Hasil Rekomendasi Anda ===");
            printProductsTable(recommendedProducts);

            int totalPrice = 0;
            double totalRating = 0;
            for (Product p : recommendedProducts) {
                totalPrice += p.getPrice();
                totalRating += p.getRating();
            }

            System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Total Harga  : Rp " + totalPrice);
            System.out.println("Total Rating : " + String.format("%.1f", totalRating));
            System.out.println("Sisa Budget  : Rp " + (budget - totalPrice));
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------");

        } catch (NumberFormatException e) {
            System.out.println("Input budget tidak valid. Harap masukkan angka.");
        }
    }

    private static void printProductsTable(List<Product> products) {
        System.out.println("===============================================================================================================================");
        System.out.printf("%-3s | %-10s | %-45s | %-10s | %-12s | %-6s | %-15s%n", 
                        "ID", "Brand", "Nama Produk", "Kategori", "Harga", "Rating", "Skin Type");
        System.out.println("===============================================================================================================================");
        for (Product product : products) {
            System.out.println(product);
        }
        System.out.println("===============================================================================================================================");
    }
}