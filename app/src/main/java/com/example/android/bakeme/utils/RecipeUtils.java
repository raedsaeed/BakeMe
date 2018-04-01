package com.example.android.bakeme.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.widget.ImageButton;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe;
import com.example.android.bakeme.data.Recipe.Ingredients;
import com.example.android.bakeme.data.Recipe.Steps;
import com.example.android.bakeme.data.db.RecipeProvider;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Methods enabling the storing of the recipe information.
 */
public class RecipeUtils {

    public static void writeRecipesToRoom(List<Recipe> recipes, Context ctxt) {
        ContentValues singleRecipe = new ContentValues();

        for (int i = 0; i < recipes.size(); i++) {
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
                                              long recipeId, Context ctxt) {
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
                    recipeId);

            ctxt.getContentResolver().insert(RecipeProvider.CONTENT_URI_INGREDIENTS, setOfIngredients);
        }
    }

    public static void writeStepsToRoom(ArrayList<Steps> stepsList, long recipeId, Context ctxt) {
        ContentValues setOfSteps = new ContentValues();

        for (int i = 0; i < stepsList.size(); i++) {
            Steps receivedSteps = stepsList.get(i);

            setOfSteps.put(Steps.STEPS_ID, receivedSteps.getId());
            setOfSteps.put(Steps.STEPS_THUMB, receivedSteps.getThumbnail());
            setOfSteps.put(Steps.STEPS_VIDEO, receivedSteps.getVideo());
            setOfSteps.put(Steps.STEPS_SHORT_DESCRIP,
                    receivedSteps.getShortDescription());
            setOfSteps.put(Steps.STEPS_DESCRIP, receivedSteps.getDescription());
            setOfSteps.put(Steps.STEPS_ASSOCIATED_RECIPE, recipeId);

            Uri inserted = ctxt.getContentResolver().insert(RecipeProvider.CONTENT_URI_STEPS, setOfSteps);
            Timber.v("writing Step to room" + inserted + "; recipe id: " + recipeId);
        }
    }

    public static void updateFavDb(Recipe selectedRecipe, Context ctxt) {
        //create uri referencing the recipe's id
        Uri uri = ContentUris.withAppendedId(RecipeProvider.CONTENT_URI_RECIPE,
                selectedRecipe.getId());

        //store changed favourite selection to the db.
        ContentValues values = new ContentValues();
        values.put(Recipe.RECIPE_FAVOURITED, selectedRecipe.getFavourited());
        ctxt.getContentResolver().update(uri, values, null, null);
    }

    public static void setfavButton(boolean isFavourited, ImageButton favButtonIb, Context ctxt) {
        int color = 0;
        if (isFavourited) {
            color = R.color.colorAccent;
        } else {
            color = R.color.colorUnselected;
        }
        DrawableCompat.setTint(favButtonIb.getDrawable(),
                ContextCompat.getColor(ctxt, color));
    }
}