package com.pooja.carepack.model;

import java.util.ArrayList;
import java.util.List;

public class ModelInsurance {

    public Integer status;
    public String message;
    public List<InsuranceCard> insurance_card ;

    public class InsuranceCard {
        public String id;
        public String card_type;
        public String image;
    }
}