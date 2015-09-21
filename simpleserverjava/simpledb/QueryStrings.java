package simpledb;

public final class QueryStrings {
	private static final String userGetByIdQuery = "SELECT * from User WHERE UserId = ?";
	private static final String userGetByNoneQuery = "SELECT UserIdName, UserNickname, UserProfile, UserProfilePic, UserLevel, UserExp from User ORDER BY UserExp DESC";
	private static final String userPostQuery = "INSERT INTO User(UserIdName, UserNickname, UserPassword, UserProfile, UserProfilePic, UserType, UserLevel, UserExp, UserCookieString) values(?,?,?,?,?,1,1,0,?)";
	private static final String postingGetByPageQuery = "SELECT Posting.PostingId, Posting.PostingSubject, Posting.PostingText, Posting.PostingPic, Posting.PostingView, Posting.PostingLike,"+
									"Posting.PostingHate, Posting.PostingTime, Posting.UserId, User.UserIdName, User.UserNickname, User.UserProfile, User.UserProfilePic, User.UserLevel,"+
									"User.UserExp FROM Posting INNER JOIN User ON Posting.UserId = User.UserId " +
									"WHERE Posting.PostingDelete = false ORDER BY Posting.PostingTime DESC LIMIT ? OFFSET ?;"; 
	private static final String postingGetByIdQuery = "SELECT Posting.PostingId, Posting.PostingSubject, Posting.PostingText, Posting.PostingPic, Posting.PostingView, Posting.PostingLike,"+
									"Posting.PostingHate, Posting.PostingTime, Posting.UserId, User.UserIdName, User.UserNickname, User.UserProfile, User.UserProfilePic, User.UserLevel,"+
									"User.UserExp FROM Posting INNER JOIN User ON Posting.UserId = User.UserId " +
									"WHERE Posting.PostingDelete = false AND Posting.PostingId = ?;";
	private static final String postingPostQuery = "INSERT INTO Posting(PostingSubject, PostingText, PostingPic, PostingView, PostingLike, PostingHate, PostingTime, PostingDelete, UserId)"+
									"values(?, ?, ?, 0, 0, 0, now(), false, ?)";
	private static final String bestpostingGetByIdQuery = "SELECT Bestposting.BestpostingId, Posting.PostingId, Posting.PostingSubject, Posting.PostingText, Posting.PostingPic, Posting.PostingView, Posting.PostingLike,"+
									"Posting.PostingHate, Bestposting.BestpostingTime, Posting.UserId, User.UserIdName, User.UserNickname, User.UserProfile, User.UserProfilePic, User.UserLevel,"+
									"User.UserExp FROM Bestposting INNER JOIN Posting ON Bestposting.PostingId = Posting.PostingId INNER JOIN User ON Posting.UserId = User.UserId " +
									"WHERE Bestposting.BestpostingDelete = false AND Bestposting.BestpostingId = ?;";			
	private static final String	bestpostingGetByPageQuery = "SELECT Bestposting.BestpostingId, Posting.PostingId, Posting.PostingSubject, Posting.PostingText, Posting.PostingPic, Posting.PostingView, Posting.PostingLike,"+
									"Posting.PostingHate, Bestposting.BestpostingTime, Posting.UserId, User.UserIdName, User.UserNickname, User.UserProfile, User.UserProfilePic, User.UserLevel,"+
									"User.UserExp FROM Bestposting INNER JOIN Posting ON Bestposting.PostingId = Posting.PostingId INNER JOIN User ON Posting.UserId = User.UserId " +
									"WHERE Bestposting.BestpostingDelete = false ORDER BY Bestposting.BestpostingTime DESC LIMIT ? OFFSET ?;";	
	private static final String commentGetQuery = "SELECT Comment.CommentId, Comment.CommentText, Comment.CommentTime, Comment.UserId, User.UserIdName, User.UserNickname, User.UserProfile,"+
									"User.UserProfilePic, User.UserLevel, User.UserExp FROM Comment INNER JOIN User ON Comment.UserId = User.UserId WHERE Comment.CommentDelete = false AND Comment.PostingId = ?;";  
	private static final String commentPostQuery = "INSERT INTO Comment(CommentText, CommentTime, CommentDelete, PostingID, UserId) values(?, now(), false, ?, ?);";    
	
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
    public static String getPostingPostQuery() {
        return postingPostQuery;
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
    public static String getCommentPostQuery() {
        return commentPostQuery;
    }
}