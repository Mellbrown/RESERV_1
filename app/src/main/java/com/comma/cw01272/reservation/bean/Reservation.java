package com.comma.cw01272.reservation.bean;

/**
 * Created by COMMA08 on 2017-11-06.
 */

public class Reservation {
    public String comSeq;
    public String comName;
    public String cominfo;
    public String comVailableNum;
    public String comTotalNum;
    public String comImg;


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

    public String getComInfo() {
        return cominfo;
    }

    public void setComInfo(String info) {
        this.cominfo = info;
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

    public String getComImg() {
        return comTotalNum;
    }

    public void setComImg(String ComImg) {
        this.comImg = comImg;
    }

    public Reservation(String comSeq,String comName, String cominfo, String comVailableNum, String comTotalNum, String comImg) {
        this.comSeq = comSeq;
        this.comName = comName;
        this.cominfo = cominfo;
        this.comVailableNum = comVailableNum;
        this.comTotalNum = comTotalNum;
        this.comImg = comImg;
    }
}
