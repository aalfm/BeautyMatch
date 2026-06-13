package frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Shared interface that every content panel implements.
 */
interface ContentView {
    Region getRoot();
}

/**
 * Factory for shared UI components so every view stays consistent.
 */
class UI {

    // ── Page Scaffold ───────────────────────────────────────────────
    static VBox pageContainer(String title, String subtitle) {
        VBox page = new VBox(0);
        page.setStyle("-fx-background-color: " + BeautyMatchApp.COLOR_BG + ";");

        // Page title bar
        VBox titleBar = new VBox(4);
        titleBar.setPadding(new Insets(0, 0, 24, 0));

        Label h1 = new Label(title);
        h1.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + BeautyMatchApp.COLOR_SIDEBAR + ";" +
            "-fx-font-family: Georgia, serif;"
        );

        if (subtitle != null && !subtitle.isEmpty()) {
            Label sub = new Label(subtitle);
            sub.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + BeautyMatchApp.COLOR_TEXT_MUTED + ";"
            );
            titleBar.getChildren().addAll(h1, sub);
        } else {
            titleBar.getChildren().add(h1);
        }

        // Decorative divider
        Region divider = new Region();
        divider.setPrefHeight(2);
        divider.setStyle("-fx-background-color: " + BeautyMatchApp.COLOR_ACCENT + ";");
        divider.setMaxWidth(60);

        titleBar.getChildren().add(divider);
        page.getChildren().add(titleBar);
        return page;
    }

    // ── Card ────────────────────────────────────────────────────────
    static VBox card(double padding) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(padding));
        card.setStyle(
            "-fx-background-color: " + BeautyMatchApp.COLOR_CARD + ";" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(92,45,60,0.08), 12, 0, 0, 3);"
        );
        return card;
    }

    // ── Product Card (for recommendation results) ───────────────────
    static VBox productCard(int id, String brand, String name, String category,
                            int price, double rating, String skinType) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(16));
        card.setPrefWidth(190);
        card.setMaxWidth(220);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + BeautyMatchApp.COLOR_BORDER + ";" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(92,45,60,0.07), 8, 0, 0, 2);"
        );

        // Category badge
        Label badge = new Label(category);
        badge.setStyle(
            "-fx-background-color: " + BeautyMatchApp.COLOR_BLUSH + ";" +
            "-fx-background-radius: 20;" +
            "-fx-text-fill: " + BeautyMatchApp.COLOR_SIDEBAR + ";" +
            "-fx-font-size: 10px;" +
            "-fx-padding: 2 8;" +
            "-fx-font-weight: bold;"
        );

        // Product icon
        Label icon = new Label(categoryIcon(category));
        icon.setStyle("-fx-font-size: 30px;");
        icon.setAlignment(Pos.CENTER);
        icon.setMaxWidth(Double.MAX_VALUE);

        Label brandLbl = new Label(brand);
        brandLbl.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-text-fill: " + BeautyMatchApp.COLOR_ACCENT + ";" +
            "-fx-font-weight: bold;"
        );

        Label nameLbl = new Label(name);
        nameLbl.setWrapText(true);
        nameLbl.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + BeautyMatchApp.COLOR_TEXT + ";"
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label skinLbl = new Label("🧴 " + skinType);
        skinLbl.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-text-fill: " + BeautyMatchApp.COLOR_TEXT_MUTED + ";"
        );
        skinLbl.setWrapText(true);

        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_LEFT);
        Label priceLbl = new Label("Rp " + String.format("%,d", price).replace(',', '.'));
        priceLbl.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + BeautyMatchApp.COLOR_SIDEBAR + ";"
        );
        Region sp2 = new Region(); HBox.setHgrow(sp2, Priority.ALWAYS);
        Label ratingLbl = new Label("★ " + String.format("%.1f", rating));
        ratingLbl.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #E8A838;" +
            "-fx-font-weight: bold;"
        );
        footer.getChildren().addAll(priceLbl, sp2, ratingLbl);

        card.getChildren().addAll(badge, icon, brandLbl, nameLbl, spacer, skinLbl, footer);
        return card;
    }

    static String categoryIcon(String category) {
        if (category == null) return "💄";
        return switch (category.toLowerCase()) {
            case "blush"   -> "🌸";
            case "lip"     -> "💋";
            case "cushion" -> "✨";
            case "mascara" -> "👁";
            default        -> "💄";
        };
    }

    // ── Table ───────────────────────────────────────────────────────
    static TableView<ProductRow> buildProductTable() {
        TableView<ProductRow> table = new TableView<>();
        table.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + BeautyMatchApp.COLOR_BORDER + ";" +
            "-fx-border-radius: 12;" +
            "-fx-font-size: 12px;"
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("Tidak ada data produk."));

        TableColumn<ProductRow, String> colId       = col("ID",          "id",          60);
        TableColumn<ProductRow, String> colBrand    = col("Brand",       "brand",       100);
        TableColumn<ProductRow, String> colName     = col("Nama Produk", "productName", 220);
        TableColumn<ProductRow, String> colCat      = col("Kategori",    "category",    90);
        TableColumn<ProductRow, String> colPrice    = col("Harga",       "price",       110);
        TableColumn<ProductRow, String> colRating   = col("Rating",      "rating",      70);
        TableColumn<ProductRow, String> colSkin     = col("Jenis Kulit", "skinType",    130);

        table.getColumns().addAll(colId, colBrand, colName, colCat, colPrice, colRating, colSkin);
        return table;
    }

    private static TableColumn<ProductRow, String> col(String header, String prop, double w) {
        TableColumn<ProductRow, String> c = new TableColumn<>(header);
        c.setCellValueFactory(data -> {
            ProductRow r = data.getValue();
            String val = switch (prop) {
                case "id"          -> String.valueOf(r.getId());
                case "brand"       -> r.getBrand();
                case "productName" -> r.getProductName();
                case "category"    -> r.getCategory();
                case "price"       -> "Rp " + String.format("%,d", r.getPrice()).replace(',','.');
                case "rating"      -> "★ " + String.format("%.1f", r.getRating());
                case "skinType"    -> r.getSkinType();
                default            -> "";
            };
            return new javafx.beans.property.SimpleStringProperty(val);
        });
        c.setPrefWidth(w);
        return c;
    }

    // ── Form Helpers ────────────────────────────────────────────────
    static VBox formField(String label, Control input) {
        VBox box = new VBox(5);
        Label lbl = new Label(label);
        lbl.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + BeautyMatchApp.COLOR_TEXT + ";"
        );
        input.setStyle(inputStyle());
        input.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().addAll(lbl, input);
        return box;
    }

    static String inputStyle() {
        return  "-fx-background-color: " + BeautyMatchApp.COLOR_BG + ";" +
                "-fx-border-color: " + BeautyMatchApp.COLOR_BORDER + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 8 12;" +
                "-fx-font-size: 13px;";
    }

    static Button primaryBtn(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: " + BeautyMatchApp.COLOR_ACCENT + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 24;" +
            "-fx-cursor: hand;"
        );
        return btn;
    }

    static Button dangerBtn(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: #C0392B;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 24;" +
            "-fx-cursor: hand;"
        );
        return btn;
    }

    // ── Alert / Toast ───────────────────────────────────────────────
    static void showAlert(String title, String msg, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
