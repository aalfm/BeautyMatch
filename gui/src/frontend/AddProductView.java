package frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AddProductView implements ContentView {

    private final VBox root;

    public AddProductView() {
        root = build();
    }

    @Override
    public Region getRoot() { return root; }

    private VBox build() {
        VBox page = UI.pageContainer("➕  Tambah Produk Baru", "Daftarkan produk kosmetik ke dalam database");

        VBox formCard = UI.card(28);
        formCard.setMaxWidth(640);

        TextField brandField    = new TextField();  brandField.setPromptText("Contoh: Wardah, NARS, Dior");
        TextField nameField     = new TextField();  nameField.setPromptText("Contoh: Colorfit Velvet Matte Lip Mousse");
        ComboBox<String> catCombo = new ComboBox<>();
        catCombo.getItems().addAll("Blush", "Lip", "Cushion", "Mascara");
        catCombo.setPromptText("Pilih kategori...");
        catCombo.setMaxWidth(Double.MAX_VALUE);
        catCombo.setStyle(UI.inputStyle());

        TextField priceField    = new TextField();  priceField.setPromptText("Contoh: 135000");
        TextField ratingField   = new TextField();  ratingField.setPromptText("Contoh: 4.5");
        ComboBox<String> skinCombo = new ComboBox<>();
        skinCombo.getItems().addAll("Semua Kulit", "Normal", "Kering", "Berminyak",
                                    "Kombinasi", "Normal, Kering", "Berminyak, Kombinasi",
                                    "Normal, Kombinasi", "Normal, Berminyak");
        skinCombo.setPromptText("Pilih jenis kulit...");
        skinCombo.setMaxWidth(Double.MAX_VALUE);
        skinCombo.setStyle(UI.inputStyle());

        // 2-column grid
        GridPane grid = new GridPane();
        grid.setHgap(20); grid.setVgap(16);
        ColumnConstraints cc = new ColumnConstraints(); cc.setPercentWidth(50);
        grid.getColumnConstraints().addAll(cc, cc);

        grid.add(UI.formField("Brand *",        brandField),  0, 0);
        grid.add(UI.formField("Kategori *",     catCombo),    1, 0);
        grid.add(UI.formField("Nama Produk *",  nameField),   0, 1, 2, 1); // span 2 cols
        grid.add(UI.formField("Harga (Rp) *",   priceField),  0, 2);
        grid.add(UI.formField("Rating (1-5) *", ratingField), 1, 2);
        grid.add(UI.formField("Jenis Kulit *",  skinCombo),   0, 3, 2, 1);

        Label info = new Label("* Wajib diisi");
        info.setStyle("-fx-font-size: 11px; -fx-text-fill: " + BeautyMatchApp.COLOR_TEXT_MUTED + ";");

        Button saveBtn = UI.primaryBtn("💾  Simpan Produk");

        Label feedback = new Label();
        feedback.setStyle("-fx-font-size: 12px;");

        saveBtn.setOnAction(e -> {
            feedback.setText("");
            String brand  = brandField.getText().trim();
            String name   = nameField.getText().trim();
            String cat    = catCombo.getValue();
            String skin   = skinCombo.getValue();
            String priceS = priceField.getText().trim().replaceAll("[^0-9]", "");
            String ratS   = ratingField.getText().trim();

            if (brand.isEmpty() || name.isEmpty() || cat == null || skin == null
                    || priceS.isEmpty() || ratS.isEmpty()) {
                feedback.setText("⚠  Semua kolom wajib diisi.");
                feedback.setStyle("-fx-font-size: 12px; -fx-text-fill: #C0392B;");
                return;
            }

            int price; double rating;
            try {
                price  = Integer.parseInt(priceS);
                rating = Double.parseDouble(ratS);
                if (rating < 1 || rating > 5) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                feedback.setText("⚠  Harga harus angka, rating antara 1.0 – 5.0.");
                feedback.setStyle("-fx-font-size: 12px; -fx-text-fill: #C0392B;");
                return;
            }

            try {
                ProductService.addProduct(capitalize(brand), capitalize(name), cat, price, rating, skin);
                feedback.setText("✅  Produk \"" + name + "\" berhasil ditambahkan!");
                feedback.setStyle("-fx-font-size: 12px; -fx-text-fill: " + BeautyMatchApp.COLOR_SUCCESS + ";");
                // Reset form
                brandField.clear(); nameField.clear(); priceField.clear(); ratingField.clear();
                catCombo.setValue(null); skinCombo.setValue(null);
            } catch (Exception ex) {
                feedback.setText("❌  Gagal menyimpan: " + ex.getMessage());
                feedback.setStyle("-fx-font-size: 12px; -fx-text-fill: #C0392B;");
            }
        });

        formCard.getChildren().addAll(grid, info, saveBtn, feedback);
        page.getChildren().add(formCard);
        return page;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        String[] words = s.toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty()) sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
