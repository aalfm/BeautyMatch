import java.util.List;
import Database.Product;
import Database.ProductDAO;

public class Main {
    public static void main(String[] args) {
        ProductDAO dao = new ProductDAO();

        dao.initializeDatabase();
        List<Product> products =
                dao.getAllProducts();
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