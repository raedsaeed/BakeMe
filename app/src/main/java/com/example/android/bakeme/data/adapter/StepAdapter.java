package com.example.android.bakeme.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe.Steps;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link StepAdapter} is a {@link RecyclerView.Adapter} to display the ingredients for the
 * recipe in question.
 */
public class StepAdapter
        extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    Context ctxt;
    ArrayList<Steps> stepsList;

    public StepAdapter(Context ctxt, ArrayList<Steps> stepsList) {
        this.ctxt = ctxt;
        this.stepsList = stepsList;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View root = LayoutInflater.from(ctxt).inflate(R.layout.recipe_step_shortdescrip, parent,
                false);
        root.setFocusable(true);
        return new StepViewHolder(root);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Steps currentItem = this.stepsList.get(position);

        if (currentItem.getId() == 0) { // don't show step "0" number
            holder.stepNoTv.setVisibility(View.INVISIBLE);
        } else {
            holder.stepNoTv.setText(String.valueOf(currentItem.getId()));
        }

        holder.recipeTextTv.setText(currentItem.getShortdescription());

    }

    @Override
    public int getItemCount() {
        if (stepsList == null) return 0;
        else return stepsList.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_text_tv)
        TextView recipeTextTv;
        @BindView(R.id.stepNo_tv)
        TextView stepNoTv;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
