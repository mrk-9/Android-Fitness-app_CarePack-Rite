package com.pooja.carepack.activities;

/**
 * Created by Vinay Rathod on 22/12/15.
 */
public class WebServicesConst {

    //    public static final String ADD_EDIT_USER_VACCINATION_LIST = BASE_URL + "user_vaccination";
    public static final String URL_IMPRINT = "http://bottle.ninja/php/codeigniter/carepack/page/imprint/en";//"http://bottle.ninja/carepack/page/imprint/en";
    public static final String URL_BMI_DESC = "http://bottle.ninja/php/codeigniter/carepack/page/bmi_description/en";//"http://bottle.ninja/carepack/page/bmi_description/en";
    public static final String URL_HEART_RATE = "http://bottle.ninja/php/codeigniter/carepack/page/heart_rate_desc/en";//http://bottle.ninja/carepack/page/heart_rate_desc/en";
    private static final String BASE_URL = "http://bottle.ninja/php/codeigniter/carepack/api/";//"http://bottle.ninja/carepack/api/";
    // profile
    public static final String LOGIN = BASE_URL + "login";
    public static final String LOGOUT = BASE_URL + "logout";
    public static final String FORGET_URL = BASE_URL + "forgot_password";
    public static final String UPDATE_PROFILE = BASE_URL + "update_profile";
    public static final String USER_PROFILE = BASE_URL + "user_profile";
    public static final String UPDATE_PROFILE_IMAGE = BASE_URL + "profile_image";
    public static final String COUNTRY_LIST = BASE_URL + "country_list";
    public static final String REGISTER = BASE_URL + "register";
    public static final String CHANGE_PASSWORD = BASE_URL + "change_password";

    //    setting
    public static final String BODY_SETTING = BASE_URL + "body_settings";
    public static final String FAMILY_DOCTOR_INFO = BASE_URL + "family_doctor";
    public static final String KINSHIP = BASE_URL + "kinship";
    public static final String WHATSAPP_INFO = BASE_URL + "whatsapp_details";

    // vaccination
    public static final String VACCINATION_LIST = BASE_URL + "get_vaccination_list";
    public static final String USER_VACCINATION_LIST = BASE_URL + "user_vaccination";

    //medical finding
    public static final String MEDICAL_FINDING = BASE_URL + "medical_findings";

    //tablets
    public static final String USER_TABLE_REGULATION = BASE_URL + "user_tablet";
    public static final String TABLE_REGULATION = BASE_URL + "get_tablet_list";

    // insurance
    public static final String INSURANCE_CARD = BASE_URL + "insurance_card";

    //teeth
    public static final String TEETH = BASE_URL + "teeth";


    //treatment_plan
    public static final String TREATMENT_PLAN = BASE_URL + "treatment_plan";
    public static final String TREATMENT_REMINDER = BASE_URL + "get_reminder";

    //message_list
    public static final String MESSAGE_LIST = BASE_URL + "message_list";

    //get_users
    public static final String GET_USER = BASE_URL + "get_users";

    //friend_status
    public static final String FRIEND_STATUS = BASE_URL + "friend_status";

    public static class RES {
        public static final int LOGIN = 0;
        public static final int UPDATE_PROFILE = 1;
        public static final int COUNTRY_LIST = 2;
        public static final int REGISTER = 3;
        public static final int UPDATE_BODY_SETTING = 4;
        public static final int UPDATE_FAMILY_DOCTOR_INFO = 5;
        public static final int UPDATE_KINSHIP = 6;
        public static final int GET_BODY_SETTING = 7;
        public static final int GET_FAMILY_DOCTOR_INFO = 8;
        public static final int GET_KINSHIP = 9;
        public static final int UPDATE_WHATSAPP_INFO = 10;
        public static final int GET_WHATSAPP_INFO = 11;
        public static final int GET_USER_PROFILE = 12;
        public static final int GET_VACCINATION_LIST = 13;
        public static final int GET_VACCINATION_INFO = 14;
        public static final int GET_USER_VACCINATION_LIST = 15;
        public static final int ADD_EDIT_USER_VACCINATION_LIST = 16;
        public static final int EDIT_EMAIL_STAUS = 17;
        public static final int DELETE_USER_VACCINATION = 18;
        public static final int GET_USER_VACCINATION_INFO = 19;
        public static final int FORGET_URL = 20;
        public static final int ADD_MEDICALFINDING_INFO = 21;
        public static final int GET_MEDICALFINDING = 22;
        public static final int EDIT_MEDICALFINDING_INFO = 23;
        public static final int DELETE_MEDICALFINDING_INFO = 24;
        public static final int UPDATE_PROFILE_IMAGE = 25;
        public static final int LOGOUT = 26;
        public static final int GET_USER_TABLE_REGULATION = 27;
        public static final int GET_TABLE_REGULATION = 28;
        public static final int ADD_TABLE_REGULATION = 29;
        public static final int DELETE_TABLE_REGULATION = 30;
        public static final int GET_INSURANCE_CARD = 31;
        public static final int DELETE_INSURANCE_CARD = 32;
        public static final int CHANGE_PASSWORD = 33;
        public static final int DELETE_TEETH_DETAIL = 34;
        public static final int TREATMENT_PLAN = 35;
        public static final int TREATMENT_ADD = 36;
        public static final int TREATMENT_GET = 37;
        public static final int TREATMENT_DELETE = 38;
        public static final int TREATMENT_REMINDER = 39;
        public static final int MESSAGE_LIST = 40;
        public static final int GET_USER = 41;
        public static final int FRIEND_STATUS = 42;

    }

}
