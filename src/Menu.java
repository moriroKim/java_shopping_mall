import java.util.Scanner;

public class Menu {
    DataBase.ProductDB prodDB;
    DataBase.ShopDB shopDB;
    DataBase.OrderDB orderDB = new DataBase.OrderDB();
    User currentUser = null;

    Menu(DataBase.ProductDB prodDB, DataBase.ShopDB shopDB, User currentUser) {
        this.prodDB = prodDB;
        this.shopDB = shopDB;
        this.currentUser = currentUser;
    }

    public void showUserMenu(User user) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("############# 커맨드 메뉴 ############");
        System.out.println();
        System.out.println("###### 1 : 전체 상품보기 | 2 : 상품 검색 | 3 : 장바구니에 담기 | q : 나가기 ######");
        System.out.println();
        System.out.print("###### 명령어를 입력해주세요: ");
        String command = scanner.nextLine();
        System.out.println("#####################################");
        System.out.println();

        if (command.equals("1")) {
            boolean hasProduct = false;
            for (int i = 0; i < this.prodDB.db.length; i++) {
                if (this.prodDB.db[i] != null) {
                    hasProduct = true;
                    System.out.println(this.prodDB.db[i].prodName + " : " + this.prodDB.db[i].prodPrice + "원");
                }
            }
            if (!hasProduct) {
                System.out.println("현재 품목이 없습니다!");
                System.out.println();
            }
        }

        if (command.equals("2")) {
            boolean hasProduct = false;

            System.out.print("검색하실 상품명을 입력해주세요: ");
            String targetItem = scanner.nextLine();

            for (int i = 0; i < this.prodDB.db.length; i++) {
                if (this.prodDB.db[i] != null) {
                    if (this.prodDB.db[i].prodName.equals(targetItem)) {
                        hasProduct = true;
                        System.out.println(this.prodDB.db[i]);
                        break;
                    }
                }
            }
            if (!hasProduct) {
                System.out.println("해당 상품명은 상점에 존재하지 않습니다!");
            }
        }

        if (command.equals("3")) {
            Cart cart = new Cart();
            System.out.println("########### 장바구니 페이지 ##########");
            System.out.println("q: 나가기 | s: 장바구니 품목 구매하기");
            while(true) {
                System.out.println();
                System.out.print("장바구니에 담을 상품 이름을 입력해주세요: ");
                String inputValue = scanner.nextLine();

                if (inputValue.equals("q")) {
                    break;
                }

                if (inputValue.equals("s")) {
                    int totalPrice = 0;
                    if (cart.products[0] == null) {
                        System.out.println("장바구니가 텅 비었습니다!");
                        continue;
                    }

                    System.out.println("######### 선택하신 품목 #########");
                    System.out.println();
                    for (int i = 0; i < cart.products.length; i++) {
                        if (cart.products[i] == null) {
                            break;
                        }
                        System.out.println("#" + cart.products[i].prodName + " : " + cart.products[i].prodPrice + "#");
                    }

                    System.out.printf("총 가격 %d", totalPrice);
                    System.out.println("###############################");

                    System.out.println();

                    while(true) {
                        System.out.print("정말 구매하시겠습니까? y/n: ");
                        inputValue = scanner.nextLine();

                        if (inputValue.equals("y")) {
                            System.out.print("배송지를 입력해주세요: ");
                        }

                        if (inputValue.equals("n")) {
                            break;
                        } else {
                            System.out.println("유효하지 않은 명령어입니다!");
                        }
                    }
                }

                // DB에서 실제로 있는 상품인지 검색
                Product foundItem = null;
                for (int i = 0; i < this.prodDB.db.length; i++) {
                    if (this.prodDB.db[i] != null) {
                        if (this.prodDB.db[i].prodName.equals(inputValue)) {
                            foundItem = this.prodDB.db[i];
                        }
                    }
                }

                if (foundItem == null) {
                    System.out.println("해당 상품명은 상점에 존재하지 않습니다!");
                    continue;
                }

                // 구매 개수 선택
                while(true) {
                    int quantity = 0;
                    System.out.printf("몇 개 구매하시겠습니까? (%s의 재고: %d개)", foundItem.prodName, foundItem.prodQuantity);
                    inputValue = scanner.nextLine();

                    try {
                        quantity = Integer.parseInt(inputValue);
                    } catch (Exception e) {
                        System.out.println("유효하지 않은 숫자를 입력하셨습니다. 정수만 입력해주세요!");
                        continue;
                    }

                    if (quantity > foundItem.prodQuantity || quantity < 0) {
                        System.out.println("재고량을 초과했거나 유효하지 않은 숫자를 입력하셨습니다!");
                        continue;
                    }

                    cart.addCart(foundItem, quantity);
                    cart.calcTotalPrice();

                    System.out.printf("총 금액: %d", cart.totalPrice);
                    System.out.println();
                    System.out.print("정말 구매하시겠습니까?: (y/n)");
                    inputValue = scanner.nextLine();

                    if (inputValue.equals("y")) {
                        System.out.print("주소지를 입력해주세요: ");
                        String address = scanner.nextLine();

                        System.out.print("전화번호를 입력해주세요: ");
                        String phoneNum = scanner.nextLine();

                        String shopId = this.shopDB.db[0].shopId;
                        String userId = this.currentUser.userId;

                        Order order = new Order(shopId, userId, address, phoneNum, cart);
                        this.orderDB.addOrder(order);
                    }

                    if (inputValue.equals("n")) {
                        break;
                    }
                }
            }
        }

        if (command.equals("q")) {
            System.out.println("메인 화면으로 돌아갑니다.");
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
                    for (int i = 0; i < db.productDB.db.length; i++) {
                        Product p = db.productDB.db[i];
                        if (p != null && user.userId.equals(p.ownerId)) {
                            System.out.printf("상품ID: %s | %s - %d원 | 수량: %d\n",
                                    p.PROD_ID, p.prodName, p.prodPrice, p.prodQuantity);
                        }
                    }
                    break;
                case "2":
                    Product newProduct = new Product();
                    newProduct.ownerId = user.userId;
                    newProduct.add(); // 상품 등록페이지 실행.
                    db.productDB.addByOwner(newProduct);
                    break;
                // 수정/삭제/주문조회는 추후 확장
                case "6":
                    System.out.println("로그아웃합니다.");
                    return;
            }
        }
    }

}
