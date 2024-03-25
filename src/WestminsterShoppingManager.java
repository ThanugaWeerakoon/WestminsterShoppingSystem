import javax.swing.*;
import java.io.*;
import java.util.*;


public class WestminsterShoppingManager implements ShoppingManager {
    private final ArrayList<Product> itemList;
    private final ShoppingGUI shoppingGUI;
    private final List<Product> shoppingCart = new ArrayList<>(); // Initialize shoppingCart
    private final Map<Product, Integer> productQuantities;
    private final Map<Product, Double> productTotalPrices;





    public WestminsterShoppingManager()  {
        this.itemList = new ArrayList<>();
        this.shoppingGUI = new ShoppingGUI(this);
        this.productTotalPrices = new HashMap<>();
        productQuantities = new HashMap<>(); // Initialize the map

    }

        public static void main(String[] args) {
        WestminsterShoppingManager stock = new WestminsterShoppingManager();

        stock.loadFromFile("productList.txt");
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            System.out.println("======== Westminster Shopping Manager ========");
            System.out.println("1. Add a new product ");
            System.out.println("2. Delete a product ");
            System.out.println("3. Print the list of products ");
            System.out.println("4. Save product list to file");
            System.out.println("5. Open GUI");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    stock.addProduct(scanner);
                    break;
                case 2:
                    stock.deleteProduct(scanner);
                    break;
                case 3:
                    stock.printProductList();
                    break;
                case 4:
                    stock.saveToFile("productList.txt");
                    break;
                case 5:
                    stock.openGUI();
                    break;
                case 0:
                    System.out.println("Exit from Westminster Shopping Manager. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please enter a valid option.");
            }

        } while (option != 0);

        scanner.close();
    }

    public void addProduct(Scanner scanner) {
        if (itemList.size() >= 50) {
            System.out.println("Sorry, the maximum limit of 50 products has been reached. Cannot add more products.");
            return;
        }

        System.out.println("Choose the product type:");
        System.out.println("1. Electronics");
        System.out.println("2. Clothing");
        System.out.print("Enter your choice: ");
        int productTypeChoice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter product ID: ");
        String productID = scanner.nextLine();

        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();

        System.out.print("Enter number of available items: ");
        int availableItems = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        switch (productTypeChoice) {
            case 1:
                addElectronics(scanner, productID, productName, availableItems, price);
                break;
            case 2:
                addClothing(scanner, productID, productName, availableItems, price);
                break;
            default:
                System.out.println("Invalid product type choice.");
        }
    }



    private void addElectronics(Scanner scanner, String productID, String productName, int availableItems, double price) {
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine();

        System.out.print("Enter warranty period (in weeks): ");

        String warrantyPeriod = scanner.nextLine();

        Electronics electronics = new Electronics(productID, productName, availableItems, price, brand, warrantyPeriod);
        itemList.add(electronics);

        System.out.println("Electronics product added to the system.");

    }
    private void addClothing(Scanner scanner, String productID, String productName, int availableItems, double price) {
        System.out.print("Enter size: ");
        String size = scanner.nextLine();

        System.out.print("Enter color: ");
        String color = scanner.nextLine();

        Clothing clothing = new Clothing(productID, productName, availableItems, price, size, color);
        itemList.add(clothing);

        System.out.println("Clothing product added to the system.");

    }



    public void deleteProduct(Scanner scanner) {
        System.out.print("Enter product ID to delete: ");
        String productID = scanner.nextLine();

        Iterator<Product> iterator = itemList.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getProductID().equals(productID)) {
                iterator.remove();

                System.out.println("Product deleted:");
                product.displayInfo();

                if (product instanceof Electronics) {
                    System.out.println("Type: Electronics");
                } else if (product instanceof Clothing) {
                    System.out.println("Type: Clothing");
                }

                System.out.println("Remaining products in the system: " + itemList.size());


                return;
            }
        }
        System.out.println("Product not found in the system.");
    }



    public void printProductList() {
        if (itemList.isEmpty()) {
            System.out.println("No products are found in the system.");
            return;
        }

        System.out.println("==== Product List ====");
        itemList.sort(Comparator.comparing(Product::getProductID));

        for (Product product : itemList) {
            product.displayInfo();
        }
        System.out.println("============================================");
    }
    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Product product : itemList) {
                if (product instanceof Electronics electronics) {
                    writer.write("Electronics,");
                    writer.write(electronics.getProductID() + ",");
                    writer.write(electronics.getProductName() + ",");
                    writer.write(electronics.getAvailableItems() + ","); // Corrected to getAvailableItems()
                    writer.write(electronics.getPrice() + ",");
                    writer.write(electronics.getBrand() + ",");
                    writer.write(electronics.getWarrantyPeriod() + "\n");
                } else if (product instanceof Clothing clothing) {
                    writer.write("Clothing,");
                    writer.write(clothing.getProductID() + ",");
                    writer.write(clothing.getProductName() + ",");
                    writer.write(clothing.getAvailableItems() + ","); // Corrected to getAvailableItems()
                    writer.write(clothing.getPrice() + ",");
                    writer.write(clothing.getSize() + ",");
                    writer.write(clothing.getColor() + "\n");
                }
            }
            System.out.println("Product list saved to file: " + filename);
        } catch (IOException e) {
            System.err.println("Error saving product list to file: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            itemList.clear(); // Clear existing data

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String productType = parts[0].trim(); // Trim to remove spaces

                    if ("Electronics".equals(productType) && parts.length == 7) {
                        String productID = parts[1];
                        String productName = parts[2];
                        int availableItems = Integer.parseInt(parts[3]);
                        double price = Double.parseDouble(parts[4]);
                        String brand = parts[5];
                        String warrantyPeriod = parts[6];

                        Electronics electronics = new Electronics(productID, productName, availableItems, price, brand, warrantyPeriod);
                        itemList.add(electronics);

                    } else if ("Clothing".equals(productType) && parts.length == 7) {
                        String productID = parts[1];
                        String productName = parts[2];
                        int availableItems = Integer.parseInt(parts[3]);
                        double price = Double.parseDouble(parts[4]);
                        String size = parts[5];
                        String color = parts[6];

                        Clothing clothing = new Clothing(productID, productName, availableItems, price, size, color);
                        itemList.add(clothing);
                    } else {
                        System.err.println("Invalid product format in the file: " + line);
                    }
                }
            }
            System.out.println("Product list loaded from file: " + filename);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading product list from file: " + e.getMessage());
        }
    }
    public void addToCart(Product product) {
        if (shoppingCart.contains(product)) {
            //  update the quantity & total price
            int quantity = productQuantities.getOrDefault(product, 0);
            productQuantities.put(product, quantity + 1);

            // Update total price
            double totalPrice = product.getPrice() * productQuantities.get(product);
            productTotalPrices.put(product, totalPrice);
        } else {
            // Product is not in the cart, add it
            shoppingCart.add(product);
            productQuantities.put(product, 1);
            productTotalPrices.put(product, product.getPrice());
        }
    }

    public ArrayList<Product> getItemList() {
        return itemList;
    }
    public List<Product> getShoppingCart() {
        return shoppingCart;
    }

    public int getProductQuantity(Product product) {
        return productQuantities.getOrDefault(product, 0);
    }


    public void openGUI() {
        SwingUtilities.invokeLater(() -> {
            shoppingGUI.updateProductTable();
            shoppingGUI.setVisible(true);
        });
    }

}
