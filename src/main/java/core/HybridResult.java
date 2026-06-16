package core;

import Database.Product;
import java.util.List;

public class HybridResult {
    private List<Product> greedyProducts;
    private List<Product> dpProducts;
    private List<Product> otherCategoryProducts;
    
    public HybridResult(List<Product> greedyProducts, List<Product> dpProducts, List<Product> otherCategoryProducts) {
        this.greedyProducts = greedyProducts;
        this.dpProducts = dpProducts;
        this.otherCategoryProducts = otherCategoryProducts;
    }
    
    public List<Product> getGreedyProducts() { return greedyProducts; }
    public List<Product> getDpProducts() { return dpProducts; }
    public List<Product> getOtherCategoryProducts() { return otherCategoryProducts; }
}
