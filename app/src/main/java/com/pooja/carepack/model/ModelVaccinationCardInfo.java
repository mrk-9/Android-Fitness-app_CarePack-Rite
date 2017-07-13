package com.pooja.carepack.model;


public class ModelVaccinationCardInfo {

    public Integer status;
    public String message;
    public Info info;

    public class Info {

        public String id;
        public String userid;
        public String title;
        public String description;
        public String email_status;
        public String vaccine_date;
        public String reminder;
        public String updateddate;
    }


}

