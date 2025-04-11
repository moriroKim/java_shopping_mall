public class Cart {

    Product[] products = new Product[100];
    int[] quantity = new int[products.length];
    int totalPrice = 0;

    public void addCart(Product product, int quantity) {
        for (int i = 0; i < products.length; i++) {
            if (products[i] == null) {
                products[i] = product;
                this.quantity[i] = quantity;
                break;
            }
        }
    }

    public void calcTotalPrice() {
        for (int i = 0; i < products.length; i++) {
            if (products[i] != null) {
                totalPrice += products[i].prodPrice * quantity[i];
            } else {
                break;
            }
        }
    }
}
