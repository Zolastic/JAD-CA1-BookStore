package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import utils.OTPManagement;

public class UserOTPDAO {
	private String updateOtpSqlStr = "UPDATE user_otp SET  otp = ?, otp_creation_timestamp = CURRENT_TIMESTAMP() WHERE user_id = ?;";
	
	public boolean updateOTP(Connection connection, String userID, String secret) {
		try (PreparedStatement updateOtpPS = connection.prepareStatement(updateOtpSqlStr)) {
			
			String otp = OTPManagement.generateOTPCode(secret);
			updateOtpPS.setString(1, otp);
			updateOtpPS.setString(2, userID);
			int affectedRows = updateOtpPS.executeUpdate();
			
			if (affectedRows == 0) {
				return false;
			}
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
