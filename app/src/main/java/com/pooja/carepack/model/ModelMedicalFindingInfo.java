package com.pooja.carepack.model;

import java.util.List;

public class ModelMedicalFindingInfo {

    public Integer status;
    public String message;
    public Info info;
    public List<Images> images;

    public class Info {
        public String id;
        public String title;
        public String finding_date;
        public String email_status;
        public String description;
        public String hospital;
        public String doctor;
        public String doctor_phone;
        public String doctor_email;
        public String createddate;
    }

    public class Images {
        public String id;
        public String image;
        public String image_512;
        public boolean isLocal;
    }
}
