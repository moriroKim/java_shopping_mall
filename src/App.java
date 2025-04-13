import java.util.Scanner;

public class App {
    User currentUser = null;
    DataBase db;
    User user;
    Scanner scanner = new Scanner(System.in);
    Menu menu; // menu는 currentUser가 로그인 후 생성됨

    App(DataBase db) {
        this.db = db;
        this.user = new User(db.userDB, db.shopDB);
    }

    public void run() {
        while (true) {
            System.out.println("##### 쇼핑몰에 오신것을 환영합니다! #####");
            System.out.println();
            System.out.println("1: 로그인 / 2: 회원가입 / q: 접속종료");
            String tempStr = scanner.nextLine();

            if (tempStr.equals("q")) {
                System.out.println("접속을 종료합니다.");
                return;
            }

            // 로그인 페이지
            if (tempStr.equals("1")) {
                currentUser = user.SignIn();

                if (currentUser != null) {
                    menu = new Menu(db.productDB, db.shopDB, db.orderDB, currentUser);
                    if (currentUser.userType.equals("2")) {
                        menu.showSellerMenu(currentUser);
                    } else {
                        menu.showUserMenu(currentUser);
                    }
                } else {
                    continue;
                }
            }

            // 회원가입 페이지 => 로그인 페이지
            if (tempStr.equals("2")) {
                user.SignUp();
                currentUser = user.SignIn();
                if (currentUser != null) {
                    menu = new Menu(db.productDB, db.shopDB, db.orderDB, currentUser);
                    if (currentUser.userType.equals("2")) {
                        menu.showSellerMenu(currentUser);
                    } else {
                        menu.showUserMenu(currentUser);
                    }
                }
            }
        }
    }
}
