package com.pooja.carepack.model;

import java.util.ArrayList;

public class ModelTeeth {

    public Integer status;
    public String message;
    public UserTeeth user_teeth;

    public class UserTeeth {
        public Teeth l;
        public Teeth r;
        public Teeth f;
    }

    public class Teeth {
        public String id;
        public String image;
        public ArrayList<TeethDetail> details;
    }

    public class TeethDetail {
        public String id;
        public String title;
        public String description;
    }
}