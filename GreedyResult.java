import Database.Product;
import java.util.List;

public class GreedyResult {
    private final List<Product> selectedProducts;
    private final int totalPrice;
    private final double totalRating;
    private final int remainingBudget;

    public GreedyResult(
        List<Product> selectedProducts, int totalPrice, double totalRating, int remainingBudget
    ) {
        this.selectedProducts = selectedProducts;
        this.totalPrice = totalPrice;
        this.totalRating = totalRating;
        this.remainingBudget = remainingBudget;
    }
        
    public List<Product> getSelectedProducts() {
        return selectedProducts;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public int getRemainingBudget() {
        return remainingBudget;
    }
}
