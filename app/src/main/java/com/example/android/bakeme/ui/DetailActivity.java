package com.example.android.bakeme.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class DetailActivity extends AppCompatActivity {

    Recipe selectedRecipe;
    OverviewFragment overviewFrag;
    FragmentManager fragMan;

    static ArrayList<Recipe.Ingredients> ingredientsList;
    static ArrayList<Recipe.Steps> stepsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent recipeIntent = getIntent();
        Timber.v("recipe Intent: %s", recipeIntent);
        if (recipeIntent != null && recipeIntent.hasExtra(MainActivity.SELECTED_RECIPE)) {
            selectedRecipe = recipeIntent.getParcelableExtra(MainActivity.SELECTED_RECIPE);
            Timber.v("ingredients test: %s", selectedRecipe.getIngredients());
        }

        getSupportActionBar().setTitle(selectedRecipe.getName());


        //instantiate lists and retrieve the provided information for each.
        ingredientsList = new ArrayList<>();
        stepsList = new ArrayList<>();

        List<Recipe.Ingredients> ingredients = selectedRecipe.getIngredients();
        if (ingredients != null) {
            ingredientsList.addAll(ingredients);
        } else {
            //TODO: Handle empty list
        }

        List<Recipe.Steps> steps = selectedRecipe.getSteps();
        if(steps != null) {
            stepsList.addAll(steps);
        } else {
            //TODO: Handle empty list
        }

        fragMan = getSupportFragmentManager();

        overviewFrag =  new OverviewFragment();
        overviewFrag.setIngredientsList(ingredientsList);
        overviewFrag.setStepsList(stepsList);

        fragMan.beginTransaction().replace(R.id.detail_fragment_container, overviewFrag).commit();
    }
}