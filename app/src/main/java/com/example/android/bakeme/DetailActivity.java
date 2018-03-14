package com.example.android.bakeme;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.example.android.bakeme.data.Recipe;
import com.example.android.bakeme.data.Recipe.Ingredients;
import com.example.android.bakeme.data.Recipe.Steps;
import com.example.android.bakeme.data.adapter.IngredientAdapter;
import com.example.android.bakeme.databinding.RecipeDetailBinding;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    RecipeDetailBinding recipeBinder;
    Recipe selectedRecipe;

    // lists for the recipe in question.
    ArrayList<Ingredients> ingredientsList;
    ArrayList<Steps> stepsList;

    //Adapters for displaying the ingredients and steps recipe in question.
    IngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeBinder = DataBindingUtil.setContentView(this, R.layout.recipe_detail);

        Intent recipeIntent = getIntent();

        if(recipeIntent != null && recipeIntent.hasExtra(MainActivity.SELECTED_RECIPE)) {
            selectedRecipe = recipeIntent.getParcelableExtra(MainActivity.SELECTED_RECIPE);
            Log.v(LOG_TAG, "ingredients: " + selectedRecipe.getIngredients());
        }

        //instantiate lists and retrieve the provided information for each.
        ingredientsList = new ArrayList<>();
        List<Ingredients> ingredients = selectedRecipe.getIngredients(); //TODO: list remains empty?!
        ingredientsList.addAll(ingredients);

        stepsList = new ArrayList<>();
        List<Steps> steps = selectedRecipe.getSteps();
        stepsList.addAll(steps);

        //Setup Ingredient adapter
        ingredientAdapter = new IngredientAdapter(this, ingredientsList);
        recipeBinder.ingredientRv.setAdapter(ingredientAdapter);
        recipeBinder.ingredientRv.setLayoutManager(new GridLayoutManager(this, 2));
        ingredientAdapter.notifyDataSetChanged();

    }
}
