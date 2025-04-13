public class Printer {
    public static void print(String format, Object... args) {
        System.out.printf(format + "\n", args);
    }

    public static void line() {
        System.out.println("----------------------------------------");
    }

    public static void success(String msg) {
        print("[✅] " + msg);
    }

    public static void error(String msg) {
        print("[🚫] " + msg);
    }

    public static void prompt(String msg) {
        System.out.print(msg);
    }

    public static void printLine(String line) {
        System.out.println(line);
    }

    public static void printFormat(String format, Object... args) {
        System.out.printf(format, args);
    }

    public static void printOrderHeader(Order order) {
        printLine("########################################");
        printFormat("📅 주문날짜 : %s\n", order.date);
        printFormat("🛒 주문번호 : %s\n", order.orderId);
        printFormat("🏪 가맹점 ID : %s\n", order.shopId);
        printFormat("👤 유저 ID : %s\n", order.userId);
        printFormat("📞 연락처 : %s\n", order.phoneNum);
        printFormat("🏠 배송지 : %s\n", order.address);
        printLine("========================================\n");
    }

    public static void printCartItems(Cart cart) {
        printLine("########### 🛍️ 주문 상품 목록 ############");
        printLine("========================================");
        for (int i = 0; i < cart.products.length; i++) {
            if (cart.products[i] == null) break;
            String name = cart.products[i].prodName;
            long qty = cart.quantity[i];
            long price = cart.products[i].prodPrice * cart.quantity[i];
            printFormat("[%d] %s x %d개 | %,d원 \n", i, name, qty, price);
        }
        printLine("========================================");
        printFormat("💰 총 결제 금액: %,d원\n", cart.totalPrice);
        printLine("########################################\n");
    }
}
