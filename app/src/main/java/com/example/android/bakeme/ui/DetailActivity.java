package com.example.android.bakeme.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe;

import timber.log.Timber;

public class DetailActivity extends AppCompatActivity {

    Recipe selectedRecipe;
    OverviewFragment overviewFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent recipeIntent = getIntent();
        Timber.v("recipe Intent: %s", recipeIntent);
        if (recipeIntent != null && recipeIntent.hasExtra(MainActivity.SELECTED_RECIPE)) {
            selectedRecipe = recipeIntent.getParcelableExtra(MainActivity.SELECTED_RECIPE);
            Timber.v("ingredients: %s", selectedRecipe.getIngredients());
        }

        getSupportActionBar().setTitle(selectedRecipe.getName());

        overviewFrag = new OverviewFragment();
        overviewFrag.setSelectedRecipe(selectedRecipe);
    }
}