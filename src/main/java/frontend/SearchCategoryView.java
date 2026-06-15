package frontend;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class SearchCategoryView implements ContentView {

    private final VBox root;
    private TableView<ProductRow> table;

    public SearchCategoryView() {
        root = build();
    }

    @Override
    public Region getRoot() { return root; }

    private VBox build() {
        VBox page = UI.pageContainer("🔍  Cari Produk", "Filter produk berdasarkan kategori");

        // Search bar card
        VBox searchCard = UI.card(20);

        Label hint = new Label("Pilih atau ketik kategori produk:");
        hint.setStyle("-fx-font-size: 12px; -fx-text-fill: " + BeautyMatchApp.COLOR_TEXT_MUTED + ";");

        // Quick category buttons
        HBox quickBtns = new HBox(10);
        quickBtns.setAlignment(Pos.CENTER_LEFT);
        for (String cat : new String[]{"Blush", "Lip", "Cushion", "Mascara"}) {
            Button b = new Button(UI.categoryIcon(cat) + "  " + cat);
            b.setStyle(
                "-fx-background-color: " + BeautyMatchApp.COLOR_BLUSH + ";" +
                "-fx-text-fill: " + BeautyMatchApp.COLOR_SIDEBAR + ";" +
                "-fx-background-radius: 20;" +
                "-fx-border-color: " + BeautyMatchApp.COLOR_BORDER + ";" +
                "-fx-border-radius: 20;" +
                "-fx-font-size: 12px;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 5 14;"
            );
            b.setOnAction(e -> doSearch(cat));
            quickBtns.getChildren().add(b);
        }

        TextField input = new TextField();
        input.setPromptText("Ketik kategori lain (contoh: Blush, Lip, Mascara)...");
        input.setStyle(UI.inputStyle());

        Button searchBtn = UI.primaryBtn("🔍  Cari");

        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(input, Priority.ALWAYS);
        row.getChildren().addAll(input, searchBtn);

        searchCard.getChildren().addAll(hint, quickBtns, row);

        // Results area
        table = UI.buildProductTable();
        table.setPlaceholder(new Label("Pilih kategori di atas untuk melihat produk."));
        VBox.setVgrow(table, Priority.ALWAYS);

        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + BeautyMatchApp.COLOR_TEXT_MUTED + ";");

        searchBtn.setOnAction(e -> {
            String val = input.getText().trim();
            if (!val.isEmpty()) doSearch(val);
        });
        input.setOnAction(e -> searchBtn.fire());

        page.getChildren().addAll(searchCard, resultLabel, table);
        return page;
    }

    private void doSearch(String category) {
        try {
            List<ProductRow> results = ProductService.getByCategory(category);
            table.setItems(FXCollections.observableArrayList(results));
            if (results.isEmpty()) {
                table.setPlaceholder(new Label("Tidak ada produk dengan kategori: " + category));
            }
        } catch (Exception ex) {
            UI.showAlert("Error", "Gagal mencari produk: " + ex.getMessage(), javafx.scene.control.Alert.AlertType.ERROR);
        }
    }
}
