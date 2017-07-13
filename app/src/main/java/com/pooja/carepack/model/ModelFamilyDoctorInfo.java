package com.pooja.carepack.model;

public class ModelFamilyDoctorInfo {

    private Integer status;
    private String message;
    private FamilyDoctor family_doctor;

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
     * @return The familyDoctor
     */
    public FamilyDoctor getFamilyDoctor() {
        return family_doctor;
    }

    /**
     * @param familyDoctor The family_doctor
     */
    public void setFamilyDoctor(FamilyDoctor familyDoctor) {
        this.family_doctor = familyDoctor;
    }


    public class FamilyDoctor {

        private String id;
        private String userid;
        private String firstname;
        private String lastname;
        private String email;
        private String emergency;
        private String address;
        private String phone;
        private String send_gps;
        private String createddate;

        /**
         * @return The id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return The userid
         */
        public String getUserid() {
            return userid;
        }

        /**
         * @param userid The userid
         */
        public void setUserid(String userid) {
            this.userid = userid;
        }

        /**
         * @return The firstname
         */
        public String getFirstname() {
            return firstname;
        }

        /**
         * @param firstname The firstname
         */
        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        /**
         * @return The lastname
         */
        public String getLastname() {
            return lastname;
        }

        /**
         * @param lastname The lastname
         */
        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        /**
         * @return The email
         */
        public String getEmail() {
            return email;
        }

        /**
         * @param email The email
         */
        public void setEmail(String email) {
            this.email = email;
        }

        /**
         * @return The emergency
         */
        public String getEmergency() {
            return emergency;
        }

        /**
         * @param emergency The emergency
         */
        public void setEmergency(String emergency) {
            this.emergency = emergency;
        }

        /**
         * @return The address
         */
        public String getAddress() {
            return address;
        }

        /**
         * @param address The address
         */
        public void setAddress(String address) {
            this.address = address;
        }

        /**
         * @return The phone
         */
        public String getPhone() {
            return phone;
        }

        /**
         * @param phone The phone
         */
        public void setPhone(String phone) {
            this.phone = phone;
        }

        /**
         * @return The sendGps
         */
        public String getSendGps() {
            return send_gps;
        }

        /**
         * @param sendGps The send_gps
         */
        public void setSendGps(String sendGps) {
            this.send_gps = sendGps;
        }

        /**
         * @return The createddate
         */
        public String getCreateddate() {
            return createddate;
        }

        /**
         * @param createddate The createddate
         */
        public void setCreateddate(String createddate) {
            this.createddate = createddate;
        }

    }

}
