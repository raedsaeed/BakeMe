package com.example.android.bakeme.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe;
import com.example.android.bakeme.data.Recipe.Steps;
import com.example.android.bakeme.data.adapter.StepAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import timber.log.Timber;

public class DetailActivity extends AppCompatActivity implements StepAdapter.StepClickHandler {

    Recipe selectedRecipe;
    OverviewFragment overviewFrag;
    MethodFragment methodFrag;
    FragmentManager fragMan;

    //booleans to track layout and favouriting
    public static boolean twoPane;
    public static boolean isFavourited;

    Menu menu;

    static ArrayList<Recipe.Ingredients> ingredientsList;
    static ArrayList<Steps> stepsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        twoPane = findViewById(R.id.detail_fragment_container2) != null;

        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);

        //instantiate lists ready to retrieve the provided information for each.
        ingredientsList = new ArrayList<>();
        stepsList = new ArrayList<>();

        fragMan = getSupportFragmentManager();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(String.valueOf(R.string.SELECTED_RECIPE))) {
                selectedRecipe = savedInstanceState.getParcelable(String.valueOf(R.string.SELECTED_RECIPE));
            }
            if (savedInstanceState.containsKey(String.valueOf(R.string.INGREDIENT_LIST))) {
                ingredientsList = savedInstanceState.getParcelableArrayList(String.valueOf(R.string.INGREDIENT_LIST));
            }
            if (savedInstanceState.containsKey(String.valueOf(R.string.STEP_LIST))) {
                stepsList = savedInstanceState.getParcelableArrayList(String.valueOf(R.string.STEP_LIST));
            }

        } else {
            Intent recipeIntent = getIntent();
            Timber.v("recipe Intent: %s", recipeIntent);
            if (recipeIntent != null && recipeIntent.hasExtra(String.valueOf(R.string.SELECTED_RECIPE))) {
                selectedRecipe = recipeIntent.getParcelableExtra(String.valueOf(R.string.SELECTED_RECIPE));
                Timber.v("ingredients test: %s", selectedRecipe.getIngredients());
            }

            List<Recipe.Ingredients> ingredients = selectedRecipe.getIngredients();
            ingredientsList.addAll(ingredients);

            List<Steps> steps = selectedRecipe.getSteps();
            stepsList.addAll(steps);

            overviewFrag = new OverviewFragment();
            methodFrag = new MethodFragment();
        }

        getSupportActionBar().setTitle(selectedRecipe.getName());

        overviewFrag = new OverviewFragment();
        overviewFrag.setIngredientsList(ingredientsList);
        overviewFrag.setStepsList(stepsList);

        fragMan.beginTransaction().add(R.id.detail_fragment_container1, overviewFrag)
                .addToBackStack(null).commit();

        if (twoPane) {
            methodFrag = new MethodFragment();
            methodFrag.setStep(stepsList.get(0));
            methodFrag.setRecipe(selectedRecipe);
            methodFrag.setStepsList(stepsList);
            //methodFrag.setTwoPane(twoPane);

            fragMan.beginTransaction().add(R.id.detail_fragment_container2, methodFrag)
                    .addToBackStack(null).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(String.valueOf(R.string.SELECTED_RECIPE), selectedRecipe);
        outState.putParcelableArrayList(String.valueOf(R.string.INGREDIENT_LIST), ingredientsList);
        outState.putParcelableArrayList(String.valueOf(R.string.STEP_LIST), stepsList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(Steps step) {

        if (twoPane) {
            methodFrag.setStep(step);
            methodFrag.setRecipe(selectedRecipe);
            methodFrag.setStepsList(stepsList);
            //methodFrag.setTwoPane(twoPane);

            fragMan.beginTransaction().replace(R.id.detail_fragment_container2, methodFrag)
                    .addToBackStack(null).commit();
        } else {
            Intent openMethod = new Intent(this, MethodActivity.class);
            Bundle recipeBundle = new Bundle();

            recipeBundle.putParcelableArrayList(String.valueOf(R.string.STEP_LIST), stepsList);
            recipeBundle.putParcelable(String.valueOf(R.string.SELECTED_RECIPE), selectedRecipe);
            recipeBundle.putParcelable(String.valueOf(R.string.SELECTED_STEP), step);

            openMethod.putExtra(String.valueOf(R.string.RECIPE_BUNDLE), recipeBundle);
            startActivity(openMethod);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        getMenuInflater().inflate(R.menu.favourite_bt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.favourite_menu) {
            if (isFavourited) {
                isFavourited = false;
                menu.getItem(0).setIcon(AppCompatResources
                        .getDrawable(this, android.R.drawable.btn_star_big_off));
            } else {
                isFavourited = true;
                menu.getItem(0).setIcon(AppCompatResources
                        .getDrawable(this, android.R.drawable.btn_star_big_on));
            }
        }
        return super.onOptionsItemSelected(item);
    }
}