package com.example.android.bakeme.data.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.bakeme.data.Recipe;
import com.example.android.bakeme.data.Recipe.Ingredients;
import com.example.android.bakeme.data.Recipe.Steps;
import com.example.android.bakeme.data.db.RecipeContract.IngredientsEntry;
import com.example.android.bakeme.data.db.RecipeContract.RecipeEntry;
import com.example.android.bakeme.data.db.RecipeContract.StepsEntry;


import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

/**
 * {@link RecipeProvider} is a {@link ContentProvider} communicating between the acitivities and the
 * db using {@link RecipeDao}.
 */
public class RecipeProvider extends ContentProvider {

    private static final String TAG = "RecipeProvider";
    //authority & uri
    public static final String CONTENT_AUTH = "com.example.android.bakeme";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTH);

    //paths for the tables
    public static final String PATH_RECIPE = "recipe";
    public static final String PATH_STEPS = "steps";
    public static final String PATH_INGREDIENTS = "ingredients";

    // table uris
    public static final Uri CONTENT_URI_RECIPE = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_RECIPE).build();
    public static final Uri CONTENT_URI_STEPS = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_STEPS).build();
    public static final Uri CONTENT_URI_INGREDIENTS = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_INGREDIENTS).build();

    //MIME types
    public static final String CONTENT_LIST_TYPE_RECIPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
            + CONTENT_AUTH + PATH_RECIPE;
    public static final String CONTENT_ITEM_TYPE_RECIPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
            + CONTENT_AUTH + PATH_RECIPE;
    public static final String CONTENT_LIST_TYPE_STEPS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
            + CONTENT_AUTH + PATH_STEPS;
    public static final String CONTENT_ITEM_TYPE_STEPS = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
            + CONTENT_AUTH + PATH_STEPS;
    public static final String CONTENT_LIST_TYPE_INGREDIENTS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
            + CONTENT_AUTH + PATH_INGREDIENTS;
    public static final String CONTENT_ITEM_TYPE_INGREDIENTS = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
            + CONTENT_AUTH + PATH_INGREDIENTS;


    private static final int RECIPE_LIST = 100; //match code for all recipes
    private static final int RECIPE_ENTRY = 101; // match code for one recipe

    private static final int STEPS_LIST = 200; // match code for all steps
    private static final int STEPS_ENTRY = 201; // match code for one step

    private static final int INGREDIENTS_LIST = 300; // match code for all ingredients
    private static final int INGREDIENTS_ENTRY = 301; // match code for one ingredient

    private RecipeDao recipeDao;

    private static UriMatcher buildMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeContract.CONTENT_AUTH;

        uriMatcher.addURI(authority, RecipeContract.PATH_RECIPE, RECIPE_LIST);
        uriMatcher.addURI(authority, RecipeContract.PATH_RECIPE + "/#", RECIPE_ENTRY);
        uriMatcher.addURI(authority, RecipeContract.PATH_STEPS, STEPS_LIST);
        uriMatcher.addURI(authority, RecipeContract.PATH_STEPS + "/#", STEPS_ENTRY);
        uriMatcher.addURI(authority, RecipeContract.PATH_INGREDIENTS, INGREDIENTS_LIST);
        uriMatcher.addURI(authority, RecipeContract.PATH_INGREDIENTS + "/#", INGREDIENTS_ENTRY);
        return uriMatcher;
    }

    private static final UriMatcher uriMatcher = buildMatcher(); // match by MIMEtypes
    private SQLiteDatabase dbRead, dbWrite; //access to the db to read & write

    private int getMatch(@NonNull Uri uri) {
        return uriMatcher.match(uri);
    }

    @Override
    public boolean onCreate() {
        RecipeDbHelper movieDbHelper = new RecipeDbHelper(getContext());

        dbRead = movieDbHelper.getReadableDatabase();
        dbWrite = movieDbHelper.getWritableDatabase();

        Timber.plant(new Timber.DebugTree());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor csr;
        switch (getMatch(uri)) {
            case RECIPE_LIST:
                csr = dbRead.query(RecipeEntry.TABLE_RECIPE, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case INGREDIENTS_LIST:
                csr = dbRead.query(IngredientsEntry.TABLE_INGREDIENTS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case STEPS_LIST:
                csr = dbRead.query(StepsEntry.TABLE_STEPS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri, which cannot be queried: " + uri);
        }
        csr.setNotificationUri(getContext().getContentResolver(), uri);
        return csr;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = getMatch(uri);
        switch (match) {
            case RECIPE_LIST:
                return RecipeEntry.CONTENT_LIST_TYPE_RECIPE;
            case RECIPE_ENTRY:
                return RecipeEntry.CONTENT_ITEM_TYPE_RECIPE;
            case STEPS_LIST:
                return StepsEntry.CONTENT_LIST_TYPE_STEPS;
            case STEPS_ENTRY:
                return StepsEntry.CONTENT_ITEM_TYPE_STEPS;
            case INGREDIENTS_LIST:
                return IngredientsEntry.CONTENT_LIST_TYPE_INGREDIENTS;
            case INGREDIENTS_ENTRY:
                return IngredientsEntry.CONTENT_ITEM_TYPE_INGREDIENTS;
            default:
                throw new IllegalArgumentException("Unknown uri " + uri + " with match " + match);
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        switch (getMatch(uri)) {
            case RECIPE_LIST:
                final List<Recipe> recipes = new ArrayList<>();
                for (int i = 0; i < values.length; i++) {
                    recipes.set(i, Recipe.fromContentValues(values[i]));
                    Log.d(TAG, "bulkInsert: insert data num" + i);
                }
                return recipeDao.insertAll(recipes).length;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        return rowsInserted;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) { // no single insertRecipe needed
        long id = 0;

        switch (getMatch(uri)) {
            case RECIPE_LIST:
                Log.d(TAG, "Trying to insert ");
                        long recipeId = recipeDao.insertRecipe(Recipe.fromContentValues(values));
                        getContext().getContentResolver().notifyChange(uri, null);
                        return ContentUris.withAppendedId(uri, recipeId);

            case INGREDIENTS_LIST:
                id = dbWrite.insert(IngredientsEntry.TABLE_INGREDIENTS, null, values);
                break;
            case STEPS_LIST:
                id = dbWrite.insert(StepsEntry.TABLE_STEPS, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (id == -1) {
            Timber.e("Row could not be inserted for %s", uri);

            notifyResolver(uri);
        }
        return ContentUris.withAppendedId(uri, id); // return new uri and id of row inserted.
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0; //no deletes needed
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[]
            selectionArgs) {
        if (values.size() == 0) {
            return 0; // return early if nothing has been changed
        }

        int updatedRows = 0;

        switch (getMatch(uri)) { //we only need to update single items
            case RECIPE_ENTRY:
                updatedRows = dbWrite.update(RecipeEntry.TABLE_RECIPE, values, selection,
                        selectionArgs);
                break;
            case INGREDIENTS_ENTRY:
                updatedRows = dbWrite.update(IngredientsEntry.TABLE_INGREDIENTS, values, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (updatedRows != 0) {
            notifyResolver(uri);
        }
        return updatedRows;
    }


    private void notifyResolver(@NonNull Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }
}