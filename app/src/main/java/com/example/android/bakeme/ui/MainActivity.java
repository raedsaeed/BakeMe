package com.example.android.bakeme.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe;
import com.example.android.bakeme.data.Recipe.Ingredients;
import com.example.android.bakeme.data.adapter.RecipeCardAdapter;
import com.example.android.bakeme.data.api.ApiClient;
import com.example.android.bakeme.data.api.ApiInterface;
import com.example.android.bakeme.data.db.RecipeProvider;
import com.example.android.bakeme.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.example.android.bakeme.data.Recipe.*;

public class MainActivity extends AppCompatActivity implements RecipeCardAdapter.RecipeClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    ActivityMainBinding mainBinder;
    RecipeCardAdapter recipeCardAdapter;
    ArrayList<Recipe> recipeList;

    private static int RECIPE_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinder = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Timber.plant(new Timber.DebugTree());

        if (savedInstanceState != null && savedInstanceState.containsKey(String.valueOf(R.string.RECIPE_KEY))) {
            mainBinder.alertView.progressPb.setVisibility(View.GONE);
            mainBinder.alertView.alertTv.setVisibility(View.GONE);
            recipeList = savedInstanceState.getParcelableArrayList(String.valueOf(R.string.RECIPE_KEY));
            if (recipeList != null) {
                setAdapter(this, recipeList, this);
            }
        } else {
            checkNetworkAndLoadData();
            getSupportLoaderManager().initLoader(RECIPE_LOADER, null, this);
        }
    }

    //Make sure we have internet before we load the data.
    private void checkNetworkAndLoadData() {
        ConnectivityManager connectMan = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectMan != null;
        NetworkInfo netInfo = connectMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            mainBinder.alertView.alertTv.setVisibility(View.GONE);
            mainBinder.alertView.progressPb.setVisibility(View.GONE);

            if (recipeList == null) {

                ApiInterface apiCall = ApiClient.getClient().create(ApiInterface.class);

                Call<List<Recipe>> call = apiCall.getRecipes();
                call.enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                        recipeList = new ArrayList<>();
                        if (response.isSuccessful()) {
                            //retrieve data and send to adapter to display
                            List<Recipe> recipes = response.body();
                            //recipeList.addAll(recipes);

                            //write api-data to the db.
                            ContentValues recipeValues = new ContentValues();
                            ContentValues ingredientValues = new ContentValues();
                            Recipe recipe = null;
                            recipeValues.put(Recipe.RECIPE_ID, recipe.getId());
                            recipeValues.put(Recipe.RECIPE_IMAGE, recipe.getImage());
                            recipeValues.put(Recipe.RECIPE_NAME, recipe.getName());
                            recipeValues.put(Recipe.RECIPE_SERVINGS, recipe.getServings());
                            //TODO: get ingredient list-reference
                            //TODO: get step list-reference
                            recipeValues.put(Recipe.RECIPE_FAVOURITED, recipe.getFavourited());
                            getContentResolver().insert(RecipeProvider.CONTENT_URI_RECIPE, recipeValues);


                            Ingredients ingredients = null;
                            ingredientValues.put(Ingredients.INGREDIENTS_ID, ingredients.getId());
                            ingredientValues.put(Ingredients.INGREDIENTS_INGREDIENT, ingredients
                                    .getIngredient());
                            ingredientValues.put(Ingredients.INGREDIENTS_MEASURE, ingredients
                                    .getMeasure());
                            ingredientValues.put(Ingredients.INGREDIENTS_QUANTITY, ingredients
                                    .getQuantity());
                            ingredientValues.put(Ingredients.INGEDIENTS_CHECKED, ingredients
                                    .getChecked());
                            getContentResolver().insert(RecipeProvider.CONTENT_URI_INGREDIENTS,
                                    ingredientValues);

                            ContentValues stepValues = new ContentValues();
                            Steps steps = null;

                            Timber.v("ingredients: %s", recipeList.get(0).getIngredients().size());

                            getSupportLoaderManager().initLoader(RECIPE_LOADER, null,
                                    MainActivity.this);


                        } else {
                            //write error to log as a warning
                            Timber.w("HTTP status code: %s", response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Recipe>> call, Throwable t) {
                        //write error to log
                        Timber.e(t.toString());
                    }
                });
            }
        } else {
            mainBinder.alertView.progressPb.setVisibility(View.GONE);
            mainBinder.alertView.alertTv.setVisibility(View.VISIBLE);
            mainBinder.alertView.alertTv.setText(R.string.no_internet);
        }
    }

    //build the adapter and with it the RecyclerView to display the data.
    private void setAdapter(Context ctxt, ArrayList<Recipe> recipeList,
                            RecipeCardAdapter.RecipeClickHandler clicker) {
        //set up adapter and RecyclerView.
        recipeCardAdapter = new RecipeCardAdapter(ctxt, recipeList, clicker);
        mainBinder.recipeOverviewRv.setAdapter(recipeCardAdapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mainBinder.recipeOverviewRv.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mainBinder.recipeOverviewRv.setLayoutManager(new GridLayoutManager(this, 2));
        }

        recipeCardAdapter.notifyDataSetChanged();
    }

    /**
     * Start DetailActivity with recipe selected
     *
     * @param selectedRecipe Is the selected recipe to be passed to the activity
     */
    @Override
    public void onClick(Recipe selectedRecipe) {
        Intent openDetailActivity = new Intent(this, DetailActivity.class);
        openDetailActivity.putExtra(String.valueOf(R.string.SELECTED_RECIPE), selectedRecipe);
        Timber.v("ingredients: %s", selectedRecipe.getIngredients().size());

        startActivity(openDetailActivity);
    }

    /**
     * Keep hold of the data
     *
     * @param outState is the bundle which holds the data for the activity to reuse
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(String.valueOf(R.string.RECIPE_KEY), recipeList);
        super.onSaveInstanceState(outState);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Timber.v("onCreate Loader called");
        String[] projection = new String[]{
                RECIPE_ID,
                RECIPE_IMAGE,
                RECIPE_NAME,
                RECIPE_SERVINGS,
                RECIPE_FAVOURITED};
        return new CursorLoader(this, RecipeProvider.CONTENT_URI_RECIPE, projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Timber.v("onLoadFinished called");
        if (data == null) {
            mainBinder.alertView.alertTv.setText(R.string.no_recipes);
        } else {
            data.moveToFirst();
            while (data.moveToNext()) {
                int id = data.getInt(data.getColumnIndex(RECIPE_ID));
                String image = data.getString((data.getColumnIndex(RECIPE_IMAGE)));
                String name = data.getString(data.getColumnIndex(RECIPE_NAME));
                int servings = data.getInt(data.getColumnIndex(RECIPE_SERVINGS));
                int favourited = data.getInt(data.getColumnIndex(RECIPE_FAVOURITED));
                recipeList.add(new Recipe(id, image, name, servings, favourited));
            }
        }

        setAdapter(this, recipeList, this);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // no action on reset required, as it is not used.
    }
}