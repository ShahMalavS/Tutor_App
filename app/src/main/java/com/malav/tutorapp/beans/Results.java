package com.malav.tutorapp.beans;

/**
 * Created by shahmalav on 09/03/17.
 */

public class Results {
    private String resultId;
    private String examId;
    private String userId;
    private String Name;
    private String marksObtained;
    private String marksTotal;
    private String examDate;
    private String topic;
    private String srNo;

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(String marksObtained) {
        this.marksObtained = marksObtained;
    }

    public String getMarksTotal() {
        return marksTotal;
    }

    public void setMarksTotal(String marksTotal) {
        this.marksTotal = marksTotal;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    @Override
    public String toString() {
        return "Results{" +
                "resultId='" + resultId + '\'' +
                ", examId='" + examId + '\'' +
                ", userId='" + userId + '\'' +
                ", Name='" + Name + '\'' +
                ", marksObtained='" + marksObtained + '\'' +
                ", marksTotal='" + marksTotal + '\'' +
                ", examDate='" + examDate + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}
