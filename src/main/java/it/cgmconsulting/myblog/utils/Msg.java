package it.cgmconsulting.myblog.utils;

public class Msg {

    // -------------------- USER
    public final static String USER_ALREADY_PRESENT = "Email or username already present";
    public final static String USERNAME_ALREADY_PRESENT = "Username already in use";
    public final static String EMAIL_ALREADY_PRESENT = "Email already in use";

    public final static String USER_SIGNUP_FIRST_STEP = "User successfully registered. Please check your email to complete the registration";
    public final static String USER_SIGNUP_SECOND_STEP = "Your email has been verified. Please log in";
    public final static String USER_HAS_DEFAULT_AUTHORITY = "This user has default authority: the authority cannot be changed until user verification mail";

    public final static String USER_HAS_SAME_AUTHORITY = "Old and new authorities are the same";
    public final static String AUTHORITY_CHANGED = "Authority successfully updated";
    public final static String INVALID_AUTHORITY = "Invalid authority name";

    public final static String MAIL_SIGNUP_SUBJECT = "MyBlog: verification email";

    // l'endpoint presente nell'email va copiato e messo su postman con metodo PATCH.
    // Se lanciato direttamente dall'email, ovvero da browser,
    // la chiamata fallisce in quando il browser permette solo chiamate GET
    public final static String MAIL_SIGNUP_BODY = "Please click here to confirm your email : http://localhost:8081/api/v0/auth/confirm/";

    public final static String ACCESS_DENIED = "YOU ARE NOT AUTHORIZED TO PERMORM THIS ACTION";

    public final static String PASSWORD_MISMATCH = "The repeated password not match";
    public final static String PWD_CHANGED = "Password successfully updated";
    public final static String PWD_INCORRECT = "Wrong Password";

    // -------------------- UPLOAD FILE
    public final static String FILE_TOO_LARGE = "File size not allowed";
    public final static String FILE_NOT_VALID_IMAGE = "Image not valid";
    public final static String FILE_INVALID_DIMENSIONS = "Invalid image width or height";
    public final static String FILE_EXTENSION_NOT_ALLOWED = "Image type not allowed";
    public final static String FILE_EXTENSION_MISSING = "Missing image type";

    // -------------------- TAG
    public final static String TAG_ALREADY_PRESENT = "Tag already present";

    // -------------------- POST
    public final static String POST_TITLE_IN_USE = "Post title already in use";
    public final static String POST_UNAUTHORIZED_ACCESS = "The post can modified only by owner";


}
