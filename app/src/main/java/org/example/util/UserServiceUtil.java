package org.example.util;

import org.mindrot.jbcrypt.BCrypt;

public class UserServiceUtil {

    public static String hashPassword(String pointPassword){
        return BCrypt.hashpw(pointPassword,BCrypt.gensalt());
    }

    public static boolean checkPassword(String pointPassword,String hashedPassword){
        return BCrypt.checkpw(pointPassword,hashedPassword);
    }

}
