package com.pooja.carepack.model;

import java.util.List;

public class ModelReminder {
    public Integer status;
    public String message;
    public List<Reminder> reminder;

    public class Reminder {
        public String minute;
        public String title;
    }

}