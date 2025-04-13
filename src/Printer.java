public class Printer {

    public static String[] numIco = {"1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣"};

    public static void space(boolean isTwice) {
        if (isTwice) {
            System.out.println();
        }
        System.out.println();
    }

    public static void printLine() {
        System.out.println("----------------------------------------");
    }

    public static void printHash() {
        System.out.println("########################################");
    }

    public static void printHashHeader(String ico, String header) {
        Printer.space(true);
        System.out.printf("########### %s %s ############", ico, header);
        Printer.space(false);
    }

    public static void printBoldLine() {
        Printer.space(false);
        System.out.println("========================================");
        Printer.space(false);
    }

    public static void printOptions(String[] ico, String[] options, boolean hasIco) {
        String[] optionalIco = hasIco ? ico : numIco;
            for (int i = 0; i < options.length; i++) {
                System.out.printf("[%s]   %s\n", optionalIco[i], options[i]);
                Printer.printLine();
            }
    }

    public static void select(String msg) {
        System.out.printf("👉  %s : ", msg);
    }

    public static void loadingMsg(String user, String noun, String verb) {
        String baseMsg = String.format("[ %s ] 님의 %s %s", user, noun, verb);
        String[] dots = {".  ", ".. ", "..."};

        try {
            for (int i = 0; i < 10; i++) {
                for (String dot : dots) {
                    System.out.print("\r" + baseMsg + dot); // 줄 덮어쓰기
                    Thread.sleep(200); // 0.2초 대기
                }
            }
            System.out.println();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Printer.space(true);
    }

    public static void success(String msg) {
        System.out.print("[✅] " + msg);
        Printer.space(true);
    }

    public static void error(String msg) {
        System.out.print("[🚫] " + msg);
        Printer.space(true);
    }

    public static void quitMsg(String msg) {
        System.out.print("[🚪]" + msg);
        Printer.space(true);
    }

    public static void customMsg(String ico, String msg) {
        System.out.printf("[%s] %s", ico, msg);
        Printer.space(true);
    }

    public static void systemMsg(String type, String user, String msg) {
        System.out.printf("[%s] %s 님, %s", type, user, msg);
        Printer.space(true);
    }

    public static void printOrderHeader(Order order) {
        Printer.printHashHeader("🛒", "주문 정보");
        System.out.printf("📅 주문날짜 : %s\n", order.date);
        System.out.printf("🛒 주문번호 : %s\n", order.orderId);
        System.out.printf("🏪 가맹점 ID : %s\n", order.shopId);
        System.out.printf("👤 유저 ID : %s\n", order.userId);
        System.out.printf("📞 연락처 : %s\n", order.phoneNum);
        System.out.printf("🏠 배송지 : %s\n", order.address);
        printBoldLine();
    }

    public static void printCartItems(Cart cart) {
        Printer.printHashHeader("📦", "주문 상품 목록");
        printBoldLine();
        for (int i = 0; i < cart.products.length; i++) {
            if (cart.products[i] == null) break;
            String name = cart.products[i].prodName;
            long qty = cart.quantity[i];
            long price = cart.products[i].prodPrice * cart.quantity[i];
            System.out.printf("[%s] %s x %d개 | %d원 \n", numIco[i], name, qty, price);
        }
        printBoldLine();
        System.out.printf("💰 총 결제 금액: %d원\n", cart.totalPrice);
        printHash();
    }

    public static void printProduct(Product p, int idx) {
        System.out.printf("""
                ----------------------------
                🛍️ [%s] [상품 정보]
                🔖 상품ID   : %s
                📦 상품명   : %s
                🏪 상점이름 : %s
                🧑‍💼 판매자   : %s
                💰 가격     : %d원
                📊 수량     : %d개
                ----------------------------
                """, idx, p.PROD_ID, p.prodName, p.shopName, p.ownerId, p.prodPrice, p.prodQuantity);
    }
}
