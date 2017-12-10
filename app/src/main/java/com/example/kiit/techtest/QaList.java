package com.example.kiit.techtest;

/**
 * Created by KIIT on 07-12-2017.
 */

public class QaList {

    String testname,testdes,totalqstn;

    public QaList() {
    }

    public QaList(String testname, String testdes, String totalqstn) {
        this.testname = testname;
        this.testdes = testdes;
        this.totalqstn = totalqstn;
    }

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public String getTestdes() {
        return testdes;
    }

    public void setTestdes(String testdes) {
        this.testdes = testdes;
    }

    public String getTotalqstn() {
        return totalqstn;
    }

    public void setTotalqstn(String totalqstn) {
        this.totalqstn = totalqstn;
    }
}
