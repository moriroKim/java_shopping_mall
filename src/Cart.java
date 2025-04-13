public class Cart {

    Product[] products = new Product[100];
    long[] quantity = new long[products.length];
    long totalPrice = 0;

    public void addCart(Product product, long quantity) {
        for (int i = 0; i < products.length; i++) {
            if (products[i] == null) {
                products[i] = product;
                this.quantity[i] = quantity;
                break;
            }
        }
    }

    public void calcTotalPrice() {
        totalPrice = 0; // 누적 방지를 위해 초기화
        for (int i = 0; i < products.length; i++) {
            if (products[i] != null) {
                totalPrice += products[i].prodPrice * quantity[i];
            } else {
                break;
            }
        }
    }

    public void empty() {
        this.products = new Product[100];
        this.quantity = new long[products.length];
        this.totalPrice = 0;
    }

    public long[] getQuantity() {
        return this.quantity;
    }
}