package com.example.kiit.techtest;

/**
 * Created by KIIT on 09-12-2017.
 */

public class AllQA {
    String question,an1,ans2,ans3,ans4,rgans;

    public AllQA() {
    }

    public AllQA(String question, String an1, String ans2, String ans3, String ans4, String rgans) {
        this.question = question;
        this.an1 = an1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
        this.rgans = rgans;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAn1() {
        return an1;
    }

    public void setAn1(String an1) {
        this.an1 = an1;
    }

    public String getAns2() {
        return ans2;
    }

    public void setAns2(String ans2) {
        this.ans2 = ans2;
    }

    public String getAns3() {
        return ans3;
    }

    public void setAns3(String ans3) {
        this.ans3 = ans3;
    }

    public String getAns4() {
        return ans4;
    }

    public void setAns4(String ans4) {
        this.ans4 = ans4;
    }

    public String getRgans() {
        return rgans;
    }

    public void setRgans(String rgans) {
        this.rgans = rgans;
    }
}
