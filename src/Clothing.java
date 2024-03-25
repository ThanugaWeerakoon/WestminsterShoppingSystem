class Clothing extends Product {
    private  String size;
    private  String color;

    public Clothing(String productID, String productName, int availableItems, double price, String size, String color) {
        super(productID, productName, price, availableItems);
        this.size = size;
        this.color = color;
    }

    // Getters and Setters

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }




    public void setSize(String size){
        this.size=size;
    }

    public void setColor(String color){
        this.color=color;
    }

    @Override
    public void displayInfo() {
        System.out.println("Clothing: " + getProductID() + " - " + getProductName() + " - Size: " + size + " - Color: " + color);
    }


}
