package com.example.android.bakeme.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe;
import com.example.android.bakeme.data.Recipe.Ingredients;
import com.example.android.bakeme.data.Recipe.Steps;
import com.example.android.bakeme.data.adapter.StepAdapter;
import com.example.android.bakeme.data.db.RecipeProvider;

import java.util.ArrayList;

import butterknife.ButterKnife;
import timber.log.Timber;

public class DetailActivity extends AppCompatActivity implements StepAdapter.StepClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    Recipe selectedRecipe;
    OverviewFragment overviewFrag;
    MethodFragment methodFrag;
    FragmentManager fragMan;

    //booleans to track layout
    public static boolean twoPane;
    private boolean isFavourited;

    //Loader constants
    private static final int RECIPE_LOADER = 1;
    private static final int INGREDIENTS_LOADER = 2;
    private static final int STEPS_LOADER = 3;

    Menu menu;

    static ArrayList<Ingredients> ingredientsList;
    static ArrayList<Steps> stepsList;
    protected int amountOfLoaders = 3;
    protected int completedLoaders = 0;

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
                selectedRecipe = savedInstanceState.getParcelable(String
                        .valueOf(R.string.SELECTED_RECIPE));
            }
            if (savedInstanceState.containsKey(String.valueOf(R.string.INGREDIENT_LIST))) {
                ingredientsList = savedInstanceState.getParcelableArrayList(String
                        .valueOf(R.string.INGREDIENT_LIST));
            }
            if (savedInstanceState.containsKey(String.valueOf(R.string.STEP_LIST))) {
                stepsList = savedInstanceState.getParcelableArrayList(String
                        .valueOf(R.string.STEP_LIST));
            }

        } else {
            Intent recipeIntent = getIntent();
            Timber.v("recipe Intent: %s", recipeIntent);
            if (recipeIntent != null
                    && recipeIntent.hasExtra(String.valueOf(R.string.SELECTED_RECIPE))) {
                selectedRecipe
                        = recipeIntent.getParcelableExtra(String.valueOf(R.string.SELECTED_RECIPE));
            }

            overviewFrag = new OverviewFragment();
            methodFrag = new MethodFragment();
        }

        getSupportLoaderManager().initLoader(RECIPE_LOADER, null, this);

        getSupportLoaderManager().initLoader(INGREDIENTS_LOADER, null, this);

        getSupportLoaderManager().initLoader(STEPS_LOADER, null, this);

        getSupportActionBar().setTitle(selectedRecipe.getName());

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

    // see: https://stackoverflow.com/a/11421298/7601437
    public void loaderHasFinished() {
        completedLoaders++;
        if (completedLoaders == amountOfLoaders) {
            setupFragments();
            completedLoaders = 0;
        }
    }

    private void setupFragments() {
        overviewFrag = new OverviewFragment();
        overviewFrag.setIngredientsList(ingredientsList);
        overviewFrag.setStepsList(stepsList);
        overviewFrag.setFavourited(isFavourited);
        overviewFrag.setSelectedRecipe(selectedRecipe);

        fragMan.beginTransaction().add(R.id.detail_fragment_container1, overviewFrag)
                .addToBackStack(null).commit();

        if (twoPane) {
            methodFrag = new MethodFragment();
            methodFrag.setStep(stepsList.get(0));
            methodFrag.setRecipe(selectedRecipe);
            methodFrag.setStepsList(stepsList);

            fragMan.beginTransaction().add(R.id.detail_fragment_container2, methodFrag)
                    .addToBackStack(null).commit();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri uri = null;
        String[] projection = new String[0];
        String selection = null;
        String[] selectionArgs = new String[0];
        long selectedRecipeId = selectedRecipe.getId();
        switch (id) {
            case RECIPE_LOADER:
                uri = RecipeProvider.CONTENT_URI_RECIPE;
                projection = new String[]{Recipe.RECIPE_FAVOURITED};
                break;
            case INGREDIENTS_LOADER:
                uri = RecipeProvider.CONTENT_URI_INGREDIENTS;
                //selection = Ingredients.INGREDIENTS_ASSOCIATED_RECIPE + "=?";
                //selectionArgs = new String[]{String.valueOf(selectedRecipeId)};
                break;
            case STEPS_LOADER:
                uri = RecipeProvider.CONTENT_URI_STEPS;
                //selection = Steps.STEPS_ASSOCIATED_RECIPE + "=?";
                //selectionArgs = new String[]{String.valueOf(selectedRecipeId)};
                break;
        }
        return new CursorLoader(this, uri, projection, null, null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case RECIPE_LOADER:
                data.moveToFirst();
                int favourited = data.getInt(data.getColumnIndex(Recipe.RECIPE_FAVOURITED));
                if (favourited == getResources().getInteger(R.integer.is_favourited)) {
                    isFavourited = true;
                } else {
                    isFavourited = false;
                }
                break;
            case INGREDIENTS_LOADER:
                data.moveToFirst();
                while (data.moveToNext()) {
                    long recipeID = data.getLong(data.getColumnIndex(Ingredients.INGREDIENTS_ASSOCIATED_RECIPE));
                    if (recipeID == selectedRecipe.getId()) {
                        long id = data.getLong(data.getColumnIndex(Ingredients.INGREDIENTS_ID));
                        String ingredient = data.getString(data.getColumnIndex(Ingredients
                                .INGREDIENTS_INGREDIENT));
                        String measure = data.getString(data.getColumnIndex(Ingredients
                                .INGREDIENTS_MEASURE));
                        int quantity = data.getInt(data.getColumnIndex(Ingredients
                                .INGREDIENTS_QUANTITY));
                        int checked = data.getInt(data.getColumnIndex(Ingredients
                                .INGREDIENTS_CHECKED));
                        ingredientsList.add(new Ingredients(id, ingredient, measure, quantity,
                                checked));
                    }
                }
                break;
            case STEPS_LOADER:
                data.moveToFirst();
                while (data.moveToNext()) {
                    long recipeId = data.getLong(data.getColumnIndex(Steps.STEPS_ASSOCIATED_RECIPE));
                    if (recipeId == selectedRecipe.getId()) {
                        long id = data.getLong(data.getColumnIndex(Steps.STEPS_ID));
                        String shortDescrip
                                = data.getString(data.getColumnIndex(Steps.STEPS_SHORT_DESCRIP));
                        String descrip
                                = data.getString(data.getColumnIndex(Steps.STEPS_DESCRIP));
                        String video = data.getString(data.getColumnIndex(Steps.STEPS_VIDEO));
                        String thumb = data.getString(data.getColumnIndex(Steps.STEPS_THUMB));
                        stepsList.add(new Steps(id, shortDescrip, descrip, video, thumb));
                    }
                }
                break;

        }
        data.close();

        //keep track of each loader finishing so the fragments start with all data on hand.
        loaderHasFinished();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}