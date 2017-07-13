package com.pooja.carepack.model;

import java.util.ArrayList;
import java.util.List;

public class ModelUserTabletList {

    public Integer status;
    public String message;
    public List<UserTablet> user_tablet = new ArrayList<UserTablet>();

    public class UserTablet {
        public String id;
        public String title;
        public String userid;
        public String email_status;
        public String description;
        public String dose;
        public String number;
        public String mg;
        public String morning;
        public String lunch;
        public String night;
        public String updateddate;
    }

}