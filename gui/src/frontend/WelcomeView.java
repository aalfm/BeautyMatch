package frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class WelcomeView {

    private final BeautyMatchApp app;
    private final VBox root;

    public WelcomeView(BeautyMatchApp app) {
        this.app = app;
        this.root = build();
    }

    public Region getRoot() { return root; }

    private VBox build() {
        VBox page = new VBox(30);
        page.setAlignment(Pos.TOP_LEFT);
        page.setStyle("-fx-background-color: " + BeautyMatchApp.COLOR_BG + ";");

        // Hero banner
        VBox hero = new VBox(10);
        hero.setPadding(new Insets(32, 36, 32, 36));
        hero.setStyle(
            "-fx-background-color: " + BeautyMatchApp.COLOR_SIDEBAR + ";" +
            "-fx-background-radius: 16;"
        );

        Label emoji = new Label("✿");
        emoji.setStyle("-fx-font-size: 36px;");

        Label headline = new Label("Selamat datang di BeautyMatch");
        headline.setStyle(
            "-fx-font-size: 26px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: Georgia, serif;"
        );
        headline.setWrapText(true);

        Label sub = new Label(
            "Temukan produk skincare & kosmetik terbaik yang sesuai dengan\n" +
            "jenis kulit dan budget kamu — menggunakan algoritma 0/1 Knapsack."
        );
        sub.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #C8A8B0;" +
            "-fx-line-spacing: 4;"
        );

        Button cta = new Button("✨  Mulai Rekomendasi");
        cta.setStyle(
            "-fx-background-color: " + BeautyMatchApp.COLOR_ACCENT + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12 28;" +
            "-fx-cursor: hand;"
        );
        cta.setOnAction(e -> app.navigateTo("recommend"));

        hero.getChildren().addAll(emoji, headline, sub, cta);

        // Stats row
        HBox stats = new HBox(16);
        stats.setAlignment(Pos.CENTER_LEFT);

        try {
            int total = ProductService.getAllProducts().size();
            stats.getChildren().addAll(
                statCard("📦", String.valueOf(total), "Produk tersedia"),
                statCard("🏷", "6", "Brand tersedia"),
                statCard("🧴", "4", "Kategori produk"),
                statCard("⚡", "O(n·W)", "Kompleksitas algoritma")
            );
        } catch (Exception ex) {
            stats.getChildren().add(new Label("Gagal memuat statistik."));
        }

        // Quick action cards
        Label qLabel = new Label("Aksi Cepat");
        qLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + BeautyMatchApp.COLOR_SIDEBAR + ";"
        );

        HBox quick = new HBox(14);
        quick.getChildren().addAll(
            quickAction("📦", "Semua Produk",  "Lihat daftar seluruh produk",    () -> app.navigateTo("products")),
            quickAction("🔍", "Cari Kategori", "Filter produk berdasar kategori", () -> app.navigateTo("search")),
            quickAction("➕", "Tambah Produk", "Daftarkan produk baru",           () -> app.navigateTo("add")),
            quickAction("🗑", "Hapus Produk",  "Hapus produk dari database",      () -> app.navigateTo("delete"))
        );

        page.getChildren().addAll(hero, stats, qLabel, quick);
        return page;
    }

    private VBox statCard(String icon, String value, String label) {
        VBox card = new VBox(4);
        card.setPadding(new Insets(14, 20, 14, 20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + BeautyMatchApp.COLOR_BORDER + ";" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;"
        );

        Label iconLbl = new Label(icon + "  " + value);
        iconLbl.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + BeautyMatchApp.COLOR_SIDEBAR + ";"
        );
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 11px; -fx-text-fill: " + BeautyMatchApp.COLOR_TEXT_MUTED + ";");
        card.getChildren().addAll(iconLbl, lbl);
        return card;
    }

    private VBox quickAction(String icon, String title, String desc, Runnable action) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(16));
        card.setPrefWidth(165);
        card.setStyle(
            "-fx-background-color: " + BeautyMatchApp.COLOR_BLUSH + ";" +
            "-fx-background-radius: 12;" +
            "-fx-cursor: hand;"
        );

        Label iconLbl = new Label(icon);
        iconLbl.setStyle("-fx-font-size: 22px;");
        Label titleLbl = new Label(title);
        titleLbl.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + BeautyMatchApp.COLOR_SIDEBAR + ";"
        );
        Label descLbl = new Label(desc);
        descLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: " + BeautyMatchApp.COLOR_TEXT_MUTED + ";");
        descLbl.setWrapText(true);

        card.getChildren().addAll(iconLbl, titleLbl, descLbl);
        card.setOnMouseClicked(e -> action.run());
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: " + BeautyMatchApp.COLOR_ACCENT + ";" +
            "-fx-background-radius: 12; -fx-cursor: hand;"
        ));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: " + BeautyMatchApp.COLOR_BLUSH + ";" +
            "-fx-background-radius: 12; -fx-cursor: hand;"
        ));
        return card;
    }
}
