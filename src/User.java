import java.util.Scanner;
import java.util.UUID;

public class User {
    String shopId = "";
    String shopName = "";
    DataBase.UserDB userDB;
    DataBase.ShopDB shopDB;
    String userType = "";
    String userId = "";
    String password = "";

    User(DataBase.UserDB userDB, DataBase.ShopDB shopDB) {
        this.userDB = userDB;
        this.shopDB = shopDB;
    }

    public void SignUp() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Printer.printLine("######### 회원가입 페이지 #########");
            Printer.printLine("");
            Printer.printLine("계정 타입 선택 (1: 구매자 / 2: 판매자 / q: 현재 페이지 나가기)");

            Printer.prompt("타입: ");
            String tempStr = scanner.nextLine();

            if (!(tempStr.equals("1") || tempStr.equals("2") || tempStr.equals("q"))) {
                Printer.printLine("잘못된 입력값입니다!");
                continue;
            }

            if (tempStr.equals("q")) {
                Printer.printLine("회원가입 페이지를 종료합니다.");
                break;
            }

            this.userType = tempStr;

            Printer.prompt("아이디: ");
            tempStr = scanner.nextLine();
            this.userId = tempStr;

            Printer.prompt("비밀번호: ");
            tempStr = scanner.nextLine();
            this.password = tempStr;

            if (this.userType.equals("2")) {
                // 판매자일 경우 상점 정보 입력
                Printer.print("%s님의 상점 이름을 입력해주세요: ", this.userId);
                this.shopName = scanner.nextLine();
                this.shopId = UUID.randomUUID().toString(); // 상점 ID 생성
                Printer.print("%s님의 [%s] 개설 및 회원가입 진행중...\n", this.userId, this.shopName);
            }

            // User 객체 생성 후 UserDB에 추가
            User newUser = new User(this.userDB, this.shopDB);
            newUser.userId = this.userId;
            newUser.userType = this.userType;
            newUser.password = this.password;
            newUser.shopId = this.shopId;  // 상점 ID를 User에 설정

            this.userDB.addUser(newUser);

            if (this.userType.equals("2")) {
                // 판매자일 경우 상점도 등록
                Shop shop = new Shop(this.shopId, this.shopName, newUser.userId);
                this.shopDB.addShop(shop);
            }

            User isUserExist = this.userDB.isUserExist(newUser);

            if (isUserExist == null) {
                Printer.error("회원가입에 실패했습니다!");
                this.userType = "";
                this.userId = "";
                this.password = "";
            } else {
                String currUserType = isUserExist.userType.equals("1") ? "구매자" : "판매자";
                Printer.success("");
                Printer.print("[%s]%s님, 회원가입이 완료되었습니다\n", currUserType, isUserExist.userId);
                break;
            }
        }
    }

    public User SignIn() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Printer.printLine("######### 로그인 페이지 #########");
            Printer.printLine("로그인을 종료하려면 'q'를 입력하세요.");

            Printer.prompt("아이디: ");
            String inputId = scanner.nextLine();

            if (inputId.equals("q")) {
                Printer.printLine("로그인을 종료합니다.");
                return null;
            }

            Printer.prompt("비밀번호: ");
            String inputPw = scanner.nextLine();

            User foundUser = this.userDB.findUserById(inputId);

            if (foundUser == null) {
                Printer.error("해당 아이디의 유저가 존재하지 않습니다.");
                continue;
            }

            if (!foundUser.password.equals(inputPw)) {
                Printer.error("비밀번호가 일치하지 않습니다!");
                continue;
            }

            String currUserType = foundUser.userType.equals("1") ? "구매자" : "판매자";
            Printer.success("");
            Printer.print("[%s]%s님, 로그인에 성공하였습니다!\n", foundUser.userId, currUserType);

            return foundUser;
        }
    }
}
