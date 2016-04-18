package com.rohitsuratekar.NCBSinfo.helpers;

import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;


public class StatusConverter {
    private String timestamp;
    private String message;
    private String details;
    private String category;
    private int statuscode;
    private int status;

   public StatusConverter(int statuscode) {
        this.statuscode = statuscode;
        this.timestamp = new GeneralHelp().timeStamp();
        putDetails();
    }

    private void putDetails (){
        String _message;
        String _details;
        String _category;
        int _status;
        switch (statuscode){
            case StatusCodes.STATUS_OPENED:
                _message = "Application opened";
                _details = "Application opened for the first time";
                _category = StatusCodes.STATCAT_APPLICATION;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_LOGINSKIP:
                _message = "Login skipped";
                _details = "User did not register for updates";
                _category = StatusCodes.STATCAT_IMPORTANT;
                _status = StatusCodes.TYPE_FAILED;
                break;
            case StatusCodes.STATUS_SERVICESTART:
                _message = "Service Started";
                _details = "Intent is handled by background data fetching service.";
                _category = StatusCodes.STATCAT_BACKGROUND;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_DATARETRIVED:
                _message = "Data fetched from the server";
                _details = "Request to server is successful and no additional entry retrieved.";
                _category = StatusCodes.STATCAT_NETWORK;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_UNAUTHENTICATED:
                _message = "Authentication failed";
                _details = "There is problem authenticating you, please contact developers";
                _category = StatusCodes.STATCAT_NETWORK;
                _status = StatusCodes.TYPE_ERROR;
                break;
            case StatusCodes.STATUS_ALARMCALL:
                _message = "Alarm requested";
                _details = "Background services called alarm service";
                _category = StatusCodes.STATCAT_BACKGROUND;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_ALARMCHANGED:
                _message = "Alarm frequency changed";
                _details = "Alarm frequency is set.";
                _category = StatusCodes.STATCAT_BACKGROUND;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_FORBIDDEN:
                _message = "Forbidden";
                _details = "You are not authorized to access this data";
                _category = StatusCodes.STATCAT_NETWORK;
                _status = StatusCodes.TYPE_ERROR;
                break;
            case StatusCodes.STATUS_AFTERBOOT:
                _message = "Device rebooted";
                _details = "Service restarted after device booted";
                _category = StatusCodes.STATCAT_BACKGROUND;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_ONUPGRADE:
                _message = "Application upgraded";
                _details = "Service restarted after application upgraded";
                _category = StatusCodes.STATCAT_BACKGROUND;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_DAILYNOTIFICATIONS:
                _message = "Daily notifications trigger";
                _details = "Today's notifications added";
                _category = StatusCodes.STATCAT_DAILY;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_FAILED_NOTIFICATIONS:
                _message = "Sending notification failed";
                _details = "Unknown code detected while triggering notifications";
                _category = StatusCodes.STATCAT_DAILY;
                _status = StatusCodes.TYPE_FAILED;
                break;
            case StatusCodes.STATUS_EVENTNOTIFIED:
                _message = "Notification sent";
                _details = "General notification sent";
                _category = StatusCodes.STATCAT_APPLICATION;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_SET_NOTIFICATION:
                _message = "Timed notification sent";
                _details = " Timer set";
                _category = StatusCodes.STATCAT_APPLICATION;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_DAILYNOTE_RESET:
                _message = "Daily notification service restarted";
                _details = "Notification service was stopped due to changes in app or reboot";
                _category = StatusCodes.STATCAT_DAILY;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_ALARM_FAILED:
                _message = "Failed to set alarm";
                _details = "Wrong intent code was sent to alarm service";
                _category = StatusCodes.STATCAT_BACKGROUND;
                _status = StatusCodes.TYPE_FAILED;
                break;
            case StatusCodes.STATUS_REGISTRATION_STARTED:
                _message = "Registration process started";
                _details = "User accepted to receive updates and further process started";
                _category = StatusCodes.STATCAT_APPLICATION;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_USER_REGISTERED:
                _message = "GCM registration successful";
                _details = "Now user can receive GCM from moderators";
                _category = StatusCodes.STATCAT_NETWORK;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_TOPIC_SUBSCRIBED:
                _message = "Topic subscribed";
                _details = "User subscribed to topic ";
                _category = StatusCodes.STATCAT_NETWORK;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case  StatusCodes.STATUS_TOPIC_UNSUBSCRIBED:
                _message = "Topic unsubscribed";
                _details = "Use will no longer receive notifications for this topic";
                _category = StatusCodes.STATCAT_NETWORK;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_SENDFORM_FAIL:
                _message = "Error sending form data";
                _details = "Status code";
                _category = StatusCodes.STATCAT_NETWORK;
                _status = StatusCodes.TYPE_FAILED;
                break;
            case StatusCodes.STATUS_REGISTRATION_ERROR:
                _message = "Unable to register";
                _details = "Error";
                _category = StatusCodes.STATCAT_NETWORK;
                _status = StatusCodes.TYPE_ERROR;
                break;
            case StatusCodes.STATUS_GCM_RECEIVED:
                _message = "GCM received from moderator";
                _details = "Message";
                _category = StatusCodes.STATCAT_NETWORK;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;
            case StatusCodes.STATUS_PUBLIC_GCM:
                _message = "Public GCM action";
                _details = "Public message";
                _category = StatusCodes.STATCAT_BACKGROUND;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;

            case StatusCodes.STATUS_NO_TOPIC:
                _message = "Unknown GCM";
                _details = "unknwon code found";
                _category = StatusCodes.STATCAT_NETWORK;
                _status = StatusCodes.TYPE_ERROR;
                break;
            case StatusCodes.STATUS_ERROR_DELETING:
                _message = "Error deleting entry";
                _details = "Timestamp entry is not found in user's database";
                _category = StatusCodes.STATCAT_APPLICATION;
                _status = StatusCodes.TYPE_ERROR;
                break;
            case StatusCodes.STATUS_OPTIMIZED_ALARMS:
                _message = "Data fetching set to optimized settings";
                _details = "Data fetching service restarted with default settings";
                _category = StatusCodes.STATCAT_DAILY;
                _status = StatusCodes.TYPE_SUCCESSFUL;
                break;

            default:
                _message = "Unknown Code";
                _details = "Status code '"+statuscode+"' not found in internal database";
                _category= StatusCodes.STATCAT_IMPORTANT;
                _status = StatusCodes.TYPE_UNKNOWN;
                break;
        }
        this.message = _message;
        this.details = _details;
        this.category = _category;
        this.status = _status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public String getCategory() {
        return category;
    }

    public int getStatus() {
        return status;
    }
}
