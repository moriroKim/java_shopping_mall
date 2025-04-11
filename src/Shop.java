import java.util.UUID;

public class Shop {
    String ownerId;
    String shopId = UUID.randomUUID().toString();
    DataBase.ProductDB productDB;

    public Shop(User user) {
        this.ownerId = user.userId;
        user.shopId = this.shopId;
        this.productDB = new DataBase.ProductDB();
    }
}
