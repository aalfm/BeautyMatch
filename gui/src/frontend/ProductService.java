package frontend;

import Database.Product;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;

/**
 * Calls RecommendationEngineWrapper (default package) via reflection
 * because Java doesn't allow named-package classes to import default-package classes.
 */
public class ProductService {

    private static Object invoke(String method, Object... args) {
        try {
            Class<?> cls = Class.forName("RecommendationEngineWrapper");
            for (Method m : cls.getMethods()) {
                if (m.getName().equals(method)) {
                    return m.invoke(null, args);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Service error: " + e.getMessage(), e);
        }
        throw new RuntimeException("Method not found: " + method);
    }

    @SuppressWarnings("unchecked")
    public static List<ProductRow> getAllProducts() {
        List<Product> products = (List<Product>) invoke("getAllProducts");
        return toRows(products);
    }

    @SuppressWarnings("unchecked")
    public static List<ProductRow> getByCategory(String category) {
        List<Product> products = (List<Product>) invoke("getByCategory", category);
        return toRows(products);
    }

    @SuppressWarnings("unchecked")
    public static List<ProductRow> recommend(int budget, String skinType, List<String> categories) {
        List<Product> result = (List<Product>) invoke("recommend", budget, skinType, categories);
        return toRows(result);
    }

    public static void addProduct(String brand, String name, String category,
                                  int price, double rating, String skinType) {
        invoke("addProduct", brand, name, category, price, rating, skinType);
    }

    public static boolean deleteProduct(int id) {
        return (Boolean) invoke("deleteProduct", id);
    }

    private static List<ProductRow> toRows(List<Product> products) {
        List<ProductRow> rows = new ArrayList<>();
        if (products == null) return rows;
        for (Product p : products) {
            rows.add(new ProductRow(
                p.getId(), p.getBrand(), p.getProductName(),
                p.getCategory(), p.getPrice(), p.getRating(), p.getSkinType()
            ));
        }
        return rows;
    }
}
