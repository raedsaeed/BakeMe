package com.example.android.bakeme.data.adapter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe.Ingredients;
import com.example.android.bakeme.data.db.RecipeProvider;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link IngredientAdapter} is a {@link RecyclerView.Adapter} to display the ingredients for the
 * recipe in question.
 */
public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private Context ctxt;
    private ArrayList<Ingredients> ingredientsList;

    private boolean offerCheckBoxes;

    public boolean isOfferCheckBoxes() {
        return offerCheckBoxes;
    }

    public void setOfferCheckBoxes(boolean offerCheckBoxes) {
        this.offerCheckBoxes = offerCheckBoxes;
    }

    public IngredientAdapter(Context ctxt, ArrayList<Ingredients> ingredientsList) {
        this.ctxt = ctxt;
        this.ingredientsList = ingredientsList;
    }

    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        View root = LayoutInflater.from(ctxt).inflate(R.layout.ingredient_item, parent,
                false);
        root.setFocusable(true);
        return new IngredientViewHolder(root);
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.IngredientViewHolder holder, int position) {
        final Ingredients currentItem = this.ingredientsList.get(position);

        holder.ingredientTv.setText(currentItem.toString());

        if (offerCheckBoxes) {
            holder.ingredientCb.setVisibility(View.VISIBLE);
        } else {
            holder.ingredientCb.setVisibility(View.GONE);

        }

        holder.ingredientCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentValues contentValues = new ContentValues();
                if (isChecked) {
                    contentValues.put(Ingredients.INGREDIENTS_CHECKED, R.integer.is_checked);
                } else {
                    contentValues.put(Ingredients.INGREDIENTS_CHECKED, R.integer.not_checked);
                }
                //create uri referencing the recipe's id
                Uri uri = ContentUris.withAppendedId(RecipeProvider.CONTENT_URI_INGREDIENTS,
                        currentItem.getId());
                ctxt.getContentResolver().update(uri, contentValues, null, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (ingredientsList == null) return 0;
        else return ingredientsList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_tv)
        TextView ingredientTv;
        @BindView(R.id.ingredient_cb)
        CheckBox ingredientCb;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
