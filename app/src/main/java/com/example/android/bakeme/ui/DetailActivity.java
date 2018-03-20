package com.example.android.bakeme.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

    public static final String SELECTED_STEP = "selected_step";
    public static final String STEP_ARRAY_SIZE = "number of steps";

    public static boolean twoPane;

    static ArrayList<Recipe.Ingredients> ingredientsList;
    static ArrayList<Steps> stepsList;

    private String RECIPE_DETAIL = "detail recipe stack";
    private String RECIPE_METHOD = "method recipe stack";
    public static final String INGREDIENT_LIST= "ingredient_list";
    public static final String STEP_LIST = "step_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MainActivity.SELECTED_RECIPE)) {
                selectedRecipe = savedInstanceState.getParcelable(MainActivity.SELECTED_RECIPE);
            }
            if (savedInstanceState.containsKey(INGREDIENT_LIST)) {
                ingredientsList = savedInstanceState.getParcelableArrayList(INGREDIENT_LIST);
            }
            if ( savedInstanceState.containsKey(STEP_LIST)) {
                stepsList = savedInstanceState.getParcelableArrayList(STEP_LIST);
            }
        }

        twoPane = false; //TODO: Implement phone <-> tablet recognition

        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);

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
        ingredientsList.addAll(ingredients);

        List<Steps> steps = selectedRecipe.getSteps();
        stepsList.addAll(steps);

        fragMan = getSupportFragmentManager();

        overviewFrag = new OverviewFragment();
        overviewFrag.setIngredientsList(ingredientsList);
        overviewFrag.setStepsList(stepsList);

        fragMan.beginTransaction().replace(R.id.detail_fragment_container, overviewFrag)
                .addToBackStack(RECIPE_DETAIL).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MainActivity.SELECTED_RECIPE, selectedRecipe);
        outState.putParcelableArrayList(INGREDIENT_LIST, ingredientsList);
        outState.putParcelableArrayList(STEP_LIST, stepsList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(Steps step) {
        if (twoPane) {
            // tablet layout communication
        } else {
            methodFrag = new MethodFragment();
            methodFrag.setStep(step);
            methodFrag.setRecipe(selectedRecipe);
            methodFrag.setStepsList(stepsList);
            fragMan.beginTransaction().replace(R.id.detail_fragment_container, methodFrag)
                    .addToBackStack(RECIPE_METHOD).commit();
        }
    }
}