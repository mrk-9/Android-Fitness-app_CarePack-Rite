package com.pooja.carepack.model;

/**
 * Created by Yudiz on 24/12/15.
 */
public class ModelBodySettings {


    public Integer status;
    public String message;
    public BodySettings body_settings;

    public class BodySettings {

        public String id;
        public String userid;
        public String height;
        public String weight;
        public String arm_left;//armLeft;
        public String arm_right;
        public String neck;
        public String body_fat_percent;
        public String waist;
        public String chest;
        public String hip;
        public String abdominal;
        public String thigh_left;
        public String thigh_right;
        public String calf_left;
        public String calf_right;
        public String dress_size;
        public String updateddate;
    }
}
