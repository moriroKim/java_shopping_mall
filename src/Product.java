import java.util.Scanner;
import java.util.UUID;

public class Product {
    final String PROD_ID = UUID.randomUUID().toString();
    String shopId = ""; // 소속된 상점 ID
    String shopName = ""; // 소속된 상점 이름
    String ownerId = ""; // 판매자 ID
    String prodName = "";
    long prodPrice = 0;
    long prodQuantity = 0;

    Product(String shopId, String ownerId, String shopName) {
        this.shopId = shopId;
        this.ownerId = ownerId;
        this.shopName = shopName;
    }

    public boolean add() {
        Scanner scanner = new Scanner(System.in);

        Printer.printHashHeader("🆕", "상품 등록 페이지");
        Printer.printBoldLine();
        Printer.select("등록할 상품의 이름을 입력해주세요");

        try {
            this.prodName = scanner.nextLine();
            Printer.select("[ " + this.prodName + " ]" + "의 가격(원)을 입력해주세요");
            this.prodPrice = Long.parseLong(scanner.nextLine());

            Printer.select("[ " + this.prodName + " ]" + "의 수량을 입력해주세요");
            this.prodQuantity = Long.parseLong(scanner.nextLine());

            if (this.prodPrice <= 0 || this.prodQuantity <= 0) {
                Printer.error("가격과 수량은 0보다 커야 합니다. 상품 등록 실패.");
                return false;
            }

        } catch (NumberFormatException e) {
            Printer.error("숫자 형식이 올바르지 않습니다. 상품 등록 실패.");
            return false;
        }
        Printer.printBoldLine();
        Printer.printHash();
        return true;
    }
}
