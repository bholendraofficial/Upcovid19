package com.piyushrai.upcovid19.Activity;

import java.io.Serializable;

public class QustnAnssDeailst implements Serializable {
    private String question_id;
    private String answer;






    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

