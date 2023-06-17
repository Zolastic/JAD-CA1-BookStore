package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import model.Author;
import model.Book;
import model.ReviewHistoryClass;

public class ReviewDAO {
	// Get all reviews of a book
	public List<Map<String, Object>> getBookReviews(Connection connection, String bookID) {
		List<Map<String, Object>> reviews = new ArrayList<>();
		String sqlStr = "SELECT review.*, users.name, users.img FROM review, users WHERE review.custID=users.userID AND bookID=?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, bookID);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> review = new HashMap<>();
					review.put("userName", rs.getString("name"));
					review.put("userImg", rs.getString("img"));
					review.put("review_text", rs.getString("review_text"));
					review.put("ratingByEachCust", rs.getDouble("rating"));
					review.put("ratingDate", rs.getString("ratingDate"));
					reviews.add(review);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return reviews;
	}

	// Function to insert a review
	public String insertReview(Connection connection, String custID, String bookID, String review_text, double rating,
			String transactionHistoryItemID) {
		String review_id = uuidGenerator();
		String sql = "INSERT INTO review (review_id, custID, bookID, review_text, rating, ratingDate, transaction_history_itemID) VALUES (?, ?, ?, ?, ?, ?, ?)";
		String ratingDate = getCurrentDate();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, review_id);
			statement.setString(2, custID);
			statement.setString(3, bookID);
			statement.setString(4, review_text);
			statement.setDouble(5, rating);
			statement.setString(6, ratingDate);
			statement.setString(7, transactionHistoryItemID);
			int rowsAffected = statement.executeUpdate();
			statement.close();
			if (rowsAffected == 1) {
				return review_id;
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			return null;
		}
	}

	// Get current DATE
	private String getCurrentDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();
		return dateFormat.format(currentDate);
	}

	// To generate a UUID
	private String uuidGenerator() {
		UUID uuid = UUID.randomUUID();
		return (uuid.toString());
	}

	// Delete the inserted review
	public int deleteReview(Connection connection, String review_id) {
		String sql = "DELETE FROM review WHERE review_id=?;";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, review_id);
			int rowsAffected = statement.executeUpdate();
			statement.close();
			return rowsAffected;
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			return 0;
		}
	}

	// Function to update user's review state for the transaction history item
	public int updateReviewState(Connection connection, String transactionHistoryItemID, int state) {
		String sql = "UPDATE transaction_history_items SET reviewed=? WHERE transaction_history_itemID=?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, state);
			statement.setString(2, transactionHistoryItemID);
			int rowsAffected = statement.executeUpdate();
			return rowsAffected;
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			return 0;
		}
	}

	// Get all review history of the user
	public List<ReviewHistoryClass> getReviewHistories(Connection connection, String custID) {
		List<ReviewHistoryClass> reviewHistories = new ArrayList<>();
		String query = "SELECT review.*, book.*, genre.genre_name, author.authorName, publisher.publisherName, \r\n"
				+ "(SELECT CAST(AVG(IFNULL(rating, 0)) AS DECIMAL(2, 1)) \r\n"
				+ "FROM review WHERE bookID = book.book_id) AS average_rating\r\n"
				+ "FROM review JOIN book ON review.bookID = book.book_id \r\n"
				+ "JOIN genre ON genre.genre_id = book.genre_id  JOIN author ON book.authorID = author.authorID\r\n"
				+ "JOIN publisher ON book.publisherID = publisher.publisherID WHERE review.custID = ?\r\n"
				+ "ORDER BY review.ratingDate DESC";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, custID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String reviewID = resultSet.getString("review_id");
				String bookID = resultSet.getString("book_id");
				String reviewText = resultSet.getString("review_text");
				double rating = resultSet.getDouble("rating");
				String ratingDate = resultSet.getString("ratingDate");
				String transaction_history_itemID = resultSet.getString("transaction_history_itemID");
				Book book = new Book(resultSet.getString("book_id"), resultSet.getString("ISBN"),
						resultSet.getString("title"), resultSet.getString("authorName"),
						resultSet.getString("publisherName"), resultSet.getString("publication_date"),
						resultSet.getString("description"), resultSet.getString("genre_name"),
						resultSet.getString("img"), resultSet.getInt("sold"), resultSet.getInt("inventory"),
						resultSet.getDouble("price"), resultSet.getDouble("average_rating"));

				ReviewHistoryClass review = new ReviewHistoryClass(book, reviewID, custID, bookID, reviewText, rating,
						ratingDate, transaction_history_itemID);
				reviewHistories.add(review);
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return reviewHistories;
	}

}
