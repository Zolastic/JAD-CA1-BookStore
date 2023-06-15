package utils;

import java.util.Random;

public class OTPCreation {
	public static char[] CreateOTP() {
		System.out.println("Generating OTP using random() : ");
		System.out.print("You OTP is : ");

		String numbers = "0123456789";

		Random rndm_method = new Random();

		char[] otp = new char[6];

		for (int i = 0; i < 6; i++) {
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		return otp;
	}
}
