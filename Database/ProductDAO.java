package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public void initializeDatabase() {
        String createTable = """
            CREATE TABLE IF NOT EXISTS products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                brand TEXT NOT NULL,
                product_name TEXT NOT NULL,
                category TEXT NOT NULL,
                price INTEGER NOT NULL,
                rating REAL NOT NULL,
                skin_type TEXT NOT NULL
            );
            """;

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTable);
            ResultSet rs =
                    stmt.executeQuery("SELECT COUNT(*) FROM products");
            if (rs.getInt(1) == 0) {
                insertDefaultProducts(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertDefaultProducts(Connection conn) throws SQLException {
        String sql = """
            INSERT INTO products
            (brand, product_name, category, price, rating, skin_type)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        PreparedStatement pstmt =
                conn.prepareStatement(sql);

        addProduct(pstmt, "Dior", "Rosy Glow Blush", "Blush", 900000, 4.8, "Semua Kulit");
        addProduct(pstmt, "Dior", "Addict Lip Glow", "Lip", 943000, 4.7, "Semua Kulit");
        addProduct(pstmt, "Dior", "Forever Cushion", "Cushion", 1250000, 4.8, "Normal, Kering");
        addProduct(pstmt, "Dior", "Diorshow Mascara", "Mascara", 820000, 4.7, "Semua Kulit");

        addProduct(pstmt, "NARS", "Blush Orgasm", "Blush", 587000, 4.9, "Semua Kulit");
        addProduct(pstmt, "NARS", "Powermatte Lip Pigment", "Lip", 417000, 4.8, "Semua Kulit");
        addProduct(pstmt, "NARS", "Natural Radiant Cushion", "Cushion", 1030000, 4.7, "Normal, Kombinasi");
        addProduct(pstmt, "NARS", "Climax Mascara", "Mascara", 411000, 4.7, "Semua Kulit");

        addProduct(pstmt, "Make Over", "Powerstay Suede Blush", "Blush", 160000, 4.6, "Semua Kulit");
        addProduct(pstmt, "Make Over", "Powerstay Transferproof Matte Lip Cream", "Lip", 143000, 4.7, "Semua Kulit");
        addProduct(pstmt, "Make Over", "Powerstay Demi-Matte Cover Cushion", "Cushion", 225000, 4.7, "Berminyak, Kombinasi");
        addProduct(pstmt, "Make Over", "Lash Impulse Waterproof Mascara", "Mascara", 139000, 4.5, "Semua Kulit");

        addProduct(pstmt, "Wardah", "Colorfit Cream Blush", "Blush", 59000, 4.5, "Semua Kulit");
        addProduct(pstmt, "Wardah", "Colorfit Velvet Matte Lip Mousse", "Lip", 80000, 4.6, "Semua Kulit");
        addProduct(pstmt, "Wardah", "Colorfit Perfect Glow Cushion", "Cushion", 135000, 4.6, "Normal, Kering");
        addProduct(pstmt, "Wardah", "EyeXpert Perfect Curl Mascara", "Mascara", 75000, 4.5, "Semua Kulit");

        addProduct(pstmt, "Emina", "Cheeklit Pressed Blush", "Blush", 45000, 4.4, "Semua Kulit");
        addProduct(pstmt, "Emina", "Creamatte Lip Cream", "Lip", 52000, 4.5, "Semua Kulit");
        addProduct(pstmt, "Emina", "Bare With Me Mineral Cushion", "Cushion", 64000, 3.9, "Normal");
        addProduct(pstmt, "Emina", "Star Lash Aqua Mascara", "Mascara", 56000, 4.4, "Semua Kulit");

        addProduct(pstmt, "Viva", "Blush On Duo Color", "Blush", 28000, 4.3, "Semua Kulit");
        addProduct(pstmt, "Viva", "Perfect Matte Lip Color", "Lip", 34000, 4.3, "Semua Kulit");
        addProduct(pstmt, "Viva", "Queen Perfect Cover Cushion", "Cushion", 70000, 4.2, "Normal, Berminyak");
        addProduct(pstmt, "Viva", "Queen Perfect VoluMAX Waterproof Mascara", "Mascara", 44000, 4.2, "Semua Kulit");
    }

    private void addProduct( PreparedStatement pstmt, String brand, String name, String category, int price, double rating, String skinType) throws SQLException {
        pstmt.setString(1, brand);
        pstmt.setString(2, name);
        pstmt.setString(3, category);
        pstmt.setInt(4, price);
        pstmt.setDouble(5, rating);
        pstmt.setString(6, skinType);

        pstmt.executeUpdate();
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs =
                     stmt.executeQuery("SELECT * FROM products")) {
            while (rs.next()) {
                products.add(
                        new Product(
                                rs.getInt("id"),
                                rs.getString("brand"),
                                rs.getString("product_name"),
                                rs.getString("category"),
                                rs.getInt("price"),
                                rs.getDouble("rating"),
                                rs.getString("skin_type")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}