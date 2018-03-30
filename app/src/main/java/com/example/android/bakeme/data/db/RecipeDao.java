package com.example.android.bakeme.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.android.bakeme.data.Recipe;
import com.example.android.bakeme.data.Recipe.Ingredients;
import com.example.android.bakeme.data.Recipe.Steps;

import static com.example.android.bakeme.data.db.RecipeContract.*;

/**
 * {@link RecipeDao} provides the CRUD methods ready for use for the ContentProvider.
 * See RoomWithContentProvider Code sample provided via AndroidStudio
 */

public interface RecipeDao {

    // ------------- C ----------- //

    @Insert
    long[] insertAllRecipes(ContentValues[] recipes);

    @Insert
    long[] insertAllIngredients(ContentValues[] ingredients);

    @Insert
    long[] insertAllSteps(ContentValues[] steps);

    /**
     * Insert recipes from api
     *
     * @param recipe the single recipe that will be added
     * @return number of recipes added.
     */
    @Insert(onConflict = 1)
    long insertRecipe(Recipe recipe);

    /**
     * insert ingredients from api
     *
     * @param ingredient the single ingredient that will be added
     * @return number of ingredients added.
     */
    @Insert
    long insertIngredient(Ingredients ingredient);

    /**
     * insert steps from api
     *
     * @param step the single step that will be added
     * @return number of steps added.
     */
    @Insert
    long insertStep(Steps step);

    //single inserts is not needed as we will not be writing our own.

    // ------------- R ----------- //

    /**
     * Query all recipes to display
     *
     * @return cursor with all recipes stored in the db
     */
    @Query("SELECT * FROM " + RecipeEntry.TABLE_RECIPE)
    Cursor QueryAllRecipes();

    /**
     * Query all recipes to ingredients
     *
     * @return cursor with all ingredients stored in the db
     */
    @Query("SELECT * FROM " + IngredientsEntry.TABLE_INGREDIENTS)
    Cursor QueryAllIngredients();

    /**
     * Query all steps to display
     *
     * @return cursor with all steps stored in the db
     */
    @Query("SELECT * FROM " + StepsEntry.TABLE_STEPS)
    Cursor QueryAllSteps();

    //we don't need to query single recipes, ingredients or steps

    // ------------- U ----------- //

    /** update recipe will allow users to favourite a recipe
     *
     * @param recipe is the recipe to be updated
     * @return number of recipes that have been updated.
     */
    @Update
    int updateRecipe(Recipe recipe);

    /** update ingredient will allow users to make a shopping list for a recipe.
     *
     * @param ingredient is the ingredient to be updated
     * @return number of ingredients that have been updated.
     */
    @Update
    int updateIngredient(Ingredients ingredient);

    // we don't need to update step information

    //mass update is not needed

    // ------------- D ----------- //
    //We do not need a delete function as we are not editing the recipes provided by the api, nor
    // will this database be accessed by other apps.
}
