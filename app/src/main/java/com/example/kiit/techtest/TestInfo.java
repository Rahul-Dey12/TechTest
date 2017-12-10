package com.example.kiit.techtest;

/**
 * Created by KIIT on 10-12-2017.
 */

public class TestInfo {
    String testname,tchruid,tchrname;

    public TestInfo() {
    }

    public TestInfo(String testname, String tchruid, String tchrname) {
        this.testname = testname;
        this.tchruid = tchruid;
        this.tchrname = tchrname;
    }

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public String getTchruid() {
        return tchruid;
    }

    public void setTchruid(String tchruid) {
        this.tchruid = tchruid;
    }

    public String getTchrname() {
        return tchrname;
    }

    public void setTchrname(String tchrname) {
        this.tchrname = tchrname;
    }
}
