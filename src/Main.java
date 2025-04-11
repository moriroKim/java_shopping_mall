import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DataBase db = new DataBase();
        User user = new User(db.userDB);
        Menu menu = new Menu(db.productDB, db.shopDB);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("##### 쇼핑몰에 오신것을 환영합니다! #####");
            System.out.println();
            System.out.println("1: 로그인 / 2: 회원가입  / q: 접속종료");
            String tempStr = scanner.nextLine();

            if (tempStr.equals("q")) {
                System.out.println("접속을 종료합니다.");
                break;
            }

            // 로그인 페이지
            if (tempStr.equals("1")) {
                User currentUser = user.SignIn();

                if (currentUser != null) {
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
                User currentUser = user.SignIn();

                if (currentUser != null) {
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