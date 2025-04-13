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
        System.out.println("########################################");
        System.out.printf("📅 주문날짜 : %s\n", this.date);
        System.out.printf("🛒 주문번호 : %s\n", this.orderId);
        System.out.printf("🏪 가맹점 ID : %s\n", this.shopId);
        System.out.printf("👤 유저 ID : %s\n", this.userId);
        System.out.printf("📞 연락처 : %s\n", this.phoneNum);
        System.out.printf("🏠 배송지 : %s\n", this.address);
        System.out.println("========================================");
        System.out.println();
        System.out.println("########### 🛍️ 주문 상품 목록 ############");
        System.out.println("========================================");

        for (int i = 0; i < cart.products.length; i++) {
            if (cart.products[i] == null) break;
            String name = cart.products[i].prodName;
            long qty = cart.quantity[i];
            long price = cart.products[i].prodPrice * cart.quantity[i];

            System.out.printf("[%d] %s x %d개 | %d원 \n", i, name, qty, price);
        }

        cart.calcTotalPrice();

        System.out.println("========================================");
        System.out.printf("💰 총 결제 금액: %,d원\n", cart.totalPrice);
        System.out.println("########################################\n");
    }
}
