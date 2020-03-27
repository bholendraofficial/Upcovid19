package com.piyushrai.upcovid19.Model;

public class AnswersItem{
	private String answer;
	private String id;
	private String questionId;
	private boolean isSelected=false;

	AnswersItem(String answer,String id,String questionId)
	{
		setAnswer(answer);
		setId(id);
		setQuestionId(questionId);
	}

	public void setAnswer(String answer){
		this.answer = answer;
	}

	public String getAnswer(){
		return answer;
	}

	public boolean isSelected(){
		return isSelected;
	}

	public void setSelected(boolean isSelected){
		this.isSelected=isSelected;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setQuestionId(String questionId){
		this.questionId = questionId;
	}

	public String getQuestionId(){
		return questionId;
	}

	@Override
 	public String toString(){
		return 
			"AnswersItem{" + 
			"answer = '" + answer + '\'' + 
			",id = '" + id + '\'' + 
			",question_id = '" + questionId + '\'' + 
			"}";
		}
}
