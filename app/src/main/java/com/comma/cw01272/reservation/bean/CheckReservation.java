package com.comma.cw01272.reservation.bean;

/**
 * Created by COMMA08 on 2017-11-06.
 */

public class CheckReservation {
    public String reserSeq;
    public String comSeq;
    String comName;
    String comVailableNum;
    String comTotalNum;



    public String getReserSeq() {
        return reserSeq;
    }

    public void setReserSeq(String reserSeq) {
        this.reserSeq = reserSeq;
    }

    public String getComSeq() {
        return comSeq;
    }

    public void setComSeq(String comSeq) {
        this.comSeq = comSeq;
    }


    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }


    public String getVailableNum() {
        return comVailableNum;
    }

    public void setVailableNum(String VailableNum) {
        this.comVailableNum = comVailableNum;
    }

    public String getTotalNum() {
        return comTotalNum;
    }
    public void setTotalNum(String TotalNum) {
        this.comTotalNum = comTotalNum;
    }





    public CheckReservation(String reserSeq, String comSeq, String comName, String comVailableNum, String comTotalNum ) {
        this.reserSeq = reserSeq;
        this.comSeq = comSeq;
        this.comName = comName;
        this.comVailableNum = comVailableNum;
        this.comTotalNum = comTotalNum;

    }
}
