package com.example.android.bakeme.utils;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe;
import com.example.android.bakeme.data.db.RecipeContract.IngredientsEntry;
import com.example.android.bakeme.data.db.RecipeContract.RecipeEntry;
import com.example.android.bakeme.data.db.RecipeContract.StepsEntry;

import java.util.ArrayList;

/**
 * Methods enabling the storing of the recipe information.
 */
public class RecipeUtils {

    private static Recipe receivedRecipe;

    public static void writeRecipesToRoom(ArrayList<Recipe> recipes, Context ctxt) {
        ContentValues[] recipeValues = new ContentValues[0];
        ContentValues singleRecipe = new ContentValues();

        for (int i = 0; i < recipes.size(); i++) {
            receivedRecipe = recipes.get(i);

            singleRecipe.put(RecipeEntry.RECIPE_ID, receivedRecipe.getId());
            singleRecipe.put(RecipeEntry.RECIPE_IMAGE, receivedRecipe.getImage());
            singleRecipe.put(RecipeEntry.RECIPE_NAME, receivedRecipe.getName());
            singleRecipe.put(RecipeEntry.RECIPE_SERVINGS, receivedRecipe.getServings());
            singleRecipe.put(RecipeEntry.RECIPE_FAVOURITED, receivedRecipe.getFavourited());
            singleRecipe.put(RecipeEntry.RECIPE_INGREDIENTS,
                    ctxt.getString(R.string.ingredient_indicator) + receivedRecipe.getId());
            singleRecipe.put(RecipeEntry.RECIPE_STEPS,
                    ctxt.getString(R.string.steps_indicator) + receivedRecipe.getId());

            recipeValues[i] = singleRecipe;
        }

        ctxt.getContentResolver().bulkInsert(RecipeEntry.CONTENT_URI_RECIPE, recipeValues);
    }

    public static void writeIngredientsToRoom(ArrayList<Recipe> recipes, Context ctxt) {
        ContentValues[] ingredientValues = new ContentValues[0]; //use id to link to recipe
        ContentValues setOfIngredients = new ContentValues();

        for (int i = 0; i < recipes.size(); i++) {
            receivedRecipe = recipes.get(i);
            Recipe.Ingredients receivedIngredients = receivedRecipe.getIngredients().get(i);

            setOfIngredients.put(IngredientsEntry.INGREDIENTS_ID, receivedIngredients.getId());
            setOfIngredients.put(IngredientsEntry.INGREDIENTS_INGREDIENT, receivedIngredients
                    .getIngredient());
            setOfIngredients.put(IngredientsEntry.INGREDIENTS_MEASURE,
                    receivedIngredients.getMeasure());
            setOfIngredients.put(IngredientsEntry.INGREDIENTS_QUANTITY, receivedIngredients
                    .getQuantity());
            setOfIngredients.put(IngredientsEntry.INGREDIENTS_CHECKED,
                    receivedIngredients.getChecked());
            setOfIngredients.put(IngredientsEntry.INGREDIENTS_ASSOCIATED_RECIPE,
                    ctxt.getString(R.string.ingredient_indicator) + receivedRecipe.getId());

            ingredientValues[i] = setOfIngredients;
        }
        ctxt.getContentResolver().bulkInsert(IngredientsEntry.CONTENT_URI_INGREDIENTS,
                ingredientValues);
    }

    public static void writeStepsToRoom(ArrayList<Recipe> recipes, Context ctxt) {
        ContentValues[] stepsValues = new ContentValues[0];
        ContentValues setOfSteps = new ContentValues();

        for (int i = 0; i < recipes.size(); i++) {
            receivedRecipe = recipes.get(i);
            Recipe.Steps receivedSteps = receivedRecipe.getSteps().get(i);

            setOfSteps.put(StepsEntry.STEPS_ID, receivedSteps.getId());
            setOfSteps.put(StepsEntry.STEPS_THUMBNAIL, receivedSteps.getThumbnail());
            setOfSteps.put(StepsEntry.STEPS_VIDEO, receivedSteps.getVideo());
            setOfSteps.put(StepsEntry.STEPS_SHORT_DESCRIPTION,
                    receivedSteps.getShortDescription());
            setOfSteps.put(StepsEntry.STEPS_DESCRIPTION, receivedSteps.getDescription());
            setOfSteps.put(StepsEntry.STEPS_ASSOCIATED_RECIPE,
                    ctxt.getString(R.string.steps_indicator) + receivedRecipe.getId());

            stepsValues[i] = setOfSteps;
        }
        ctxt.getContentResolver().bulkInsert(StepsEntry.CONTENT_URI_STEPS, stepsValues);
    }
}