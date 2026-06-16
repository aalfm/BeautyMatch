package frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import core.GreedyResult;
import Database.Product;

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
            "Bandingkan hasil dari Algoritma Greedy dan Dynamic Programming (0/1 Knapsack) secara langsung!"
        );

        // ── Input Form ─────────────────────────────────────────────
        VBox formCard = UI.card(24);


        // Skin type
        ComboBox<String> skinCombo = new ComboBox<>();
        skinCombo.getItems().addAll(
            "Semua Kulit", "Normal", "Kering", "Berminyak", "Kombinasi"
        );
        skinCombo.setPromptText("Pilih jenis kulit...");
        skinCombo.setMaxWidth(Double.MAX_VALUE);
        skinCombo.setStyle(UI.inputStyle());

        // Input Budget
        Label budgetLabel = new Label("Masukkan Budget Maksimal (Rp)");
        budgetLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + BeautyMatchApp.COLOR_SIDEBAR_TEXT + ";");
        
        TextField budgetField = new TextField();
        budgetField.setPromptText("Contoh: 500000");
        budgetField.setStyle(
            "-fx-padding: 10; -fx-background-radius: 8; -fx-border-radius: 8;" +
            "-fx-border-color: #E2D5D9; -fx-background-color: white;"
        );

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

            // Validate Budget
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
                // Run Greedy
                long startGreedy = System.nanoTime();
                GreedyResult resultGreedy = ProductService.recommendGreedy(budget, skin, cats);
                long timeGreedy = System.nanoTime() - startGreedy;

                // Run DP
                long startDP = System.nanoTime();
                List<Product> recsDP = ProductService.recommendDP(budget, skin, cats);
                long timeDP = System.nanoTime() - startDP;

                // Run Hybrid
                long startHybrid = System.nanoTime();
                List<Product> recsHybrid = ProductService.recommendHybrid(budget, skin, cats);
                long timeHybrid = System.nanoTime() - startHybrid;

                TabPane tabPane = new TabPane();
                tabPane.setStyle("-fx-background-color: transparent;");

                // Tab Greedy
                Tab tabGreedy = new Tab("Metode Greedy");
                tabGreedy.setClosable(false);
                tabGreedy.setContent(buildResultContent(resultGreedy.getSelectedProducts(), resultGreedy.getTotalPrice(), resultGreedy.getTotalRating(), budget, timeGreedy, "Greedy"));

                // Tab DP
                Tab tabDP = new Tab("Metode 0/1 Knapsack");
                tabDP.setClosable(false);
                int dpPrice = 0;
                double dpRating = 0;
                for (Product p : recsDP) {
                    dpPrice += p.getPrice();
                    dpRating += p.getRating();
                }
                tabDP.setContent(buildResultContent(recsDP, dpPrice, dpRating, budget, timeDP, "Knapsack"));

                // Tab Hybrid
                Tab tabHybrid = new Tab("Metode Hybrid");
                tabHybrid.setClosable(false);
                int hybridPrice = 0;
                double hybridRating = 0;
                for (Product p : recsHybrid) {
                    hybridPrice += p.getPrice();
                    hybridRating += p.getRating();
                }
                tabHybrid.setContent(buildResultContent(recsHybrid, hybridPrice, hybridRating, budget, timeHybrid, "Hybrid"));

                tabPane.getTabs().addAll(tabGreedy, tabDP, tabHybrid);

                resultArea.getChildren().add(tabPane);
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

    private VBox buildResultContent(List<Product> recs, int totalPrice, double totalRating, int budget, long timeNano, String methodType) {
        VBox container = new VBox(16);
        container.setPadding(new Insets(16, 0, 0, 0));

        if (recs.isEmpty()) {
            Label noResult = new Label("😔  Budget tidak cukup atau tidak ada produk yang sesuai kriteria.");
            noResult.setStyle("-fx-font-size: 14px; -fx-text-fill: " + BeautyMatchApp.COLOR_TEXT_MUTED + ";");
            container.getChildren().add(noResult);
            return container;
        }

        // Title and Time
        Label resTitle = new Label("🎉  Rekomendasi Terbaik (" + methodType + ")");
        resTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + BeautyMatchApp.COLOR_SIDEBAR + ";");
        
        double timeMs = timeNano / 1_000_000.0;
        Label timeLabel = new Label(String.format("⏱ Waktu Eksekusi: %.3f ms", timeMs));
        timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #8C7B80; -fx-font-style: italic;");
        
        HBox header = new HBox(15, resTitle, timeLabel);
        header.setAlignment(Pos.BOTTOM_LEFT);

        // Grid
        FlowPane cards = new FlowPane();
        cards.setHgap(14);
        cards.setVgap(14);
        for (Product r : recs) {
            cards.getChildren().add(UI.productCard(
                r.getId(), r.getBrand(), r.getProductName(),
                r.getCategory(), r.getPrice(), r.getRating(), r.getSkinType()
            ));
        }

        // Summary
        HBox summary = buildSummaryBar(recs.size(), totalPrice, budget, totalRating);

        container.getChildren().addAll(header, cards, summary);
        return container;
    }

    private HBox buildSummaryBar(int count, int total, int budget, double totalRating) {
        HBox bar = new HBox(0);
        bar.setStyle(
            "-fx-background-color: " + BeautyMatchApp.COLOR_SIDEBAR + ";" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 16 24;"
        );
        bar.setSpacing(0);

        double averageRating = count > 0 ? totalRating / count : 0.0;

        bar.getChildren().addAll(
            summaryItem("🛍", count + " Produk", "dipilih"),
            vDivider(),
            summaryItem("💰", "Rp " + String.format("%,d", total).replace(',', '.'), "total harga"),
            vDivider(),
            summaryItem("💚", "Rp " + String.format("%,d", budget - total).replace(',', '.'), "sisa budget"),
            vDivider(),
            summaryItem("★", String.format("%.1f", averageRating), "rata-rata rating")
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
