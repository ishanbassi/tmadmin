package com.bassi.tmapp.service.dto;

public class FaqItem {

    private String question;
    private String answer;
    private Boolean opened;

    public FaqItem(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.opened = true;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }
}
