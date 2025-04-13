import java.util.Scanner;
import java.util.UUID;

public class DataBase {

    public final UserDB userDB = new UserDB();
    public final ShopDB shopDB = new ShopDB();
    public final OrderDB orderDB = new OrderDB();
    public final ProductDB productDB = new ProductDB(orderDB);

    public static class UserDB {
        User[] db = new User[100];
        int dbIdx = 0;

        public void addUser(User user) {
            if (dbIdx >= db.length) {
                System.out.println("유저 DB 용량 초과!");
            } else {
                db[dbIdx] = user;
                dbIdx++;
            }
        }

        public User isUserExist(User user) {
            for (int i = 0; i < this.dbIdx; i++) {
                if (user.userId.equals(this.db[i].userId)) {
                    return this.db[i];
                }
            }
            return null;
        }

        public User findUserById(String id) {
            for (int i = 0; i < dbIdx; i++) {
                if (db[i].userId.equals(id)) return db[i];
            }
            return null;
        }
    }

    public static class ProductDB {
        Scanner scanner = new Scanner(System.in);
        Product[] db = new Product[100];
        int dbIdx = 0;
        OrderDB orderDB;

        public ProductDB(OrderDB orderDB) {
            this.orderDB = orderDB;
        }

        public void showByOwner(String ownerId) {
            for (int i = 0; i < dbIdx; i++) {
                Product p = db[i];
                if (p != null && p.ownerId.equals(ownerId)) { // 해당 판매자(ownerId)가 등록한 제품만 출력
                    System.out.printf("상품ID: %s | %s - %d원 | 수량: %d\n",
                            p.PROD_ID, p.prodName, p.prodPrice, p.prodQuantity);
                }
            }
        }

        public void addByOwner(Product prod) {
            if (dbIdx >= db.length) {
                System.out.println("상품 DB 용량 초과!");
            } else {
                db[dbIdx++] = prod;
            }
        }

        public void updateByOwner() {
            Product[] tempForPrint = new Product[dbIdx];

            System.out.println("########### 상품 수정 페이지 ############");
            System.out.println();
            System.out.print("수정하고 싶은 상품 아이디 또는 이름: ");
            String targetId = scanner.nextLine();

            boolean found = false;
            for (int i = 0; i < dbIdx; i++) {
                Product p = db[i];
                if (p != null && (p.PROD_ID.equals(targetId) || p.prodName.equals(targetId))) {
                    tempForPrint[i] = p;
                    System.out.printf("[%d][✅] 상품ID: %s | %s - %d원 | 수량: %d\n",
                            i, p.PROD_ID, p.prodName, p.prodPrice, p.prodQuantity);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("해당 상품을 찾을 수 없습니다.");
                return;
            }

            while (true) {
                System.out.print("어떤 상품을 수정하시겠습니까? (index 입력 / q: 종료) : ");
                String inputValue = scanner.nextLine();
                if (inputValue.equals("q")) break;

                int targetIdx;
                try {
                    targetIdx = Integer.parseInt(inputValue);
                } catch (Exception e) {
                    System.out.println("잘못된 입력입니다.");
                    continue;
                }

                if (targetIdx >= 0 && targetIdx < tempForPrint.length && tempForPrint[targetIdx] != null) {
                    Product p = tempForPrint[targetIdx];
                    boolean isEdited = false;

                    while (true) {
                        String editStatus = isEdited ? "[\uD83D\uDD27수정됨]" : "[✅]";
                        System.out.printf("%s : [%s] %s : %d원 | %d개\n", editStatus, p.PROD_ID, p.prodName, p.prodPrice, p.prodQuantity);
                        System.out.println("1 : 상품명 수정 / 2 : 가격 수정 / 3 : 수량 수정 / q : 종료");
                        System.out.print("선택: ");
                        String choice = scanner.nextLine();

                        if (choice.equals("1")) {
                            System.out.print("새 상품명: ");
                            String newName = scanner.nextLine();
                            sinkDB(p.PROD_ID, "name", newName);
                            System.out.println("[✅] 상품명이 수정되었습니다.");
                            isEdited = true;
                        } else if (choice.equals("2")) {
                            System.out.print("새 가격: ");
                            try {
                                long newPrice = Long.parseLong(scanner.nextLine());
                                sinkDB(p.PROD_ID, "price", newPrice);
                                System.out.println("[✅] 가격이 수정되었습니다.");
                                isEdited = true;
                            } catch (Exception e) {
                                System.out.println("숫자를 입력해주세요.");
                            }
                        } else if (choice.equals("3")) {
                            System.out.print("새 수량: ");
                            try {
                                long newQty = Long.parseLong(scanner.nextLine());
                                sinkDB(p.PROD_ID, "quantity", newQty);
                                System.out.println("[✅] 수량이 수정되었습니다.");
                                isEdited = true;
                            } catch (Exception e) {
                                System.out.println("숫자를 입력해주세요.");
                            }
                        } else if (choice.equals("q")) {
                            System.out.println("상품 수정 종료.");
                            break;
                        } else {
                            System.out.println("잘못된 선택입니다.");
                        }
                    }
                } else {
                    System.out.println("해당 인덱스의 상품이 없습니다.");
                }
            }
        }

        public void deleteByOwner(String prodId) {
            if (orderDB.isProductInAnyOrder(prodId)) {
                System.out.println("[🚫] 이미 주문된 상품은 삭제할 수 없습니다.");
                return;
            }
            boolean found = false;
            for (int i = 0; i < dbIdx; i++) {
                if (db[i] != null && db[i].PROD_ID.equals(prodId)) {
                    // i 위치부터 한 칸씩 당김
                    for (int j = i; j < dbIdx - 1; j++) {
                        db[j] = db[j + 1];
                    }
                    db[dbIdx - 1] = null; // 마지막은 비움
                    dbIdx--;
                    found = true;
                    System.out.println("[✅] 상품이 삭제되었습니다.");
                    break;
                }
            }

            if (!found) {
                System.out.println("해당 상품을 찾을 수 없습니다.");
            }
        }

        public void sinkDB(String prodId, String field, Object value) { // 어떤 값이나 올 수 있게.
            for (int i = 0; i < this.dbIdx; i++) {
                if (this.db[i].PROD_ID.equals(prodId)) {
                    switch (field) {
                        case "name":
                            this.db[i].prodName = value.toString();
                            break;
                        case "price":
                            this.db[i].prodPrice = (long) value;
                            break;
                        case "quantity":
                            this.db[i].prodQuantity = (long) value;
                            break;
                    }
                    break;
                }
            }
        }
    }

    public static class ShopDB {
        Shop[] db = new Shop[100];
        int dbIdx = 0;

        public void addShop(Shop shop) {
            if (dbIdx >= db.length) {
                System.out.println("상점 DB 용량 초과!");
            } else {
                db[dbIdx] = shop;
                dbIdx++;
            }
        }

        public Shop getShopByOwner(String ownerId) {
            for (int i = 0; i < dbIdx; i++) {
                if (db[i] != null) {
                    if (db[i].ownerId.equals(ownerId)) {
                        return db[i];
                    }
                }
            }
            return null; // Owner에 해당하는 Shop이 없을 경우 null 반환
        }
    }

    public static class OrderDB {
        Order[] db = new Order[100];
        int dbIdx = 0;

        public void addOrder(Order order) {
            if (dbIdx >= db.length) {
                System.out.println("카트 DB 용량 초과!");
            } else {
                db[dbIdx] = order;
                dbIdx++;
            }
        }

        public boolean isProductInAnyOrder(String prodId) {
            for (int i = 0; i < dbIdx; i++) {
                Order order = db[i];
                if (order != null && order.cart.products[i].PROD_ID.equals(prodId)) {
                    return true;
                }
            }
            return false;
        }

        public void showOrdersByShop(String shopId) {
            int orderCnt = 0;
            for (int i = 0; i < db.length; i++) {
                Order o = db[i];
                if (o != null && o.shopId.equals(shopId)) {
                    orderCnt++;
                    o.show(); // 주문 정보 출력
                } else {
                    break;
                }
            }
            System.out.println("총 주문 건 : " + orderCnt + "건");
        }


    }
}
