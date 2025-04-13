import java.time.LocalDate;
import java.util.UUID;

public class Order {
    final String date = LocalDate.now().toString();
    String orderId = UUID.randomUUID().toString();
    String shopId = "";
    String userId = "";
    String address = "";
    String phoneNum = "";
    Cart cart;

    Order(String shopId, String userId, String address, String phoneNum, Cart cart) {
        this.shopId = shopId;
        this.userId = userId;
        this.address = address;
        this.phoneNum = phoneNum;
        this.cart = cart;
    }

    public void show() {
        Printer.printOrderHeader(this);
        cart.calcTotalPrice();
        Printer.printCartItems(this.cart);
    }
}
