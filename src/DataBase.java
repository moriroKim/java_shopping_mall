import java.util.Scanner;

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
                Printer.error("유저 DB 용량 초과!");
            } else {
                db[dbIdx++] = user;
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
                if (p != null && p.ownerId.equals(ownerId)) {
                    Printer.printProduct(p, i);
                }
            }
        }

        public void addByOwner(Product prod) {
            if (dbIdx >= db.length) {
                Printer.error("상품 DB 용량 초과!");
            } else {
                db[dbIdx++] = prod;
            }
        }

        public void updateByOwner() {
            Product[] tempForPrint = new Product[dbIdx];

            Printer.printHashHeader("🔧", "상품 수정");
            Printer.space(false);
            Printer.select("상품 id or 이름");
            String targetId = scanner.nextLine();

            boolean found = false;
            for (int i = 0; i < dbIdx; i++) {
                Product p = db[i];
                if (p != null && (p.PROD_ID.equals(targetId) || p.prodName.equals(targetId))) {
                    tempForPrint[i] = p;
                    Printer.printProduct(p, i);
                    found = true;
                }
            }

            if (!found) {
                Printer.error("해당 상품을 찾을 수 없습니다.");
                return;
            }

            while (true) {
                String[] options = {"수정 : 번호입력", "종료 : q"};
                String[] ico = {"🔧", "❌"};
                Printer.printOptions(ico, options, true);
                Printer.select("선택");
                String inputValue = scanner.nextLine();
                if (inputValue.equals("q")) break;

                int targetIdx;
                try {
                    targetIdx = Integer.parseInt(inputValue);
                } catch (Exception e) {
                    Printer.error("잘못된 입력입니다.");
                    continue;
                }

                if (targetIdx >= 0 && targetIdx < tempForPrint.length && tempForPrint[targetIdx] != null) {
                    Product p = tempForPrint[targetIdx];
                    boolean isEdited = false;

                    label:
                    while (true) {
                        String editStatus = isEdited ? "🔧" : "📌";
                        String[] editOptions = {"상품명 수정", "가격 수정", "수량 수정"};

                        Printer.customMsg(editStatus, isEdited ? "수정됨" : "");
                        Printer.printProduct(p, targetIdx);
                        Printer.printOptions(null, editOptions, false);

                        Printer.select("선택");
                        String choice = scanner.nextLine();

                        switch (choice) {
                            case "1":
                                Printer.select("상품명 변경");
                                String newName = scanner.nextLine();
                                sinkDB(p.PROD_ID, "name", newName);
                                Printer.success("상품명이 수정되었습니다.");
                                isEdited = true;
                                break;
                            case "2":
                                Printer.select("가격 변경");
                                try {
                                    long newPrice = Long.parseLong(scanner.nextLine());
                                    sinkDB(p.PROD_ID, "price", newPrice);
                                    Printer.success("가격이 수정되었습니다.");
                                    isEdited = true;
                                } catch (Exception e) {
                                    Printer.error("숫자를 입력해주세요.");
                                }
                                break;
                            case "3":
                                Printer.select("수량 변경");
                                try {
                                    long newQty = Long.parseLong(scanner.nextLine());
                                    sinkDB(p.PROD_ID, "quantity", newQty);
                                    Printer.success("수량이 수정되었습니다.");
                                    isEdited = true;
                                } catch (Exception e) {
                                    Printer.error("숫자를 입력해주세요.");
                                }
                                break;
                            case "4":
                                Printer.quitMsg("상품 수정 종료");
                                break label;
                            default:
                                Printer.error("잘못된 선택입니다.");
                                break;
                        }
                    }
                } else {
                    Printer.error("해당 인덱스의 상품이 없습니다.");
                }
            }
        }

        public void deleteByOwner(String prodId) {
            if (orderDB.isProductInAnyOrder(prodId)) {
                Printer.error("이미 주문된 상품은 삭제할 수 없습니다.");
                return;
            }
            boolean found = false;
            for (int i = 0; i < dbIdx; i++) {
                if (db[i] != null && db[i].PROD_ID.equals(prodId)) {
                    for (int j = i; j < dbIdx - 1; j++) {
                        db[j] = db[j + 1];
                    }
                    db[dbIdx - 1] = null;
                    dbIdx--;
                    found = true;
                    Printer.success("상품이 삭제되었습니다.");
                    break;
                }
            }

            if (!found) {
                Printer.error("해당 상품을 찾을 수 없습니다.");
            }
        }

        public void sinkDB(String prodId, String field, Object value) {
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
                Printer.error("상점 DB 용량 초과!");
            } else {
                db[dbIdx++] = shop;
            }
        }

        public Shop getShopByOwner(String ownerId) {
            for (int i = 0; i < dbIdx; i++) {
                if (db[i] != null && db[i].ownerId.equals(ownerId)) {
                    return db[i];
                }
            }
            return null;
        }
    }

    public static class OrderDB {
        Order[] db = new Order[100];
        int dbIdx = 0;

        public void addOrder(Order order) {
            if (dbIdx >= db.length) {
                Printer.error("카트 DB 용량 초과!");
            } else {
                db[dbIdx++] = order;
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
            for (int i = 0; i < dbIdx; i++) {
                Order o = db[i];
                if (o != null && o.shopId.equals(shopId)) {
                    orderCnt++;
                    o.show();
                }
            }

            String msg = "총 주문 건 : " + orderCnt + "건";
            Printer.customMsg("📋", msg);
        }
    }
}
