import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class ShoppingGUI extends JFrame {
    private final DefaultTableModel productTableModel;
    private final JTable productTable;
    private final WestminsterShoppingManager shoppingManager;
    private final JComboBox<String> productTypeComboBox;
    private final JTextArea detailsTextArea;
    private final JButton addToCartButton;
    private final ShoppingCart cartGUI;


    public ShoppingGUI(WestminsterShoppingManager shoppingManager) {
        this.shoppingManager = shoppingManager;
        this.cartGUI = new ShoppingCart(shoppingManager);

        // Set up the main frame
        setTitle("Westminster Shopping Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a table model for the product table
        Vector<String> columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Name");
        columnNames.add("Price");
        columnNames.add("Additional Info");
        Vector<Vector<String>> data = new Vector<>();
        productTableModel = new DefaultTableModel(data, columnNames);
        productTable = new JTable(productTableModel);

        // Add a scroll pane for the product table
        JScrollPane scrollPane = new JScrollPane(productTable);

        // Create a combo box for product type selection
        productTypeComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});
        productTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProductTable();
            }
        });

        // Create "Show Cart" button
        JButton showCartButton = new JButton("ShowCart");
        showCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cartGUI.updateCartTable();


                cartGUI.setVisible(true);
            }
        });

        // Add spacing to the "Show Cart" button
        showCartButton.setMargin(new Insets(10, 15, 10, 15));

        // panel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());

        // panel for the top-center section
        JPanel centerTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerTopPanel.add(productTypeComboBox);

        // Add padding to the top of the category box
        centerTopPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Add "Show Cart" button to the top panel and align it to the right
        topPanel.add(centerTopPanel, BorderLayout.CENTER);
        topPanel.add(showCartButton, BorderLayout.EAST);

        // Create the details text area
        detailsTextArea = new JTextArea();
        detailsTextArea.setEditable(false);
        detailsTextArea.setLineWrap(true);
        detailsTextArea.setWrapStyleWord(true);

        // Set size for the details text area
        Dimension detailsTextAreaSize = new Dimension(300, 100);
        detailsTextArea.setPreferredSize(detailsTextAreaSize);

        // Add  scroll pane for the details text area
        JScrollPane detailsScrollPane = new JScrollPane(detailsTextArea);

        // Create a panel for the bottom layout
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Add details text area to the bottom panel
        bottomPanel.add(detailsScrollPane, BorderLayout.CENTER);

        addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < productTableModel.getRowCount()) {
                    Product selectedProduct = shoppingManager.getItemList().get(selectedRow);

                    // Adding the selected product to the cart
                    shoppingManager.addToCart(selectedProduct);

                    // Update the cart GUI
                    cartGUI.updateCartTable();

                    
                    // Display the confirmation message
                    JOptionPane.showMessageDialog(ShoppingGUI.this, "Product added to cart: " + selectedProduct.getProductID());

                    // Display the final price
                    cartGUI.displayFinalPrice();
                } else {
                    JOptionPane.showMessageDialog(ShoppingGUI.this, "Please select a product to add to the cart.");
                }
            }
        });




        // Create a panel for the "Add to Cart" button
        JPanel addToCartPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addToCartPanel.add(addToCartButton);

        // Add "Add to Cart" panel to the bottom panel
        bottomPanel.add(addToCartPanel, BorderLayout.SOUTH);

        // Set up the layout
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateProductTable();

        // Add a selection listener to the product table
        productTable.getSelectionModel().addListSelectionListener(e -> showProductDetails());
    }

    // Method to show product details in the details text area
    private void showProductDetails() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < productTableModel.getRowCount()) {
            StringBuilder details = new StringBuilder();
            details.append("Product ID: ").append(productTableModel.getValueAt(selectedRow, 0)).append("\n");
            details.append("Product Name: ").append(productTableModel.getValueAt(selectedRow, 1)).append("\n");
            details.append("Price: Rs").append(productTableModel.getValueAt(selectedRow, 2)).append("\n");

            String additionalInfo = (String) productTableModel.getValueAt(selectedRow, 3);
            details.append("Additional Info: ").append(additionalInfo);

            detailsTextArea.setText(details.toString());
        } else {
            detailsTextArea.setText(""); // Clear details text area if no product is selected
        }
    }

    public void updateProductTable() {
        productTableModel.setRowCount(0);
        String selectedProductType = (String) productTypeComboBox.getSelectedItem();

        for (Product product : shoppingManager.getItemList()) {
            if ((selectedProductType.equals("All"))
                    || (selectedProductType.equals("Electronics") && product instanceof Electronics)
                    || (selectedProductType.equals("Clothing") && product instanceof Clothing)) {

                Vector<String> rowData = new Vector<>();
                rowData.add(product.getProductID());
                rowData.add(product.getProductName());
                rowData.add(String.format("%.2f", product.getPrice()));

                if (product instanceof Electronics electronics) {
                    rowData.add(String.format("Warranty: %s, Brand: %s", electronics.getWarrantyPeriod(), electronics.getBrand()));
                } else if (product instanceof Clothing clothing) {
                    rowData.add(String.format("Color: %s, Size: %s", clothing.getColor(), clothing.getSize()));
                }

                // Check availability
                int availableItems = product.getAvailableItems();
                if (availableItems < 3) {

                    productTableModel.addRow(rowData);
                } else {

                    productTableModel.addRow(rowData);
                }
            }
        }
    }




}
