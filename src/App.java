import java.util.Scanner;

public class App {
    User currentUser = null;
    DataBase db;
    User user;
    Scanner scanner = new Scanner(System.in);
    Menu menu;

    App(DataBase db) {
        this.db = db;
        this.user = new User(db.userDB, db.shopDB);
    }

    public void run() {
        while (true) {
            System.out.print("\n" +
                    "┌────────────────────────────────────┐\n" +
                    "│   🛍️  Welcome to Shopping Mall     │\n" +
                    "└────────────────────────────────────┘");
            System.out.println();
            String[] options = {"로그인", "회원가입", "프로그램 종료"};
            Printer.printOptions(null, options, false);
            Printer.select("선택");

            String tempStr = scanner.nextLine();
            if (tempStr.equals("3")) {
                Printer.printBoldLine();
                Printer.systemMsg(currentUser.userType, currentUser.userId, "다음에 또 만나요! 👋");
                Printer.printBoldLine();
                return;
            }

            switch (tempStr) {
                case "1":
                    currentUser = user.SignIn();
                    if (currentUser != null) {
                        showMenu(currentUser);
                    }
                    break;

                case "2":
                    user.SignUp();
                    currentUser = user.SignIn();
                    if (currentUser != null) {
                        showMenu(currentUser);
                    }
                    break;

                default:
                    Printer.error("올바른 메뉴를 선택해주세요!");
            }
        }
    }

    private void showMenu(User user) {
        menu = new Menu(db.productDB, db.shopDB, db.orderDB, user);
        if (user.userType.equals("2")) {
            menu.showSellerMenu();
        } else {
            menu.showUserMenu();
        }
    }
}
