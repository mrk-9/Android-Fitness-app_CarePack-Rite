package com.pooja.carepack.model;

import java.util.ArrayList;
import java.util.List;

public class ModelTabletList {

    public Integer status;
    public String message;
    public List<TabletList> tablet_list = new ArrayList<TabletList>();

    public class TabletList {
        public String id;
        public String title;
    }

}