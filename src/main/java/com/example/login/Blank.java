package com.example.login;


import java.time.LocalDate;


/**

 The Blank class represents a blank document.

 A blank document can be of different types and can be assigned to an advisor.
 @author [Sykat Howlader]
 */
public class Blank {
    private int blankID;
    private long blankNumber;
    private int advisorAssigned;
    private LocalDate sellDate;
    private String blankType;

    public Blank(int blankID, long blankNumber, int advisorAssigned, LocalDate sellDate) {
        this.blankID = blankID;
        this.blankNumber = blankNumber;
        this.advisorAssigned = advisorAssigned;
        this.sellDate = sellDate;
    }
    public Blank(String type, long blankNumber) {
        this.blankType = type;
        this.blankNumber = blankNumber;
    }
    public Blank(long blankNumber, int advisorAssigned) {
        this.blankNumber = blankNumber;
        this.advisorAssigned = advisorAssigned;
    }

    public Blank(int advisorAssigned, long blankNumber, LocalDate sellDate) {
        this.advisorAssigned=advisorAssigned;
        this.blankNumber = blankNumber;
        this.sellDate=sellDate;
    }


    public int getBlankID() {
        return blankID;
    }

    public void setBlankID(int blankID) {
        this.blankID = blankID;
    }

    public long getBlankNumber() {
        return blankNumber;
    }

    public void setBlankNumber(long blankNumber) {
        this.blankNumber = blankNumber;
    }

    public int getAdvisorAssigned() {
        return advisorAssigned;
    }

    public void setAdvisorAssigned(int advisorAssigned) {
        this.advisorAssigned = advisorAssigned;
    }

    public LocalDate getSellDate() {
        return sellDate;
    }

    public void setSellDate(LocalDate sellDate) {
        this.sellDate = sellDate;
    }

    public String getBlankType() {
        return blankType;
    }
    public void setBlankType(long blankNumber) {
        this.blankType = blankType;
    }
}
