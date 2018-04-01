package com.example.android.bakeme.utils;

import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe;
import com.example.android.bakeme.data.Recipe.Ingredients;
import com.example.android.bakeme.data.Recipe.Steps;
import com.example.android.bakeme.data.api.ApiClient;
import com.example.android.bakeme.data.api.ApiInterface;
import com.example.android.bakeme.data.db.RecipeProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Methods enabling the storing of the recipe information.
 */
public class RecipeUtils {

    private static ArrayList<List> recipeList;
    private static ApiInterface apiCall;

    public static void retrieveRecipes(final Context ctxt, final ProgressBar progressPb,
                                       final TextView alertTv) {
        apiCall = ApiClient.getClient().create(ApiInterface.class);

        Call<List<Recipe>> recipeCall = apiCall.getRecipes();
        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipeList = new ArrayList<>();
                if (response.isSuccessful() && response.body() != null) {
                    //retrieve data and send to adapter to display
                    List<Recipe> recipes = response.body();
                    recipeList.addAll(Collections.singleton(response.body()));
                    Timber.v("recipe list size :%s", recipeList.size());

                    writeRecipesToRoom(recipes, ctxt);

                    for (int i = 0; i < recipes.size(); i++) {
                        Recipe selectedRecipe = recipes.get(i);
                        long recipeId = recipes.get(i).getId();
                        retrieveIngredients(selectedRecipe, recipeId, Recipe.RECIPE_INGREDIENTS, ctxt);
                        retrieveSteps(selectedRecipe, recipeId, Recipe.RECIPE_STEPS, ctxt);
                    }

                } else {
                    //write error to log as a warning
                    Timber.w("HTTP status code: %s", response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                //write error to log
                Timber.e(t.toString());
                progressPb.setVisibility(View.GONE);
                alertTv.setVisibility(View.VISIBLE);
                alertTv.setText(R.string.no_internet);
            }
        });
    }

    private static void retrieveSteps(final Recipe selectedRecipe, long recipeId, String recipeSteps,
                                      final Context ctxt) {
        apiCall = ApiClient.getClient().create(ApiInterface.class);

        Call<List<Steps>> stepsCall = apiCall.getSteps(recipeId, recipeSteps);
        stepsCall.enqueue(new Callback<List<Steps>>() {
            @Override
            public void onResponse(Call<List<Steps>> call, Response<List<Steps>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Steps> steps = response.body();
                    RecipeUtils.writeStepsToRoom(response.body(), selectedRecipe, ctxt);
                }
            }

            @Override
            public void onFailure(Call<List<Steps>> call, Throwable t) {
                Timber.e(t.toString());
            }
        });
    }

    private static void retrieveIngredients(final Recipe selectedRecipe, long recipeId,
                                            String recipeIngredients, final Context ctxt) {
        apiCall = ApiClient.getClient().create(ApiInterface.class);

        Call<List<Ingredients>> ingredientsCall = apiCall.getIngredients(recipeId, recipeIngredients);
        ingredientsCall.enqueue(new Callback<List<Ingredients>>() {
            @Override
            public void onResponse(Call<List<Ingredients>> call, Response<List<Ingredients>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Ingredients> ingredients = response.body();
                    RecipeUtils.writeIngredientsToRoom(response.body(), selectedRecipe, ctxt);
                }
            }

            @Override
            public void onFailure(Call<List<Ingredients>> call, Throwable t) {
                Timber.e(t.toString());
            }
        });
    }

    public static void writeRecipesToRoom(List<Recipe> recipes, Context ctxt) {
        ContentValues singleRecipe = new ContentValues();

        for (int i = 0; i < recipes.size(); i++) {
            Recipe receivedRecipe = recipes.get(i);

            singleRecipe.put(Recipe.RECIPE_ID, receivedRecipe.getId());
            singleRecipe.put(Recipe.RECIPE_IMAGE, receivedRecipe.getImage());
            singleRecipe.put(Recipe.RECIPE_NAME, receivedRecipe.getName());
            singleRecipe.put(Recipe.RECIPE_SERVINGS, receivedRecipe.getServings());
            singleRecipe.put(Recipe.RECIPE_FAVOURITED, receivedRecipe.getFavourited());
            singleRecipe.put(Recipe.RECIPE_INGREDIENTS, ctxt.getString(R.string.ingredient_indicator)
                    + receivedRecipe.getId());
            singleRecipe.put(Recipe.RECIPE_STEPS, ctxt.getString(R.string.steps_indicator)
                    + receivedRecipe.getId());

            ctxt.getContentResolver().insert(RecipeProvider.CONTENT_URI_RECIPE, singleRecipe);
        }
    }

    public static void writeIngredientsToRoom(List<Ingredients> recipes, Recipe selectedRecipe,
                                              Context ctxt) {
        ContentValues setOfIngredients = new ContentValues();

        for (int i = 0; i < recipes.size(); i++) {
            Ingredients receivedIngredients = selectedRecipe.getIngredients().get(i);

            setOfIngredients.put(Ingredients.INGREDIENTS_ID, receivedIngredients.getId());
            setOfIngredients.put(Ingredients.INGREDIENTS_INGREDIENT, receivedIngredients
                    .getIngredient());
            setOfIngredients.put(Ingredients.INGREDIENTS_MEASURE,
                    receivedIngredients.getMeasure());
            setOfIngredients.put(Ingredients.INGREDIENTS_QUANTITY, receivedIngredients
                    .getQuantity());
            setOfIngredients.put(Ingredients.INGREDIENTS_CHECKED,
                    receivedIngredients.getChecked());
            setOfIngredients.put(Ingredients.INGREDIENTS_ASSOCIATED_RECIPE,
                    Recipe.ASSOCIATED_RECIPE + selectedRecipe.getId());

            ctxt.getContentResolver().insert(RecipeProvider.CONTENT_URI_INGREDIENTS, setOfIngredients);
        }
    }

    public static void writeStepsToRoom(List<Steps> recipes, Recipe selectedRecipe, Context ctxt) {
        ContentValues setOfSteps = new ContentValues();

        for (int i = 0; i < recipes.size(); i++) {
            Steps receivedSteps = selectedRecipe.getSteps().get(i);

            setOfSteps.put(Steps.STEPS_ID, receivedSteps.getId());
            setOfSteps.put(Steps.STEPS_THUMBNAIL, receivedSteps.getThumbnail());
            setOfSteps.put(Steps.STEPS_VIDEO, receivedSteps.getVideo());
            setOfSteps.put(Steps.STEPS_SHORT_DESCRIPTION,
                    receivedSteps.getShortDescription());
            setOfSteps.put(Steps.STEPS_DESCRIPTION, receivedSteps.getDescription());
            setOfSteps.put(Steps.STEPS_ASSOCIATED_RECIPE,
                    Recipe.ASSOCIATED_RECIPE + selectedRecipe.getId());

            ctxt.getContentResolver().insert(RecipeProvider.CONTENT_URI_STEPS, setOfSteps);

        }
    }
}