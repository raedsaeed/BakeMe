package com.example.android.bakeme.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe;
import com.example.android.bakeme.data.adapter.IngredientAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * {@link OverviewFragment} is a {@link Fragment} offering the needed ingredients and steps for the
 * selected recipe.
 */
public class OverviewFragment extends Fragment {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    Recipe selectedRecipe;

    // lists for the recipe in question.
    ArrayList<Recipe.Ingredients> ingredientsList;
    ArrayList<Recipe.Steps> stepsList;

    //Adapters for displaying the ingredients and steps recipe in question.
    IngredientAdapter ingredientAdapter;

    //views
    @BindView(R.id.ingredient_rv)
    RecyclerView ingredientRv;

    public OverviewFragment() {
        //required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.overview_fragment, container, false);

        Bundle recipeBundle = new Bundle();
        selectedRecipe = recipeBundle.getParcelable(DetailActivity.RECIPE_BUNDLE);

        //instantiate lists and retrieve the provided information for each.
        ingredientsList = new ArrayList<>();
        List<Recipe.Ingredients> ingredients = selectedRecipe.getIngredients(); //TODO: list remains empty?!
        ingredientsList.addAll(ingredients);

        stepsList = new ArrayList<>();
        List<Recipe.Steps> steps = selectedRecipe.getSteps(); //TODO: list remains empty?!
        stepsList.addAll(steps);

//        //Setup Ingredient adapter
//        ingredientAdapter = new IngredientAdapter(getActivity(), ingredientsList);
//        ingredientRv.setAdapter(ingredientAdapter);
//        ingredientRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        ingredientAdapter.notifyDataSetChanged();
        return root;
    }
}
