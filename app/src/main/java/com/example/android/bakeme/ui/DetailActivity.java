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
import android.support.v7.content.res.AppCompatResources;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe;
import com.example.android.bakeme.data.Recipe.Ingredients;
import com.example.android.bakeme.data.Recipe.Steps;
import com.example.android.bakeme.data.adapter.StepAdapter;
import com.example.android.bakeme.data.db.RecipeContract;
import com.example.android.bakeme.data.db.RecipeContract.IngredientsEntry;
import com.example.android.bakeme.data.db.RecipeProvider;

import java.util.ArrayList;

import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.android.bakeme.data.db.RecipeContract.*;

public class DetailActivity extends AppCompatActivity implements StepAdapter.StepClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    Recipe selectedRecipe;
    OverviewFragment overviewFrag;
    MethodFragment methodFrag;
    FragmentManager fragMan;

    //booleans to track layout
    public static boolean twoPane;

    //Loader constants
    private static final int INGREDIENTS_LOADER = 2;
    private static final int STEPS_LOADER = 3;

    Menu menu;

    static ArrayList<Ingredients> ingredientsList;
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

            overviewFrag = new OverviewFragment();
            methodFrag = new MethodFragment();
        }

        getSupportLoaderManager().initLoader(INGREDIENTS_LOADER, null, this);

        getSupportLoaderManager().initLoader(STEPS_LOADER, null, this);

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
            if (selectedRecipe.getFavourited() == getResources().getInteger(R.integer.is_favourited)) {
                menu.getItem(0).setIcon(AppCompatResources
                        .getDrawable(this, android.R.drawable.btn_star_big_off));
                selectedRecipe.setFavourited(getResources().getInteger(R.integer.not_favourited));//TODO: update db
            } else {
                menu.getItem(0).setIcon(AppCompatResources
                        .getDrawable(this, android.R.drawable.btn_star_big_on));
                selectedRecipe.setFavourited(getResources().getInteger(R.integer.is_favourited));//TODO: update db
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri uri = null;
        switch (id) {
            case INGREDIENTS_LOADER:
                uri = IngredientsEntry.CONTENT_URI_INGREDIENTS;
                break;
            case STEPS_LOADER:
                uri = StepsEntry.CONTENT_URI_STEPS;
                break;
        }
        return new CursorLoader(this, uri, null, null, null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case INGREDIENTS_LOADER:
                data.moveToFirst();
                while (data.moveToNext()) {
                    long id = data.getLong(data.getColumnIndex(IngredientsEntry.INGREDIENTS_ID));
                    String ingredient = data.getString(data.getColumnIndex(IngredientsEntry
                            .INGREDIENTS_INGREDIENT));
                    String measure = data.getString(data.getColumnIndex(IngredientsEntry
                            .INGREDIENTS_MEASURE));
                    int quantity = data.getInt(data.getColumnIndex(IngredientsEntry
                            .INGREDIENTS_QUANTITY));
                    int checked = data.getInt(data.getColumnIndex(IngredientsEntry
                            .INGREDIENTS_CHECKED));
                    ingredientsList.add(new Ingredients(id, ingredient, measure, quantity,
                            checked));
                }
                break;
            case STEPS_LOADER:
                data.moveToFirst();
                while (data.moveToNext()) {
                    long id = data.getLong(data.getColumnIndex(StepsEntry.STEPS_ID));
                    String shortDescription = data.getString(data.getColumnIndex(StepsEntry
                            .STEPS_SHORT_DESCRIPTION));
                    String description = data.getString(data.getColumnIndex(StepsEntry
                            .STEPS_DESCRIPTION));
                    String video = data.getString(data.getColumnIndex(StepsEntry.STEPS_VIDEO));
                    String thumbnail = data.getString(data.getColumnIndex(StepsEntry.STEPS_THUMBNAIL));
                    stepsList.add(new Steps(id, shortDescription, description, video, thumbnail));
                }

        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}