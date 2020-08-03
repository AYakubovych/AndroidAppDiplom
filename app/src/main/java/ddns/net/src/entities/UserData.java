package ddns.net.src.entities;

public class UserData {

    private static long id;

    private static String email;

    private static String password;

    private static String key;

    public static void clear(){

        UserData.setEmail("");
        UserData.setKey("");
        UserData.setPassword("");
        UserData.setId(0);
    }

    public static long getId() {
        return id;
    }

    public static void setId(long id) {
        UserData.id = id;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        UserData.key = key;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserData.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        UserData.password = password;
    }
}
