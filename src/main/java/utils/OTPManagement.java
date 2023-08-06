package utils;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

import java.util.Random;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;

public class OTPManagement {
	public static String createOTP() {
		String numbers = "0123456789";

		Random rndm_method = new Random();

		String otp = "";

		for (int i = 0; i < 6; i++) {
			otp += numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		return otp;
	}
	
	public static String generateSecret() {
		SecretGenerator secretGenerator = new DefaultSecretGenerator();
		String secret = secretGenerator.generate();
		
		return secret;
	}
	
	public static boolean verifyCode(String secret, String code) throws Exception {
		TimeProvider timeProvider = new SystemTimeProvider();
		CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6);
		CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

		return verifier.isValidCode(secret, code);
	}
	
	public static String generateOTPCode(String secret) throws Exception {
		TimeProvider timeProvider = new SystemTimeProvider();
		CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6);

		long currentBucket = Math.floorDiv(timeProvider.getTime(), 30);
		String otp = codeGenerator.generate(secret, currentBucket);
		
		return otp;
	}
	
	public static String generateBase64Image(String secret, String userEmail) throws Exception {
		QrData data = new QrData.Builder()
				   .label(userEmail)
				   .secret(secret)
				   .issuer("Inkwell")
				   .algorithm(HashingAlgorithm.SHA1)
				   .digits(6)
				   .period(30)
				   .build();
		
		QrGenerator generator = new ZxingPngQrGenerator();
		byte[] imageData = generator.generate(data);
		
		String mimeType = generator.getImageMimeType();
		
		String base64Image = getDataUriForImage(imageData, mimeType);
		
		return base64Image;
	}
}
