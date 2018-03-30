package com.example.android.bakeme.data.db;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * {@link RecipeContract} holds all table and MIME information to the data retrieved from the api to
 * be displayed.
 */

public class RecipeContract {

    //authority & uri
    public static final String CONTENT_AUTH = "com.example.android.bakeme";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTH);

    //paths for the tables
    public static final String PATH_RECIPE = "recipe";
    public static final String PATH_STEPS = "steps";
    public static final String PATH_INGREDIENTS = "ingredients";


    public static final class RecipeEntry {

        //db table
        public static final String TABLE_RECIPE = "recipes";
        public static final String RECIPE_ID = "id";
        public static final String RECIPE_IMAGE = "image";
        public static final String RECIPE_SERVINGS = "servings";
        public static final String RECIPE_STEPS = "steps";
        public static final String RECIPE_INGREDIENTS = "ingredients";
        public static final String RECIPE_NAME = "name";
        public static final String RECIPE_FAVOURITED = " favourited";

        // table uri
        public static final Uri CONTENT_URI_RECIPE = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_RECIPE).build();

        //MIME types
        public static final String CONTENT_LIST_TYPE_RECIPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTH + PATH_RECIPE;
        public static final String CONTENT_ITEM_TYPE_RECIPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + CONTENT_AUTH + PATH_RECIPE;

    }

    public static final class IngredientsEntry {

        // table uri
        public static final Uri CONTENT_URI_INGREDIENTS = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INGREDIENTS).build();

        //MIME types
        public static final String CONTENT_LIST_TYPE_INGREDIENTS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTH + PATH_INGREDIENTS;
        public static final String CONTENT_ITEM_TYPE_INGREDIENTS = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + CONTENT_AUTH + PATH_INGREDIENTS;

        public static final String TABLE_INGREDIENTS = "ingredients";
        public static final String INGREDIENTS_ID = "id";
        public static final String INGREDIENTS_INGREDIENT = "ingredient";
        public static final String INGREDIENTS_MEASURE = "measure";
        public static final String INGREDIENTS_QUANTITY = "quantity";
        public static final String INGREDIENTS_CHECKED = "checked";
        public static final String INGREDIENTS_ASSOCIATED_RECIPE = "associated_recipe";

    }

    public static final class StepsEntry {

        // table uri
        public static final Uri CONTENT_URI_STEPS = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_STEPS).build();

        //MIME types
        public static final String CONTENT_LIST_TYPE_STEPS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTH + PATH_STEPS;
        public static final String CONTENT_ITEM_TYPE_STEPS = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + CONTENT_AUTH + PATH_STEPS;

        public static final String TABLE_STEPS = "steps";
        public static final String STEPS_ID = "id";
        public static final String STEPS_THUMBNAIL = "thumbnailURL";
        public static final String STEPS_VIDEO = "videoURL";
        public static final String STEPS_DESCRIPTION = "description";
        public static final String STEPS_SHORT_DESCRIPTION = "shortDescription";
        public static final String STEPS_ASSOCIATED_RECIPE = "associated_recipe";

    }
}
