package frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class RecommendationView implements ContentView {

    private final VBox root;

    public RecommendationView() {
        root = build();
    }

    @Override
    public Region getRoot() { return root; }

    private VBox build() {
        VBox page = UI.pageContainer(
            "✨  Rekomendasi Produk",
            "Dapatkan kombinasi produk terbaik dalam budget kamu • Algoritma 0/1 Knapsack"
        );

        // ── Input Form ─────────────────────────────────────────────
        VBox formCard = UI.card(24);

        // Budget
        TextField budgetField = new TextField();
        budgetField.setPromptText("Contoh: 500000");

        // Skin type
        ComboBox<String> skinCombo = new ComboBox<>();
        skinCombo.getItems().addAll(
            "Semua Kulit", "Normal", "Kering", "Berminyak", "Kombinasi"
        );
        skinCombo.setPromptText("Pilih jenis kulit...");
        skinCombo.setMaxWidth(Double.MAX_VALUE);
        skinCombo.setStyle(UI.inputStyle());

        // Categories (checkboxes)
        Label catLabel = new Label("Kategori yang diinginkan:");
        catLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + BeautyMatchApp.COLOR_TEXT + ";");

        CheckBox cbBlush   = styledCheck("🌸  Blush");
        CheckBox cbLip     = styledCheck("💋  Lip");
        CheckBox cbCushion = styledCheck("✨  Cushion");
        CheckBox cbMascara = styledCheck("👁  Mascara");

        HBox checkRow = new HBox(20);
        checkRow.getChildren().addAll(cbBlush, cbLip, cbCushion, cbMascara);

        Button recBtn = UI.primaryBtn("✨  Cari Rekomendasi Terbaik");
        recBtn.setPrefWidth(260);

        // Form layout — 2 columns
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(16);
        GridPane.setHgrow(new Region(), Priority.ALWAYS);

        VBox budgetBox = UI.formField("💰  Budget Maksimum (Rp)", budgetField);
        VBox skinBox   = UI.formField("🧴  Jenis Kulit", skinCombo);
        grid.add(budgetBox, 0, 0);
        grid.add(skinBox,   1, 0);
        ColumnConstraints cc = new ColumnConstraints(); cc.setPercentWidth(50);
        grid.getColumnConstraints().addAll(cc, cc);

        VBox catBox = new VBox(8);
        catBox.getChildren().addAll(catLabel, checkRow);

        HBox btnRow = new HBox();
        btnRow.setAlignment(Pos.CENTER_LEFT);
        btnRow.getChildren().add(recBtn);

        formCard.getChildren().addAll(grid, catBox, btnRow);

        // ── Result Area ────────────────────────────────────────────
        VBox resultArea = new VBox(16);
        resultArea.setVisible(false);

        recBtn.setOnAction(e -> {
            resultArea.setVisible(false);
            resultArea.getChildren().clear();

            // Validate
            int budget;
            try {
                budget = Integer.parseInt(budgetField.getText().trim().replaceAll("[^0-9]", ""));
            } catch (NumberFormatException ex) {
                UI.showAlert("Input Tidak Valid", "Masukkan angka untuk budget.", Alert.AlertType.WARNING);
                return;
            }

            String skin = skinCombo.getValue();
            if (skin == null || skin.isBlank()) {
                UI.showAlert("Input Tidak Valid", "Pilih jenis kulit terlebih dahulu.", Alert.AlertType.WARNING);
                return;
            }

            List<String> cats = new ArrayList<>();
            if (cbBlush.isSelected())   cats.add("Blush");
            if (cbLip.isSelected())     cats.add("Lip");
            if (cbCushion.isSelected()) cats.add("Cushion");
            if (cbMascara.isSelected()) cats.add("Mascara");

            if (cats.isEmpty()) {
                UI.showAlert("Input Tidak Valid", "Pilih minimal satu kategori produk.", Alert.AlertType.WARNING);
                return;
            }

            try {
                List<ProductRow> recs = ProductService.recommend(budget, skin, cats);

                if (recs.isEmpty()) {
                    Label noResult = new Label("😔  Budget tidak cukup atau tidak ada produk yang sesuai kriteria.");
                    noResult.setStyle("-fx-font-size: 14px; -fx-text-fill: " + BeautyMatchApp.COLOR_TEXT_MUTED + ";");
                    resultArea.getChildren().add(noResult);
                } else {
                    // Section title
                    Label resTitle = new Label("🎉  Rekomendasi Terbaik untuk Kamu");
                    resTitle.setStyle(
                        "-fx-font-size: 16px; -fx-font-weight: bold;" +
                        "-fx-text-fill: " + BeautyMatchApp.COLOR_SIDEBAR + ";"
                    );

                    // Product cards grid
                    FlowPane cards = new FlowPane();
                    cards.setHgap(14);
                    cards.setVgap(14);
                    int totalPrice = 0;
                    double totalRating = 0;
                    for (ProductRow r : recs) {
                        cards.getChildren().add(UI.productCard(
                            r.getId(), r.getBrand(), r.getProductName(),
                            r.getCategory(), r.getPrice(), r.getRating(), r.getSkinType()
                        ));
                        totalPrice  += r.getPrice();
                        totalRating += r.getRating();
                    }

                    // Summary bar
                    HBox summary = buildSummaryBar(recs.size(), totalPrice, budget, totalRating);

                    resultArea.getChildren().addAll(resTitle, cards, summary);
                }

                resultArea.setVisible(true);
            } catch (Exception ex) {
                UI.showAlert("Error", "Gagal menghitung rekomendasi: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        ScrollPane scroll = new ScrollPane(resultArea);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-width: 0;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        page.getChildren().addAll(formCard, scroll);
        return page;
    }

    private HBox buildSummaryBar(int count, int total, int budget, double totalRating) {
        HBox bar = new HBox(0);
        bar.setStyle(
            "-fx-background-color: " + BeautyMatchApp.COLOR_SIDEBAR + ";" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 16 24;"
        );
        bar.setSpacing(0);

        bar.getChildren().addAll(
            summaryItem("🛍", count + " Produk", "dipilih"),
            vDivider(),
            summaryItem("💰", "Rp " + String.format("%,d", total).replace(',', '.'), "total harga"),
            vDivider(),
            summaryItem("💚", "Rp " + String.format("%,d", budget - total).replace(',', '.'), "sisa budget"),
            vDivider(),
            summaryItem("★", String.format("%.1f", totalRating), "total rating")
        );
        return bar;
    }

    private VBox summaryItem(String icon, String value, String label) {
        VBox box = new VBox(2);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(0, 28, 0, 28));

        Label v = new Label(icon + "  " + value);
        v.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label l = new Label(label);
        l.setStyle("-fx-font-size: 10px; -fx-text-fill: #9E7D85;");
        box.getChildren().addAll(v, l);
        return box;
    }

    private Region vDivider() {
        Region r = new Region();
        r.setPrefWidth(1);
        r.setPrefHeight(40);
        r.setStyle("-fx-background-color: #7A4A58;");
        return r;
    }

    private CheckBox styledCheck(String text) {
        CheckBox cb = new CheckBox(text);
        cb.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + BeautyMatchApp.COLOR_TEXT + ";" +
            "-fx-cursor: hand;"
        );
        return cb;
    }
}
