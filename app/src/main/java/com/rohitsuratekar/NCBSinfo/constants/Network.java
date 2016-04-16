package com.rohitsuratekar.NCBSinfo.constants;

public class Network {


    public static final String NET_CBJC_TABLEID = "TABLEID_for_CBJC"; //CBJC
    public static final String NET_DBJC_TABLEID = "TABLEID_for_DBJC"; //DBJC
    public static final String NET_RESEARCH_TALK_TABLEID = "TABLEID_for_TALKS"; //Research Talks
    public static final String NET_KEY = "YOUR_API_KEY"; //This is public API Key

    //Intent switches
    public static final String NET_START_FETCHING = "startFetch";

    //GCM
    public static final String GCM_PROJECT_ID = "PROJECTID";
    public static final String GCM_SCOPE = "GCM";
    public static final String GCM_CODE = "CODE";
    public static final String GCM_TOPIC_RESEARCHTALK = "TOPIC1";
    public static final String GCM_TOPIC_JC = "TOPIC2";
    public static final String GCM_TOPIC_STUDENT_ACTIVITY = "TOPIC3";
    public static final String GCM_TOPIC_URGENT = "TOPIC4";
    public static final String GCM_TOPIC_PUBLIC = "TOPIC5";


    //GCM Triggers
    public static final String GCM_TRIGGER_DATAFETCH = "DataFetch";
    public static final String GCM_TRIGGER_NEW_UPDATE= "NewUpdate";
    public static final String GCM_TRIGGER_DELETEENTRY= "DeleteEntry";

}
