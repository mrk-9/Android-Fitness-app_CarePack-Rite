package com.pooja.carepack.model;

public class ModelWhatsappInfo {

    private Integer status;
    private String message;
    private Whatsapp whatsapp;

    /**
     * @return The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The whatsapp
     */
    public Whatsapp getWhatsapp() {
        return whatsapp;
    }

    /**
     * @param whatsapp The whatsapp
     */
    public void setWhatsapp(Whatsapp whatsapp) {
        this.whatsapp = whatsapp;
    }


    public class Whatsapp {

        private String id;
        private String userid;
        private String notification;
        private String whatsapp_number;
        private String updateddate;

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
         * The userid
         */
        public String getUserid() {
            return userid;
        }

        /**
         *
         * @param userid
         * The userid
         */
        public void setUserid(String userid) {
            this.userid = userid;
        }

        /**
         *
         * @return
         * The notification
         */
        public String getNotification() {
            return notification;
        }

        /**
         *
         * @param notification
         * The notification
         */
        public void setNotification(String notification) {
            this.notification = notification;
        }

        /**
         *
         * @return
         * The whatsappNumber
         */
        public String getWhatsappNumber() {
            return whatsapp_number;
        }

        /**
         *
         * @param whatsappNumber
         * The whatsapp_number
         */
        public void setWhatsappNumber(String whatsappNumber) {
            this.whatsapp_number = whatsappNumber;
        }

        /**
         *
         * @return
         * The updateddate
         */
        public String getUpdateddate() {
            return updateddate;
        }

        /**
         *
         * @param updateddate
         * The updateddate
         */
        public void setUpdateddate(String updateddate) {
            this.updateddate = updateddate;
        }

    }

}