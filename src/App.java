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
            Printer.printLine("\n" +
                    "┌────────────────────────────────────┐\n" +
                    "│   🛍️  Welcome to Shopping Mall     │\n" +
                    "└────────────────────────────────────┘");
            Printer.printLine(""); // 빈 줄 출력
            Printer.printLine("1️⃣  로그인");
            Printer.printLine("2️⃣  회원가입");
            Printer.printLine("❌  q: 접속 종료");
            Printer.prompt("\n👉 선택: ");

            String tempStr = scanner.nextLine();

            if (tempStr.equals("q")) {
                Printer.printLine("\n👋 접속을 종료합니다. 다음에 또 만나요!");
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
                    Printer.printLine("\n🚫 올바른 메뉴를 선택해주세요.\n");
            }
        }
    }

    private void showMenu(User user) {
        menu = new Menu(db.productDB, db.shopDB, db.orderDB, user);
        if (user.userType.equals("2")) {
            menu.showSellerMenu(user);
        } else {
            menu.showUserMenu(user);
        }
    }
}
