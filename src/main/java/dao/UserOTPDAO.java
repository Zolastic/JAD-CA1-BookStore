package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.OTPManagement;

public class UserOTPDAO {
	
	public int createOTPRowForUser(Connection connection, String customerID) throws SQLException {
		String insertUserOtpSqlStr = "INSERT INTO user_otp (user_id) VALUES (?);";
		try(PreparedStatement insertUserOtpPS = connection.prepareStatement(insertUserOtpSqlStr);) {
			insertUserOtpPS.setString(1, customerID);
			int affectedOtpRows = insertUserOtpPS.executeUpdate();
			return affectedOtpRows;
		}
	}
	
	public boolean verifyOTP(Connection connection, String userID, String otp ) throws SQLException {
		String otpSQL = "SELECT * FROM user_otp WHERE user_id = ? AND otp = ? AND TIMESTAMPDIFF(MINUTE, otp_creation_timestamp, CURRENT_TIMESTAMP()) < 5;";
		try (PreparedStatement otpPS = connection.prepareStatement(otpSQL)) {
			otpPS.setString(1, userID);
			otpPS.setString(2, otp);
			
			ResultSet otpResultSet = otpPS.executeQuery();
			
			if (!otpResultSet.next()) {
				return false;
			}
			return true;
		}
	}
		
	public boolean updateOTP(Connection connection, String userID, String secret) throws Exception {
		String updateOtpSqlStr = "UPDATE user_otp SET  otp = ?, otp_creation_timestamp = CURRENT_TIMESTAMP() WHERE user_id = ?;";
		try (PreparedStatement updateOtpPS = connection.prepareStatement(updateOtpSqlStr)) {
			
			String otp = OTPManagement.generateOTPCode(secret);
			updateOtpPS.setString(1, otp);
			updateOtpPS.setString(2, userID);
			int affectedRows = updateOtpPS.executeUpdate();
			
			if (affectedRows == 0) {
				return false;
			}
			
			return true;
			
		}
	}
}
