package com.example.android.bakeme.ui;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    Recipe selectedRecipe;
    public static String RECIPE_BUNDLE = "recipe_bundle";
    FragmentManager fragMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent recipeIntent = getIntent();
        Log.v(LOG_TAG, "recipe Intent: " + recipeIntent);
        if (recipeIntent != null && recipeIntent.hasExtra(MainActivity.SELECTED_RECIPE)) {
            selectedRecipe = recipeIntent.getParcelableExtra(MainActivity.SELECTED_RECIPE);
            Log.v(LOG_TAG, "ingredients: " + selectedRecipe.getIngredients());
        }

        getSupportActionBar().setTitle(selectedRecipe.getName());

        Bundle recipeBundle = new Bundle();
        recipeBundle.putParcelable(RECIPE_BUNDLE, recipeBundle);

        OverviewFragment overviewFrag = new OverviewFragment();
        overviewFrag.setArguments(recipeBundle);

//        fragMan = getSupportFragmentManager();
//        fragMan.beginTransaction().add(R.id.ingredient_rv, overviewFrag).commit();
    }
}

