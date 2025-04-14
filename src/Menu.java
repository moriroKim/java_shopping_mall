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

    public void showUserMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String[] options = {"전체 상품보기", "상품 검색", "장바구니에 담기", "로그아웃"};
            String[] icons = {Printer.numIco[0], Printer.numIco[1], Printer.numIco[2], "🔒" };

            Printer.printHashHeader("😊","유저 메뉴");
            Printer.printBoldLine();
            Printer.printLine();
            Printer.printOptions(icons, options, true);
            Printer.select("명령어를 입력해주세요");
            String command = scanner.nextLine();
            Printer.printBoldLine();
            Printer.printHash();

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
                    Printer.customMsg(icons[3],"로그아웃 합니다.");
                    this.currentUser = null;
                    return;
                default:
                    Printer.error("올바르지 않은 명령어입니다.");
            }
        }
    }

    public void showSellerMenu() {
        Scanner scanner = new Scanner(System.in);
        String[] options = {"상품조회", "상품등록", "상품수정", "상품삭제", "주문조회", "로그아웃"};
        String[] icons = {Printer.numIco[0], Printer.numIco[1], Printer.numIco[2], Printer.numIco[3], Printer.numIco[4], Printer.numIco[5] };

        while (true) {
            Printer.printHashHeader("🛠️","판매자 메뉴");
            Printer.printBoldLine();
            Printer.printLine();
            Printer.printOptions(icons, options, true);
            Printer.select("명령어를 입력해주세요");
            String input = scanner.nextLine();
            Printer.printBoldLine();
            Printer.printHash();

            switch (input) {
                case "1":
                    prodDB.showByOwner(currentShop.ownerId);
                    break;
                case "2":
                    Product newProduct = new Product(currentShop.shopId, currentShop.ownerId, currentShop.shopName);
                    boolean isValidProd = newProduct.add();
                    if (isValidProd) {
                        this.prodDB.addByOwner(newProduct);
                    }
                    break;
                case "3":
                    this.prodDB.updateByOwner();
                    break;
                case "4":
                    prodDB.showByOwner(currentShop.ownerId);
                    Printer.select("삭제할 상품 ID를 입력하세요: ");
                    String targetId = scanner.nextLine();
                    this.prodDB.deleteByOwner(targetId);
                    break;
                case "5":
                    if (this.orderDB.db != null) {
                        this.orderDB.showOrdersByShop(currentShop.shopId); // shopId를 currentShop에서 가져옴
                    } else {
                        Printer.error("주문 건이 없습니다!");
                    }
                    break;
                case "6":
                    Printer.customMsg(icons[3],"로그아웃 합니다.");
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
                Printer.printProduct(product, i);
            }
        }
        if (!hasProduct) {
            Printer.error("현재 품목이 없습니다!");
        }
        Printer.printBoldLine();
    }

    public void searchProduct(Scanner scanner) {
        Printer.select("검색하실 상품명을 입력해주세요");
        String targetItem = scanner.nextLine();
        boolean found = false;

        for (int i = 0; i < this.prodDB.db.length; i++) {
            Product product = this.prodDB.db[i];
            if (product != null && product.prodName.equals(targetItem)) {
                found = true;
                Printer.printProduct(product, i);
                break;
            }
        }

        if (!found) {
            Printer.error("해당 상품명은 상점에 존재하지 않습니다!");
        }
    }

    public void handleCart(Scanner scanner) {
        String[] icons = {"💰", "🗑️", "🚪"};
        String[] options = {"장바구니 품목 구매하기 : s", "장바구니 비우기: r", "나가기 : q"};

        Printer.printHashHeader("🛒", "장바구니 페이지");
        Printer.printBoldLine();
        Printer.printLine();
        Printer.printOptions(icons, options, true);
        Printer.printBoldLine();
        Printer.printHash();

        showAllProducts();

        while (true) {
            Printer.select("장바구니에 담을 상품 이름을 입력해주세요");
            String input = scanner.nextLine();

            switch (input) {
                case "q":
                    return;
                case "r":
                    this.cart.empty();
                    Printer.customMsg(icons[2], "장바구니를 비웠습니다.");
                    continue;
                case "s":
                    if (this.cart.products[0] == null) {
                        Printer.error("장바구니가 텅 비었습니다!");
                        continue;
                    }
                    proceedToPurchase(scanner);
                    return;
                default:
                    Product foundItem = findProductByName(input);
                    if (foundItem == null) {
                        Printer.error("해당 상품명은 상점에 존재하지 않습니다!");
                        continue;
                    }
                    addToCart(scanner, foundItem);
            }
        }
    }

    public void addToCart(Scanner scanner, Product product) {
        String[] icons = {"🚪"};
        String[] options = {"나가기 : q"};

        if (product.prodQuantity == 0) {
            Printer.error("해당 상품은 품절되었습니다!");
            return;
        }

        while (true) {
            Printer.printHashHeader("💰", "구매 페이지");
            Printer.printBoldLine();
            Printer.printLine();
            Printer.printOptions(icons, options, true);
            Printer.printProduct(product, 0);
            Printer.select("몇 개 구매하시겠습니까?");
            String input = scanner.nextLine();
            Printer.printBoldLine();
            Printer.printHash();

            long userQty = 0;

            if (input.equals("q")) {
                return;
            }

            try {
                userQty = Long.parseLong(input);
            } catch (NumberFormatException e) {
                Printer.error("유효하지 않은 숫자입니다. 정수만 입력해주세요!");
                continue;
            }

            if (userQty > product.prodQuantity || userQty <= 0) {
                Printer.error("재고량을 초과했거나 유효하지 않은 숫자를 입력하셨습니다!");
                continue;
            }

            this.cart.addCart(product, userQty);
            this.cart.calcTotalPrice();
            String msg = "총 금액" + cart.totalPrice + "원";
            Printer.customMsg("💰", msg);
            break;
        }
    }

    public void proceedToPurchase(Scanner scanner) {
        Printer.printHashHeader("🧺","선택하신 품목");
        for (int i = 0; i < this.cart.products.length; i++) {
            Product p = this.cart.products[i];
            if (p == null) break;
            long eachTotalPrice = p.prodPrice * p.prodQuantity;
            System.out.printf("👉 [%d] %s\n", i + 1, p.prodName);
            System.out.printf("   💰 가격: %d원   ✖ 수량: %d개\n", p.prodPrice, p.prodQuantity);
            System.out.printf("   📦 합계: %d원\n\n", eachTotalPrice);
        }
        this.cart.calcTotalPrice();
        System.out.println("===================================");
        System.out.printf("🧾 총 가격 💵: %d원\n", this.cart.totalPrice);
        System.out.println("===================================\n");
        System.out.println("👉 결제를 진행하려면 아무 키나 누르세요...");

        Printer.select("정말 구매하시겠습니까? [y/n]: ");
        String input = scanner.nextLine();

        if (!input.equals("y")) return;

        Printer.select("주소지를 입력해주세요");
        String address = scanner.nextLine();

        Printer.select("전화번호를 입력해주세요");
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
        Printer.success("구매가 완료되었습니다!");
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
