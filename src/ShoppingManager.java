import java.util.List;
import java.util.Scanner;

public interface ShoppingManager {
    void addProduct(Scanner scanner);
    void deleteProduct(Scanner scanner);
    void printProductList();
    void saveToFile(String filename);
    void loadFromFile(String filename);
    void addToCart(Product product);
    List<Product> getItemList();
    List<Product> getShoppingCart();
    int getProductQuantity(Product product);
    void openGUI();
}
