public class InputUtils {
    public static String formatCapitalize(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        
        // Memisahkan kata berdasarkan spasi
        String[] words = input.toLowerCase().trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (word.length() > 0) {
                // Huruf pertama kapital, sisanya sesuai aslinya (sudah lowerCase)
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1))
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }
}
