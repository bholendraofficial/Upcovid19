package com.piyushrai.upcovid19.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.piyushrai.upcovid19.Activity.UserDetailsActivity;
import com.piyushrai.upcovid19.Model.QuestionsItem;
import com.piyushrai.upcovid19.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionAnswerAdapter extends RecyclerView.Adapter<QuestionAnswerAdapter.MyViewHolder> {

    private List<QuestionsItem> answerList;
    public UserDetailsActivity activity;

    public QuestionAnswerAdapter(UserDetailsActivity activity, List<QuestionsItem> answerList) {
        this.activity=activity;
        this.answerList = answerList;
        this.answerList = answerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_answer, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        QuestionsItem answer = answerList.get(position);
        holder.tv_name.setText(answer.getQuestion());
        ArrayList<String> spinnerItems=new ArrayList<>();
        for (int i=0;i<answer.getAnswers().size();i++)
        {
            spinnerItems.add(answer.getAnswers().get(i).getAnswer());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, spinnerItems);
        holder.sp_spinner.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        Spinner sp_spinner;
        public MyViewHolder(View view) {
            super(view);
            tv_name=view.findViewById(R.id.tv_name);
            sp_spinner=view.findViewById(R.id.sp_spinner);
            sp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                   try {
                       String isYes = parent.getItemAtPosition(pos).toString();
                       if (isYes.equalsIgnoreCase("yes"))
                       {
                           answerList.get(getAdapterPosition()).getAnswers().get(pos).setSelected(true);
                       }else
                       {
                           answerList.get(getAdapterPosition()).getAnswers().get(pos).setSelected(false);
                       }

                   }catch (Exception ex)
                   {
                    ex.printStackTrace();
                   }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}