package emildobrev.Ecommerce.Store.util;

import emildobrev.Ecommerce.Store.user.User;

public class Utils {

    public static String getFullName(User user) {
        return  String.format("%s %s", user.getFirstName(), user.getLastName()).trim();
    }
}
