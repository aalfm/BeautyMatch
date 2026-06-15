package frontend;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class AllProductsView implements ContentView {

    private final VBox root;

    public AllProductsView() {
        root = build();
    }

    @Override
    public Region getRoot() { return root; }

    private VBox build() {
        VBox page = UI.pageContainer("📦  Semua Produk", "Daftar lengkap produk dalam database BeautyMatch");

        try {
            List<ProductRow> products = ProductService.getAllProducts();

            // Summary badge
            Label count = new Label("  " + products.size() + " produk ditemukan  ");
            count.setStyle(
                "-fx-background-color: " + BeautyMatchApp.COLOR_BLUSH + ";" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: " + BeautyMatchApp.COLOR_SIDEBAR + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 4 12;"
            );

            TableView<ProductRow> table = UI.buildProductTable();
            table.setItems(FXCollections.observableArrayList(products));
            VBox.setVgrow(table, Priority.ALWAYS);

            page.getChildren().addAll(count, table);
        } catch (Exception ex) {
            page.getChildren().add(new Label("❌ Gagal memuat produk: " + ex.getMessage()));
        }

        return page;
    }
}
