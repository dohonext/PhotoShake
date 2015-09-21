package simpledb;

public final class QueryStrings {
	private static final String userGetByIdQuery = "SELECT * from User WHERE UserId = ?";
	private static final String userGetByNoneQuery = "SELECT * from User ORDER BY UserExp DESC";
	private static final String userPostQuery = "INSERT INTO User(UserIdName, UserNickname, UserPassword) VALUES(?,?,?)";
	private static final String postingGetByPageQuery = "SELECT Posting.PostingId, Posting.PostingSubject, Posting.PostingText, Posting.PostingPic, Posting.PostingView, Posting.PostingLike,"+
									"Posting.PostingHate, Posting.PostingTime, Posting.UserId, User.UserIdName, User.UserNickname, User.UserProfile, User.UserProfilePic, User.UserLevel,"+
									"User.UserExp FROM Posting INNER JOIN User ON Posting.UserId = User.UserId " +
									"WHERE Posting.PostingDelete = false LIMIT ? OFFSET ?;"; 
	private static final String postingGetByIdQuery = "SELECT Posting.PostingId, Posting.PostingSubject, Posting.PostingText, Posting.PostingPic, Posting.PostingView, Posting.PostingLike,"+
									"Posting.PostingHate, Posting.PostingTime, Posting.UserId, User.UserIdName, User.UserNickname, User.UserProfile, User.UserProfilePic, User.UserLevel,"+
									"User.UserExp FROM Posting INNER JOIN User ON Posting.UserId = User.UserId " +
									"WHERE Posting.PostingDelete = false AND Posting.PostingId = ?;";
	private static final String bestpostingGetByIdQuery = "SELECT Bestposting.BestpostingId, Posting.PostingId, Posting.PostingSubject, Posting.PostingText, Posting.PostingPic, Posting.PostingView, Posting.PostingLike,"+
									"Posting.PostingHate, Bestposting.BestpostingTime, Posting.UserId, User.UserIdName, User.UserNickname, User.UserProfile, User.UserProfilePic, User.UserLevel,"+
									"User.UserExp FROM Bestposting INNER JOIN Posting ON Bestposting.PostingId = Posting.PostingId INNER JOIN User ON Posting.UserId = User.UserId " +
									"WHERE Bestposting.BestpostingDelete = false AND Bestposting.BestpostingId = ?;";			
	private static final String	bestpostingGetByPageQuery = "SELECT Bestposting.BestpostingId, Posting.PostingSubject, Posting.PostingText, Posting.PostingPic, Posting.PostingView, Posting.PostingLike,"+
									"Posting.PostingHate, Bestposting.BestpostingTime, Posting.UserId, User.UserIdName, User.UserNickname, User.UserProfile, User.UserProfilePic, User.UserLevel,"+
									"User.UserExp FROM Bestposting INNER JOIN Posting ON Bestposting.PostingId = Posting.PostingId INNER JOIN User ON Posting.UserId = User.UserId " +
									"WHERE Bestposting.BestpostingDelete = false LIMIT ? OFFSET ?;";	
	private static final String commentGetQuery = "SELECT * from Comment WHERE PostId = ?;";
	private QueryStrings () {
		
    }
	public static String getUserGetByIdQuery(){
		return userGetByIdQuery;
	}
	public static String getUserGetByNoneQuery(){
		return userGetByNoneQuery;
	}
	public static String getUserPostQuery(){
		return userPostQuery;
	}
    public static String getPostingGetByPageQuery() {
        return postingGetByPageQuery;
    }
    public static String getPostingGetByIdQuery() {
        return postingGetByIdQuery;
    }
    public static String getBestpostingGetByIdQuery() {
        return bestpostingGetByIdQuery;
    }
    public static String getBestpostingGetByPageQuery() {
        return bestpostingGetByPageQuery;
    }
    public static String getCommentGetQuery() {
        return commentGetQuery;
    }
}