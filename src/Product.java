public abstract class Product {
    private  String productID;
    private  String productName;

    private  double price;
    private int availableItems;


    public Product(String productID, String productName, double price, int availableItems) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.availableItems = availableItems;
    }

    // Getters
    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }


    public double getPrice() {
        return price;

    }

    public int getAvailableItems(){
        return availableItems;
    }

    //Setters

    public void setAvailableItems(int availableItems){
        this.availableItems=availableItems;
    }
    public void setProductID(String productID){
        this.productID = productID;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProductName(String productName){
        this.productName=productName;
    }

    // Abstract method to display product information
    public abstract void displayInfo();

    }
