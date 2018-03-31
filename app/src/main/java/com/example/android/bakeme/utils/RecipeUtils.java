package com.example.android.bakeme.utils;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe;
import com.example.android.bakeme.data.Recipe.Ingredients;
import com.example.android.bakeme.data.Recipe.Steps;
import com.example.android.bakeme.data.db.RecipeProvider;

import java.util.ArrayList;

/**
 * Methods enabling the storing of the recipe information.
 */
public class RecipeUtils {

    private static Recipe receivedRecipe;

    public static void writeRecipesToRoom(ArrayList<Recipe> recipes, Context ctxt) {
        ContentValues singleRecipe = new ContentValues();

        for (int i = 0; i< recipes.size(); i++) {
            receivedRecipe = recipes.get(i);

            singleRecipe.put(Recipe.RECIPE_ID, receivedRecipe.getId());
            singleRecipe.put(Recipe.RECIPE_IMAGE, receivedRecipe.getImage());
            singleRecipe.put(Recipe.RECIPE_NAME, receivedRecipe.getName());
            singleRecipe.put(Recipe.RECIPE_SERVINGS, receivedRecipe.getServings());
            singleRecipe.put(Recipe.RECIPE_FAVOURITED, receivedRecipe.getFavourited());
            singleRecipe.put(Recipe.RECIPE_INGREDIENTS, ctxt.getString(R.string.ingredient_indicator)
                    + receivedRecipe.getId());
            singleRecipe.put(Recipe.RECIPE_STEPS, ctxt.getString(R.string.steps_indicator)
                    + receivedRecipe.getId());
        }

        ctxt.getContentResolver().insert(RecipeProvider.CONTENT_URI_RECIPE, singleRecipe);
    }

    public static void writeIngredientsToRoom(ArrayList<Recipe> recipes, Context ctxt) {
        ContentValues setOfIngredients = new ContentValues();

        for (int i = 0; i < recipes.size(); i++) {
            receivedRecipe = recipes.get(i);
            Ingredients receivedIngredients = receivedRecipe.getIngredients().get(i);

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
                    Recipe.ASSOCIATED_RECIPE + receivedRecipe.getId());
        }
        ctxt.getContentResolver().insert(RecipeProvider.CONTENT_URI_INGREDIENTS, setOfIngredients);
    }

    public static void writeStepsToRoom(ArrayList<Recipe> recipes, Context ctxt) {
        ContentValues setOfSteps = new ContentValues();

        for (int i = 0; i < recipes.size(); i++) {
            receivedRecipe = recipes.get(i);
            Steps receivedSteps = receivedRecipe.getSteps().get(i);

            setOfSteps.put(Steps.STEPS_ID, receivedSteps.getId());
            setOfSteps.put(Steps.STEPS_THUMBNAIL, receivedSteps.getThumbnail());
            setOfSteps.put(Steps.STEPS_VIDEO, receivedSteps.getVideo());
            setOfSteps.put(Steps.STEPS_SHORT_DESCRIPTION,
                    receivedSteps.getShortDescription());
            setOfSteps.put(Steps.STEPS_DESCRIPTION, receivedSteps.getDescription());
            setOfSteps.put(Steps.STEPS_ASSOCIATED_RECIPE,
                    Recipe.ASSOCIATED_RECIPE + receivedRecipe.getId());

        }
        ctxt.getContentResolver().insert(RecipeProvider.CONTENT_URI_STEPS, setOfSteps);
    }
}