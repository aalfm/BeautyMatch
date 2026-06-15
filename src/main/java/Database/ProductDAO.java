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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM products");
            rs.next();
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

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            addProductInternal(pstmt, "Dior", "Rosy Glow Blush", "Blush", 900000, 4.8, "Semua Kulit");
            addProductInternal(pstmt, "Dior", "Addict Lip Glow", "Lip", 943000, 4.7, "Semua Kulit");
            addProductInternal(pstmt, "Dior", "Forever Cushion", "Cushion", 1250000, 4.8, "Normal, Kering");
            addProductInternal(pstmt, "Dior", "Diorshow Mascara", "Mascara", 820000, 4.7, "Semua Kulit");

            addProductInternal(pstmt, "NARS", "Blush Orgasm", "Blush", 587000, 4.9, "Semua Kulit");
            addProductInternal(pstmt, "NARS", "Powermatte Lip Pigment", "Lip", 417000, 4.8, "Semua Kulit");
            addProductInternal(pstmt, "NARS", "Natural Radiant Cushion", "Cushion", 1030000, 4.7, "Normal, Kombinasi");
            addProductInternal(pstmt, "NARS", "Climax Mascara", "Mascara", 411000, 4.7, "Semua Kulit");

            addProductInternal(pstmt, "Make Over", "Powerstay Suede Blush", "Blush", 160000, 4.6, "Semua Kulit");
            addProductInternal(pstmt, "Make Over", "Powerstay Transferproof Matte Lip Cream", "Lip", 143000, 4.7, "Semua Kulit");
            addProductInternal(pstmt, "Make Over", "Powerstay Demi-Matte Cover Cushion", "Cushion", 225000, 4.7, "Berminyak, Kombinasi");
            addProductInternal(pstmt, "Make Over", "Lash Impulse Waterproof Mascara", "Mascara", 139000, 4.5, "Semua Kulit");

            addProductInternal(pstmt, "Wardah", "Colorfit Cream Blush", "Blush", 59000, 4.5, "Semua Kulit");
            addProductInternal(pstmt, "Wardah", "Colorfit Velvet Matte Lip Mousse", "Lip", 80000, 4.6, "Semua Kulit");
            addProductInternal(pstmt, "Wardah", "Colorfit Perfect Glow Cushion", "Cushion", 135000, 4.6, "Normal, Kering");
            addProductInternal(pstmt, "Wardah", "EyeXpert Perfect Curl Mascara", "Mascara", 75000, 4.5, "Semua Kulit");

            addProductInternal(pstmt, "Emina", "Cheeklit Pressed Blush", "Blush", 45000, 4.4, "Semua Kulit");
            addProductInternal(pstmt, "Emina", "Creamatte Lip Cream", "Lip", 52000, 4.5, "Semua Kulit");
            addProductInternal(pstmt, "Emina", "Bare With Me Mineral Cushion", "Cushion", 64000, 3.9, "Normal");
            addProductInternal(pstmt, "Emina", "Star Lash Aqua Mascara", "Mascara", 56000, 4.4, "Semua Kulit");

            addProductInternal(pstmt, "Viva", "Blush On Duo Color", "Blush", 28000, 4.3, "Semua Kulit");
            addProductInternal(pstmt, "Viva", "Perfect Matte Lip Color", "Lip", 34000, 4.3, "Semua Kulit");
            addProductInternal(pstmt, "Viva", "Queen Perfect Cover Cushion", "Cushion", 70000, 4.2, "Normal, Berminyak");
            addProductInternal(pstmt, "Viva", "Queen Perfect VoluMAX Waterproof Mascara", "Mascara", 44000, 4.2, "Semua Kulit");
        }
    }

    private void addProductInternal(PreparedStatement pstmt, String brand, String name, String category, int price, double rating, String skinType) throws SQLException {
        pstmt.setString(1, brand);
        pstmt.setString(2, name);
        pstmt.setString(3, category);
        pstmt.setInt(4, price);
        pstmt.setDouble(5, rating);
        pstmt.setString(6, skinType);
        pstmt.executeUpdate();
    }

    public void addProduct(Product product) {
        String sql = "INSERT INTO products (brand, product_name, category, price, rating, skin_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getBrand());
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getCategory());
            pstmt.setInt(4, product.getPrice());
            pstmt.setDouble(5, product.getRating());
            pstmt.setString(6, product.getSkinType());
            pstmt.executeUpdate();
            System.out.println("Produk berhasil ditambahkan!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Produk dengan ID " + id + " berhasil dihapus!");
            } else {
                System.out.println("Produk dengan ID " + id + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category LIKE ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + category + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(extractProductFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsForRecommendation(String skinType, List<String> desiredCategories) {
        List<Product> products = new ArrayList<>();
        
        if (desiredCategories == null || desiredCategories.isEmpty()) {
            return products;
        }

        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM products WHERE (skin_type LIKE ? OR skin_type = 'Semua Kulit') AND (");
        for (int i = 0; i < desiredCategories.size(); i++) {
            sqlBuilder.append("category = ?");
            if (i < desiredCategories.size() - 1) {
                sqlBuilder.append(" OR ");
            }
        }
        sqlBuilder.append(")");

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {
            
            pstmt.setString(1, "%" + skinType + "%");
            for (int i = 0; i < desiredCategories.size(); i++) {
                pstmt.setString(i + 2, desiredCategories.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(extractProductFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("brand"),
                rs.getString("product_name"),
                rs.getString("category"),
                rs.getInt("price"),
                rs.getDouble("rating"),
                rs.getString("skin_type")
        );
    }
}