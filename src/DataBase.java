import java.util.Scanner;
import java.util.UUID;

public class DataBase {

    public final UserDB userDB = new UserDB();
    public final ShopDB shopDB = new ShopDB();
    public final ProductDB productDB = new ProductDB();


    public static class UserDB {
        User[] db = new User[100];
        int dbIdx = 0;

        public void addUser(User user) {
            if (dbIdx >= db.length) {
                System.out.println("유저 DB 용량 초과!");
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
        Product[] db = new Product[100];
        int dbIdx = 0;

        public void addByOwner(Product prod) {
            if (dbIdx >= db.length) {
                System.out.println("상품 DB 용량 초과!");
            } else {
                db[dbIdx++] = prod;
            }
        }

        public void showByOwner(String ownerId) {
            for (int i = 0; i < dbIdx; i++) {
                Product p = db[i];
                if (p != null && p.ownerId.equals(ownerId)) {
                    System.out.printf("상품ID: %s | %s - %d원 | 수량: %d\n",
                            p.PROD_ID, p.prodName, p.prodPrice, p.prodQuantity);
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
                db[dbIdx++] = shop;
            }
        }
    }

    public static class OrderDB {
        Order[] db = new Order[100];
        int dbIdx = 0;

        public void addOrder(Order order) {
            if (dbIdx >= db.length) {
                System.out.println("카트 DB 용량 초과!");
            } else {
                db[dbIdx++] = order;
            }
        }

        public Order findOrder(String orderId) {
            Order targetOrder = null;
            for (int i = 0; i < dbIdx; i++) {
                if (db[i].orderId.equals(orderId)) {
                    targetOrder = db[i];
                }
            }

            return targetOrder;
        }
    }
}
