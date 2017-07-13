package com.pooja.carepack.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yudiz on 23/12/15.
 */
public class ModelCountryList {



        private Integer status;
        private String message;
        private List<CountryInfo> data = new ArrayList<CountryInfo>();

        /**
         *
         * @return
         * The status
         */
        public Integer getStatus() {
            return status;
        }

        /**
         *
         * @param status
         * The status
         */
        public void setStatus(Integer status) {
            this.status = status;
        }

        /**
         *
         * @return
         * The message
         */
        public String getMessage() {
            return message;
        }

        /**
         *
         * @param message
         * The message
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         *
         * @return
         * The data
         */
        public List<CountryInfo> getCountryInfo() {
            return data;
        }

        /**
         *
         * @param data
         * The data
         */
        public void setCountryInfo(List<CountryInfo> data) {
            this.data = data;
        }



    public class CountryInfo {

        private String id;
        private String countryname;

        /**
         *
         * @return
         * The id
         */
        public String getId() {
            return id;
        }

        /**
         *
         * @param id
         * The id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         *
         * @return
         * The countryname
         */
        public String getCountryname() {
            return countryname;
        }

        /**
         *
         * @param countryname
         * The countryname
         */
        public void setCountryname(String countryname) {
            this.countryname = countryname;
        }

    }


}
