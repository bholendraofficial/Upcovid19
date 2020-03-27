package com.piyushrai.upcovid19.Activity;

import androidx.annotation.NonNull;

public class AnswerList {
String id;
    String question_id;
    String answer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public AnswerList(String id, String question_id, String answer) {
        this.id = id;
        this.question_id = question_id;
        this.answer = answer;
    }

    @NonNull
    @Override
    public String toString() {
        return  getAnswer();
    }
}
