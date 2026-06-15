package frontend;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class BeautyMatchApp extends Application {

    // ── Design Tokens ──────────────────────────────────────────────
    static final String COLOR_BG        = "#FDF6F0";   // cream background
    static final String COLOR_SIDEBAR   = "#5C2D3C";   // deep plum
    static final String COLOR_ACCENT    = "#C9847A";   // rose gold
    static final String COLOR_BLUSH     = "#F5E6E8";   // blush panel
    static final String COLOR_CARD      = "#FFFFFF";   // card white
    static final String COLOR_TEXT      = "#2B2B2B";   // dark text
    static final String COLOR_TEXT_MUTED = "#8C7B80";  // muted text
    static final String COLOR_BORDER    = "#E8D5D8";   // soft border
    static final String COLOR_SUCCESS   = "#6BAA8E";   // green for success
    static final String COLOR_SIDEBAR_TEXT = "#F5E6E8";

    private BorderPane root;
    private StackPane contentArea;
    private Button activeButton;

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: " + COLOR_BG + ";");

        // Header
        root.setTop(buildHeader());

        // Sidebar
        root.setLeft(buildSidebar());

        // Content area (default: welcome screen)
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: " + COLOR_BG + ";");
        contentArea.setPadding(new Insets(30));
        showWelcome();
        root.setCenter(contentArea);

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("BeautyMatch");
        stage.setScene(scene);
        stage.setMinWidth(1024);
        stage.setMinHeight(720);
        stage.centerOnScreen();
        stage.show();
    }

    // ── HEADER ─────────────────────────────────────────────────────
    private HBox buildHeader() {
        HBox header = new HBox();
        header.setStyle(
            "-fx-background-color: " + COLOR_SIDEBAR + ";" +
            "-fx-padding: 0 28 0 0;"
        );
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(58);

        // Brand logo area
        HBox brand = new HBox(10);
        brand.setAlignment(Pos.CENTER);
        brand.setStyle("-fx-background-color: " + COLOR_ACCENT + "; -fx-padding: 0 24;");
        brand.setPrefHeight(58);

        ImageView logoIcon = new ImageView();
        try {
            logoIcon.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/logo.jpeg")));
            logoIcon.setFitWidth(30);
            logoIcon.setFitHeight(30);
            logoIcon.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Gagal memuat logo.jpeg: " + e.getMessage());
        }

        Label logoText = new Label("BeautyMatch");
        logoText.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: Georgia, serif;"
        );
        brand.getChildren().addAll(logoIcon, logoText);

        Label tagline = new Label("Sistem Rekomendasi Skincare & Kosmetik");
        tagline.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + COLOR_SIDEBAR_TEXT + ";" +
            "-fx-padding: 0 0 0 20;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label version = new Label("v1.0  •  Komparasi Greedy & 0/1 Knapsack");
        version.setStyle("-fx-font-size: 11px; -fx-text-fill: #9E7D85;");

        header.getChildren().addAll(brand, tagline, spacer, version);
        return header;
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox(4);
        sidebar.setStyle("-fx-background-color: " + COLOR_SIDEBAR + ";");
        sidebar.setPrefWidth(195);
        sidebar.setPadding(new Insets(20, 0, 20, 0));

        Label menuLabel = new Label("MENU");
        menuLabel.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-text-fill: #9E7D85;" +
            "-fx-padding: 0 0 8 20;" +
            "-fx-font-weight: bold;"
        );

        Button btnBeranda = sidebarBtn("🏠", "Beranda", () -> showWelcome());
        activeButton = btnBeranda;
        btnBeranda.setStyle(sidebarBtnStyle(true));

        sidebar.getChildren().add(menuLabel);
        sidebar.getChildren().addAll(
            btnBeranda,
            sidebarBtn("📦", "Semua Produk",     () -> showPage(new AllProductsView())),
            sidebarBtn("🔍", "Cari Kategori",    () -> showPage(new SearchCategoryView())),
            sidebarBtn("✨", "Rekomendasi",      () -> showPage(new RecommendationView())),
            sidebarBtn("➕", "Tambah Produk",    () -> showPage(new AddProductView())),
            sidebarBtn("🗑",  "Hapus Produk",     () -> showPage(new DeleteProductView()))
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label footer = new Label("© 2025 Kelompok 5\nUniversitas Hasanuddin");
        footer.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-text-fill: #F8F7F4;" +
            "-fx-padding: 0 0 0 20;" +
            "-fx-line-spacing: 3;"
        );
        sidebar.getChildren().addAll(spacer, footer);
        return sidebar;
    }

    private Button sidebarBtn(String icon, String label, Runnable action) {
        Button btn = new Button(icon + "   " + label);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(sidebarBtnStyle(false));
        btn.setPadding(new Insets(12, 16, 12, 20));

        btn.setOnAction(e -> {
            if (activeButton != null && activeButton != btn) {
                activeButton.setStyle(sidebarBtnStyle(false));
            }
            activeButton = btn;
            btn.setStyle(sidebarBtnStyle(true));
            action.run();
        });

        btn.setOnMouseEntered(e -> {
            if (btn != activeButton) {
                btn.setStyle(sidebarBtnStyle(false) + "-fx-background-color: #7A3F50;"); // efek hover
            }
        });
        btn.setOnMouseExited(e -> {
            if (btn != activeButton) {
                btn.setStyle(sidebarBtnStyle(false));
            }
        });

        return btn;
    }

    private String sidebarBtnStyle(boolean active) {
        if (active) {
            return "-fx-background-color: " + COLOR_ACCENT + ";" +
                   "-fx-text-fill: white;" +
                   "-fx-font-size: 13px;" +
                   "-fx-cursor: hand;" +
                   "-fx-border-width: 0;";
        }
        return "-fx-background-color: transparent;" +
               "-fx-text-fill: " + COLOR_SIDEBAR_TEXT + ";" +
               "-fx-font-size: 13px;" +
               "-fx-cursor: hand;" +
               "-fx-border-width: 0;";
    }

    // ── CONTENT SWITCHING ──────────────────────────────────────────
    private void showPage(ContentView view) {
        contentArea.getChildren().setAll(view.getRoot());
    }

    private void showWelcome() {
        WelcomeView welcome = new WelcomeView(this);
        contentArea.getChildren().setAll(welcome.getRoot());
    }

    public void navigateTo(String page) {
        switch (page) {
            case "products"    -> showPage(new AllProductsView());
            case "search"      -> showPage(new SearchCategoryView());
            case "recommend"   -> showPage(new RecommendationView());
            case "add"         -> showPage(new AddProductView());
            case "delete"      -> showPage(new DeleteProductView());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
