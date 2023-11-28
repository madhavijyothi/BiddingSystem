package com.craft.biddingSystem.utils;

/**
 * @author Potturi Sireesha
 * @version v 0.1 16/11/23 6:37 pm
 */
public class Utils {

    public static boolean validateEmailId(String emailId) {
        if (!emailId.contains("@gmail.com")) {
            return false;
        }
        return true;
    }

    public static boolean validatePhoneNo(String phoneNo) {
        return phoneNo.matches("\\d{10}");
    }
}
