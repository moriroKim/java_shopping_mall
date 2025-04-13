import java.util.Scanner;
import java.util.UUID;

public class Product {
    final String PROD_ID = UUID.randomUUID().toString();
    String shopId = ""; // 소속된 상점 ID
    String ownerId = ""; // 판매자 ID
    String prodName = "";
    long prodPrice = 0;
    long prodQuantity = 0;

    Product(String shopId, String ownerId) {
        this.shopId = shopId;
        this.ownerId = ownerId;
    }

    public void add() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("####### 상품 등록 페이지에 접속했습니다. #######");
        System.out.println();

        System.out.print("등록할 상품의 이름을 입력해주세요: ");
        this.prodName = scanner.nextLine();

        System.out.print(this.prodName + "의 가격(원)을 입력해주세요: ");
        this.prodPrice = Long.parseLong(scanner.nextLine());

        System.out.print(this.prodName + "의 수량을 입력해주세요: ");
        this.prodQuantity = Long.parseLong(scanner.nextLine());

        System.out.println("############## 상품 등록 완료 ###############");
        System.out.println();
    }
}