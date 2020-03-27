package com.piyushrai.upcovid19.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionsItem{
	private String question;
	private List<AnswersItem> answers;
	private String id;

	public QuestionsItem(JSONObject jsonObject){
		setId(jsonObject.optString("id"));
		setQuestion(jsonObject.optString("question"));
		try {
			List<AnswersItem> answers =new ArrayList<>();
			JSONArray jsonArray=jsonObject.getJSONArray("answers");
			for (int i=0;i<jsonArray.length();i++)
			{
				String answer=jsonArray.getJSONObject(i).getString("answer");
				String question_id=jsonArray.getJSONObject(i).getString("question_id");
				String id=jsonArray.getJSONObject(i).getString("id");
				answers.add(new AnswersItem(answer,id,question_id));
				setAnswers(answers);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void setQuestion(String question){
		this.question = question;
	}

	public String getQuestion(){
		return question;
	}

	public void setAnswers(List<AnswersItem> answers){
		this.answers = answers;
	}

	public List<AnswersItem> getAnswers(){
		return answers;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"QuestionsItem{" + 
			"question = '" + question + '\'' + 
			",answers = '" + answers + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}