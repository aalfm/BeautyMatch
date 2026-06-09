package Database;

public class Product {
    private int id;
    private String brand;
    private String productName;
    private String category;
    private int price;
    private double rating;
    private String skinType;

    public Product(int id, String brand, String productName, String category, int price, double rating, String skinType) {
        this.id = id;
        this.brand = brand;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.skinType = skinType;
    }

    @Override
    public String toString() {
        return String.format("%-3d | %-10s | %-45s | %-10s | %-12s | %-6.1f | %-15s",
                id, brand, productName, category, "Rp" + price, rating, skinType);
    }
}