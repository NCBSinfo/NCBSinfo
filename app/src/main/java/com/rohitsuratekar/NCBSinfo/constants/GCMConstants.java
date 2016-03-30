package com.rohitsuratekar.NCBSinfo.constants;

public class GCMConstants {
    public static final String GCM_API_KEY = "ApiKey";
    public static final String GCM_TOPIC_CODE = "currentTopicCode";
    public static final String GCM_MAX_DAILYQUOTA = "currentMaxDailyQuota";
    public static final String GCM_USER_EXTRA = "currentuserExtra";
    public static final String GCM_USER_LOGTABLE = "moderatorLogTable";

    //Moderation Login
    public static final String MOD_LOGINURL = "https://accounts.google.com/o/oauth2/v2/auth";
    public static final String MOD_SCOPE = "https://www.googleapis.com/auth/fusiontables.readonly https://www.googleapis.com/auth/userinfo.email";
    public static final String MOD_CLIENT_ID = "7622CC1C3299194ACE6790A5C629E4109AA71E448C6DFC2F3BD5836A2D6B4DA774101208A854880C7CDB92A5F4620214B187411E35F91FBFAEF2E315DF7A2766BEBC3C23A44D1B777EABC7A87F03F1D3"; //This is from separate account
    public static final String MOD_CLIENT_SECRET = "AB9968AA31B79820A4573D03AE3E7661129B5008F169FE374000431F8962D211";  //This is from separate account
    public static final String MOD_REDIRECT_URI = "http://rohitsuratekar.co.in/auth/";
    public static final String DECODED_CLIENTID = "clientID";
    public static final String DECODED_SECRET = "clientSecret";
    public static final String MOD_EMAIL = "moderatorEmail";
    public static final String MOD_USERNAME = "moderatorUsername";


    public static final String GOOGLE_PROJECT_ID = "195519987632";
    public static final String GCMscope = "GCM";
    public static final String GCM_TOPIC_TALK = "research_talk";
    public static final String GCM_TOPIC_jc = "journal_clubs";
    public static final String GCM_TOPIC_student = "student_activity";
    public static final String GCM_TOPIC_EMERGENCY = "emergency";

    public static final String START_FLAG = "StartFLAG";
    public static final String FLAG_CODE = "FLAGCode";

    public static final String DATA_Registered = "AlreadyRegistered";
    public static final String DATA_REG_ID = "gcmRegistrationID";
    public static final String DATA_USERNAME = "currentUserName";
    public static final String DATA_EMAIL = "currentEmail";
    public static final String DATA_TALK = "researchTalk";
    public static final String DATA_JC = "journalClubs";
    public static final String DATA_students = "studentActivity";
    public static final String DATA_MODERATORLOGIN = "isModerator";
    public static final String DATA_ACCESS_TOKEN = "AccessToken";
    public static final String DATA_REFRESH_TOKEN = "RefreshToken";


    //FusionTable Values
    public static final String TABLE_ID = "1_0w9qHdrIwhw61BpVJ5mbB6MMFM0K7BqAJICYlAC";
}
