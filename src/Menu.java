import java.util.Scanner;

public class Menu {
    DataBase.ProductDB prodDB;
    DataBase.ShopDB shopDB;
    DataBase.OrderDB orderDB;
    User currentUser;
    Shop currentShop; // Shop 객체를 추가하여 관리
    Cart cart = new Cart();

    Menu(DataBase.ProductDB prodDB, DataBase.ShopDB shopDB, DataBase.OrderDB orderDB, User currentUser) {
        this.prodDB = prodDB;
        this.shopDB = shopDB;
        this.orderDB = orderDB;
        this.currentUser = currentUser;
        this.currentShop = this.shopDB.getShopByOwner(currentUser.userId); // 현재 사용자에 맞는 shop을 가져옴
    }

    public void showUserMenu(User user) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("############# 커맨드 메뉴 ############");
            System.out.println();
            System.out.println("###### 1 : 전체 상품보기 | 2 : 상품 검색 | 3 : 장바구니에 담기 | q : 로그아웃 ######");
            System.out.println();
            System.out.print("###### 명령어를 입력해주세요: ");
            String command = scanner.nextLine();
            System.out.println("#####################################");
            System.out.println();

            switch (command) {
                case "1":
                    showAllProducts();
                    break;
                case "2":
                    searchProduct(scanner);
                    break;
                case "3":
                    handleCart(scanner);
                    break;
                case "q":
                    System.out.println("로그아웃 합니다.");
                    this.currentUser = null;
                    return;
                default:
                    System.out.println("올바르지 않은 명령어입니다.");
            }
        }
    }
    public void showSellerMenu(User user) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("####### 판매자 메뉴 #######");
            System.out.println("1. 상품조회");
            System.out.println("2. 상품등록");
            System.out.println("3. 상품수정");
            System.out.println("4. 상품삭제");
            System.out.println("5. 주문조회");
            System.out.println("6. 로그아웃");
            System.out.print("입력: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    prodDB.showByOwner(currentShop.ownerId);
                    break;
                case "2":
                    Product newProduct = new Product(currentShop.shopId, currentShop.ownerId, currentShop.shopName);
                    newProduct.add();
                    this.prodDB.addByOwner(newProduct);
                    break;
                case "3":
                    this.prodDB.updateByOwner();
                    break;
                case "4":
                    prodDB.showByOwner(currentShop.ownerId);
                    System.out.print("삭제할 상품 ID를 입력하세요: ");
                    String targetId = scanner.nextLine();
                    this.prodDB.deleteByOwner(targetId);
                    break;
                case "5":
                    if (this.orderDB.db != null) {
                        this.orderDB.showOrdersByShop(currentShop.shopId); // shopId를 currentShop에서 가져옴
                    } else {
                        System.out.println("주문 건이 0건 입니다.");
                    }
                    break;
                case "6":
                    System.out.println("로그아웃합니다.");
                    this.currentUser = null;
                    return;
            }
        }
    }

    // 메서드들
    public void showAllProducts() {
        boolean hasProduct = false;
        for (int i = 0; i < this.prodDB.db.length; i++) {
            Product product = this.prodDB.db[i];
            if (product != null) {
                hasProduct = true;
                System.out.printf("[%d] [%s] %s : %d원 | 재고 : %d개", i, product.shopName, product.prodName, product.prodPrice, product.prodQuantity);
            }
        }
        if (!hasProduct) {
            System.out.println("현재 품목이 없습니다!");
        }
        System.out.println();
    }
    public void searchProduct(Scanner scanner) {
        System.out.print("검색하실 상품명을 입력해주세요: ");
        String targetItem = scanner.nextLine();
        boolean found = false;

        for (int i = 0; i < this.prodDB.db.length; i++) {
            Product product = this.prodDB.db[i];
            if (product != null && product.prodName.equals(targetItem)) {
                found = true;
                System.out.printf("[%d] %s : %d원 | 재고 : %d개", i,  product.prodName, product.prodPrice, product.prodQuantity);
                break;
            }
        }

        if (!found) {
            System.out.println("해당 상품명은 상점에 존재하지 않습니다!");
        }
    }
    public void handleCart(Scanner scanner) {
        System.out.println("########### 장바구니 페이지 ##########");
        System.out.println("q: 나가기 / s: 장바구니 품목 구매하기 / r: 장바구니 비우기");

        showAllProducts();

        while (true) {
            System.out.print("장바구니에 담을 상품 이름을 입력해주세요: ");
            String input = scanner.nextLine();

            switch (input) {
                case "q":
                    return;
                case "r":
                    this.cart.empty();
                    System.out.println("장바구니를 비웠습니다.");
                    continue;
                case "s":
                    if (this.cart.products[0] == null) {
                        System.out.println("장바구니가 텅 비었습니다!");
                        continue;
                    }
                    proceedToPurchase(scanner);
                    return;
                default:
                    Product foundItem = findProductByName(input);
                    if (foundItem == null) {
                        System.out.println("해당 상품명은 상점에 존재하지 않습니다!");
                        continue;
                    }
                    addToCart(scanner, foundItem);
            }
        }
    }
    public void addToCart(Scanner scanner, Product product) {

        if (product.prodQuantity == 0) {
            System.out.println();
            System.out.println("해당 상품은 품절되었습니다.");
            System.out.println();
            return;
        }

        while (true) {
            System.out.println("############## q: 나가기 ###############");
            System.out.printf("몇 개 구매하시겠습니까? (%s의 재고: %d개): ", product.prodName, product.prodQuantity);
            String input = scanner.nextLine();
            long userQty = 0;

            if (input.equals("q")) {
                return;
            }

            try {
                userQty = Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("유효하지 않은 숫자입니다. 정수만 입력해주세요!");
                continue;
            }

            if (userQty > product.prodQuantity || userQty <= 0) {
                System.out.println("재고량을 초과했거나 유효하지 않은 숫자를 입력하셨습니다!");
                continue;
            }

            this.cart.addCart(product, userQty);
            this.cart.calcTotalPrice();
            System.out.printf("총 금액: %d\n", cart.totalPrice);
            break;
        }
    }
    public void proceedToPurchase(Scanner scanner) {
        System.out.println("######### 선택하신 품목 #########");
        for (int i = 0; i < this.cart.products.length; i++) {
            Product p = this.cart.products[i];
            if (p == null) break;
            long eachTotalPrice = p.prodPrice * p.prodQuantity;
            System.out.printf("[%d] %s : %d원 X %d개 ---> 총 %d원", i + 1, p.prodName, p.prodPrice, p.prodQuantity, eachTotalPrice);
        }
        this.cart.calcTotalPrice();
        System.out.printf("총 가격 %d\n", this.cart.totalPrice);

        System.out.print("정말 구매하시겠습니까? (y/n): ");
        String input = scanner.nextLine();

        if (!input.equals("y")) return;

        System.out.print("주소지를 입력해주세요: ");
        String address = scanner.nextLine();

        System.out.print("전화번호를 입력해주세요: ");
        String phoneNum = scanner.nextLine();

        String userId = this.currentUser.userId;
        String shopId = "";

        // 해당 제품의 상점 식별하기
        for (int i = 0; i < this.cart.products.length; i++) {
            if (this.cart.products[i] == null) break;
            shopId = this.cart.products[i].shopId;
        }

        Order newOrder = new Order(shopId, userId, address, phoneNum, cart);
        this.orderDB.addOrder(newOrder);

        updateProductStocks(newOrder);
        System.out.println("[✅] 구매가 완료되었습니다!");
        newOrder.show();

    }
    public void updateProductStocks(Order order) {
        Product[] products = order.cart.products;
        long[] qtyArr = order.cart.quantity;

        for (int i = 0; i < products.length; i++) {
            Product product = products[i];
            if (product == null) break;

            String prodId = product.PROD_ID;
            long newQty = product.prodQuantity - qtyArr[i];
            this.prodDB.sinkDB(prodId, "quantity", newQty);
        }
    }
    public Product findProductByName(String name) {
        for (int i = 0; i < this.prodDB.db.length; i++) {
            Product product = this.prodDB.db[i];
            if (product != null && product.prodName.equals(name)) {
                return product;
            }
        }
        return null;
    }

}
