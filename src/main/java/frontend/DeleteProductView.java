package frontend;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Optional;

public class DeleteProductView implements ContentView {

    private final VBox root;
    private TableView<ProductRow> table;

    public DeleteProductView() {
        root = build();
    }

    @Override
    public Region getRoot() { return root; }

    private VBox build() {
        VBox page = UI.pageContainer("🗑  Hapus Produk", "Pilih produk dari tabel, lalu klik Hapus");

        // Action card
        VBox actionCard = UI.card(18);
        actionCard.setMaxWidth(500);

        TextField idField = new TextField();
        idField.setPromptText("Masukkan ID produk yang akan dihapus...");

        Button delBtn = UI.dangerBtn("🗑  Hapus Produk");

        Label hint = new Label("💡 Klik baris di tabel untuk mengisi ID secara otomatis");
        hint.setStyle("-fx-font-size: 11px; -fx-text-fill: " + BeautyMatchApp.COLOR_TEXT_MUTED + ";");

        Label feedback = new Label();
        feedback.setStyle("-fx-font-size: 12px;");

        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(idField, Priority.ALWAYS);
        row.getChildren().addAll(UI.formField("ID Produk", idField), delBtn);

        actionCard.getChildren().addAll(row, hint, feedback);

        // Table
        table = UI.buildProductTable();
        loadTable();
        VBox.setVgrow(table, Priority.ALWAYS);

        // Click-to-fill
        table.setOnMouseClicked(e -> {
            ProductRow selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) idField.setText(String.valueOf(selected.getId()));
        });

        delBtn.setOnAction(e -> {
            feedback.setText("");
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                feedback.setText("⚠  Masukkan ID produk terlebih dahulu.");
                feedback.setStyle("-fx-font-size: 12px; -fx-text-fill: #C0392B;");
                return;
            }
            int id;
            try { id = Integer.parseInt(idText); }
            catch (NumberFormatException ex) {
                feedback.setText("⚠  ID harus berupa angka.");
                feedback.setStyle("-fx-font-size: 12px; -fx-text-fill: #C0392B;");
                return;
            }

            // Confirm dialog
            Optional<ProductRow> target = ProductService.getAllProducts().stream()
                .filter(r -> r.getId() == id).findFirst();
            String prodName = target.map(r -> r.getBrand() + " " + r.getProductName()).orElse("ID " + id);

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Konfirmasi Hapus");
            confirm.setHeaderText("Hapus produk ini?");
            confirm.setContentText("\"" + prodName + "\" akan dihapus permanen.");
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean ok = ProductService.deleteProduct(id);
                if (ok) {
                    feedback.setText("✅  Produk ID " + id + " berhasil dihapus.");
                    feedback.setStyle("-fx-font-size: 12px; -fx-text-fill: " + BeautyMatchApp.COLOR_SUCCESS + ";");
                    idField.clear();
                    loadTable();
                } else {
                    feedback.setText("❌  Produk dengan ID " + id + " tidak ditemukan.");
                    feedback.setStyle("-fx-font-size: 12px; -fx-text-fill: #C0392B;");
                }
            }
        });

        page.getChildren().addAll(actionCard, table);
        return page;
    }

    private void loadTable() {
        try {
            List<ProductRow> products = ProductService.getAllProducts();
            table.setItems(FXCollections.observableArrayList(products));
        } catch (Exception ex) {
            UI.showAlert("Error", "Gagal memuat produk: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
