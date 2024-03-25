class Electronics extends Product {
    private  String brand;
    private  String warrantyPeriod;



    public Electronics(String productID, String productName, int availableItems, double price, String brand, String warrantyPeriod) {
        super(productID, productName, price, availableItems);
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    // Getters

    public String getBrand() {
        return brand;
    }

    public String getWarrantyPeriod() {
        return warrantyPeriod;
    }


    //Setters

    public void setBrand(String brand){
        this.brand = brand;
        }
    public void setWarrantyPeriod(String warrantyPeriod){
        this.warrantyPeriod = warrantyPeriod;


    }
    @Override
    public void displayInfo() {
        System.out.println("Electronics: " + getProductID() + " - " + getProductName() + " - Brand: " + brand + " - Warranty: " + warrantyPeriod + " weeks");
    }



}
