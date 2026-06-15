package frontend;

/**
 * Simple data holder for TableView rows — wraps the backend Product fields.
 */
public class ProductRow {
    private final int    id;
    private final String brand;
    private final String productName;
    private final String category;
    private final int    price;
    private final double rating;
    private final String skinType;

    public ProductRow(int id, String brand, String productName,
                      String category, int price, double rating, String skinType) {
        this.id          = id;
        this.brand       = brand;
        this.productName = productName;
        this.category    = category;
        this.price       = price;
        this.rating      = rating;
        this.skinType    = skinType;
    }

    public int    getId()          { return id; }
    public String getBrand()       { return brand; }
    public String getProductName() { return productName; }
    public String getCategory()    { return category; }
    public int    getPrice()       { return price; }
    public double getRating()      { return rating; }
    public String getSkinType()    { return skinType; }
}
