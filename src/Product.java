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

    public void add() {
        Scanner scanner = new Scanner(System.in);

        Printer.printLine("####### 상품 등록 페이지에 접속했습니다. #######");
        Printer.printLine("");

        Printer.prompt("등록할 상품의 이름을 입력해주세요: ");
        this.prodName = scanner.nextLine();

        Printer.prompt(this.prodName + "의 가격(원)을 입력해주세요: ");
        this.prodPrice = Long.parseLong(scanner.nextLine());

        Printer.prompt(this.prodName + "의 수량을 입력해주세요: ");
        this.prodQuantity = Long.parseLong(scanner.nextLine());

        Printer.printLine("############## 상품 등록 완료 ###############");
        Printer.printLine("");
    }
}
