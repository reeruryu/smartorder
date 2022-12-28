package com.example.smartorder.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtils {
	public static String encPassword(String plaintext) {
		if (plaintext == null || plaintext.length() < 1) {
			return "";
		}
		return BCrypt.hashpw(plaintext, BCrypt.gensalt());
	}
}
