package com.piyushrai.upcovid19.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.piyushrai.upcovid19.R;
import com.piyushrai.upcovid19.UserDetail;

import org.w3c.dom.Text;

import java.util.List;

public class CaseAdapter extends RecyclerView.Adapter<CaseAdapter.CaselistHolder> {
    private final List<CaseModel> list;
    private final Context context;
    private String caseid;
    private Context contex;


    public CaseAdapter(Context context,List<CaseModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public CaseAdapter.CaselistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        return new CaselistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CaselistHolder holder, int position) {
        final CaseModel caselist = list.get(position);
        if (caselist.getCase_id() != null)
            holder.caseid.setText(caselist.getCase_id());
        else
            holder.caseid.setText("-");
        if (caselist.getFull_name() != null)
            holder.name.setText(caselist.getFull_name());
        else
            holder.name.setText("-");
         caseid = caselist.getId();

        holder.viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Intent intent = new Intent(v.getContext(),UserDetailsActivity.class);
                intent.putExtra("caseid", caseid);
                intent.putExtra("userid", caseid);
              v.getContext().startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CaselistHolder extends RecyclerView.ViewHolder {
          Button viewmore;
         TextView caseid,name,mobile_no;

        public CaselistHolder(@NonNull View itemView) {
            super(itemView);
            caseid = itemView.findViewById(R.id.caseid);
            mobile_no = itemView.findViewById(R.id.mobile_no);
            name = itemView.findViewById(R.id.name);
            viewmore = itemView.findViewById(R.id.viewmore);
        }
    }
}
