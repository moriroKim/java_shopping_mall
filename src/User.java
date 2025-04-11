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

    User(DataBase.UserDB userDB) {
        this.userDB = userDB;
        this.shopDB = new DataBase.ShopDB();
    }

    public void SignUp() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("######### 회원가입 페이지 #########");
            System.out.println();
            System.out.println("계정 타입 선택 (1: 구매자 / 2: 판매자 / q: 현재 페이지 나가기)");

            System.out.print("타입: ");
            String tempStr = scanner.nextLine();

            if (!(tempStr.equals("1") || tempStr.equals("2") || tempStr.equals("q"))) {
                System.out.println("잘못된 입력값입니다!");
                continue;
            }

            if (tempStr.equals("q")) {
                System.out.println("회원가입 페이지를 종료합니다.");
                break;
            }

            this.userType = tempStr;

            System.out.print("아이디: ");
            tempStr = scanner.nextLine();
            this.userId = tempStr;

            System.out.print("비밀번호: ");
            tempStr = scanner.nextLine();
            this.password = tempStr;

            if (this.userType.equals("2")) {
                System.out.printf("%s님의 상점 이름을 입력해주세요: ", this.userId);
                this.shopName = scanner.nextLine();
                this.shopId = UUID.randomUUID().toString();
                System.out.printf("%s님의 [%s]개설 및 회원가입 진행중...", this.userId, this.shopName);
            }

            this.userDB.addUser(this);
            Shop shop = new Shop(this);
            this.shopDB.addShop(shop);
            User isUserExist = this.userDB.isUserExist(this);

            if (isUserExist == null) {
                System.out.println("회원가입에 실패했습니다!");
                this.userType = "";
                this.userId = "";
                this.password = "";
            } else {
                String currUserType = isUserExist.userType.equals("1") ? "구매자" : "판매자";
                System.out.printf("[%s]%s님, 회원가입이 완료되었습니다\n", currUserType, isUserExist.userId);
                break;
            }
        }

    }
    public User SignIn() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("######### 로그인 페이지 #########");
            System.out.println("로그인을 종료하려면 'q'를 입력하세요.");

            System.out.print("아이디: ");
            String inputId = scanner.nextLine();

            if (inputId.equals("q")) {
                System.out.println("로그인을 종료합니다.");
                return null;
            }

            System.out.print("비밀번호: ");
            String inputPw = scanner.nextLine();

            User foundUser = this.userDB.findUserById(inputId);

            if (foundUser == null) {
                System.out.println("해당 아이디의 유저가 존재하지 않습니다.");
                continue;
            }

            if (!foundUser.password.equals(inputPw)) {
                System.out.println("비밀번호가 일치하지 않습니다.");
                continue;
            }

            String currUserType = foundUser.userType.equals("1") ? "구매자" : "판매자";
            System.out.printf("[%s]%s님, 로그인에 성공하였습니다!\n", foundUser.userId, currUserType);

            return foundUser;
        }
    }


}
