public class Cart {

    Product[] products = new Product[100];
    long[] quantity = new long[products.length];
    long totalPrice = 0;

    public void addCart(Product product, long quantity) {
        for (int i = 0; i < products.length; i++) {
            // 상품의 ID가 동일하면 장바구니에 새로운 객체를 추가하지 않고 기존 객체의 수량값만 증가
            if (products[i] != null && products[i].PROD_ID.equals(product.PROD_ID)) {
                this.quantity[i] += quantity;
                return;
            }
            if (products[i] == null) {
                products[i] = product;
                this.quantity[i] = quantity;
                return;
            }
        }
    }

    public void calcTotalPrice() {
        totalPrice = 0; // 누적 방지를 위해 초기화
        for (int i = 0; i < products.length; i++) {
            if (products[i] != null) {
                totalPrice += products[i].prodPrice * quantity[i];
            } else {
                return;
            }
        }
    }

    public void empty() {
        this.products = new Product[100];
        this.quantity = new long[products.length];
        this.totalPrice = 0;
    }

}