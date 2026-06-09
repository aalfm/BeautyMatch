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
        return String.format(
                "%d | %s | %s | %s | Rp%d | %.1f | %s",
                id, brand, productName,
                category, price, rating, skinType
        );
    }
}