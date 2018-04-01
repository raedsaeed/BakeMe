package com.example.android.bakeme.utils;

import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakeme.BuildConfig;
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

    public static void writeRecipesToRoom(List<Recipe> recipes, Context ctxt) {
        ContentValues singleRecipe = new ContentValues();

        for (int i = 0; i< recipes.size(); i++) {
            Recipe receivedRecipe = recipes.get(i);

            singleRecipe.put(Recipe.RECIPE_ID, receivedRecipe.getId());
            singleRecipe.put(Recipe.RECIPE_IMAGE, receivedRecipe.getImage());
            singleRecipe.put(Recipe.RECIPE_NAME, receivedRecipe.getName());
            singleRecipe.put(Recipe.RECIPE_SERVINGS, receivedRecipe.getServings());
            singleRecipe.put(Recipe.RECIPE_FAVOURITED, receivedRecipe.getFavourited());

            ctxt.getContentResolver().insert(RecipeProvider.CONTENT_URI_RECIPE, singleRecipe);
        }
    }

    public static void writeIngredientsToRoom(ArrayList<Ingredients> ingredientsList,
                                              Context ctxt) {
        ContentValues setOfIngredients = new ContentValues();

        for (int i = 0; i < ingredientsList.size(); i++) {
            Ingredients receivedIngredients = ingredientsList.get(i);

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
                    receivedIngredients.getAssociatedRecipe());

            ctxt.getContentResolver().insert(RecipeProvider.CONTENT_URI_INGREDIENTS, setOfIngredients);
        }
    }

    public static void writeStepsToRoom(ArrayList<Steps> stepsList, Context ctxt) {
        ContentValues setOfSteps = new ContentValues();

        for (int i = 0; i < stepsList.size(); i++) {
            Steps receivedSteps = stepsList.get(i);

            setOfSteps.put(Steps.STEPS_ID, receivedSteps.getId());
            setOfSteps.put(Steps.STEPS_THUMBNAIL, receivedSteps.getThumbnail());
            setOfSteps.put(Steps.STEPS_VIDEO, receivedSteps.getVideo());
            setOfSteps.put(Steps.STEPS_SHORT_DESCRIPTION,
                    receivedSteps.getShortDescription());
            setOfSteps.put(Steps.STEPS_DESCRIPTION, receivedSteps.getDescription());
            setOfSteps.put(Steps.STEPS_ASSOCIATED_RECIPE, receivedSteps.getAssociatedRecipe());

            ctxt.getContentResolver().insert(RecipeProvider.CONTENT_URI_STEPS, setOfSteps);
        }
    }
}