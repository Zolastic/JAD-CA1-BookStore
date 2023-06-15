package utils;

import java.util.Random;

public class OTPCreation {
	public static String createOTP() {
		String numbers = "0123456789";

		Random rndm_method = new Random();

		String otp = "";

		for (int i = 0; i < 6; i++) {
			otp += numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		return otp;
	}
}
