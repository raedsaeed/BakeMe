package com.example.android.bakeme.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bakeme.data.db.RecipeContract.IngredientsEntry;
import com.example.android.bakeme.data.db.RecipeContract.RecipeEntry;

import static com.example.android.bakeme.data.db.RecipeContract.*;

/**
 * {@link RecipeDbHelper} is an {@link SQLiteOpenHelper} creating and maintaining the database.
 */
public class RecipeDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "recipe.db";
    private static final int DATABASE_VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE = "CREATE TABLE ";
    private static final String START = " (";
    private static final String CLOSE = ");";
    private static final String COMMA = " ,";
    private static final String INT_AUTO = " INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String INT_REQ = " INTEGER NOT NULL";
    private static final String TEXT_REQ = " TEXT NOT NULL";
    private static final String REAL_REQ = " REAL NOT NULL";

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_RECIPE_TABLE = CREATE + RecipeEntry.TABLE_RECIPE + START
                + RecipeEntry.RECIPE_ID + INT_REQ + COMMA
                + RecipeEntry.RECIPE_IMAGE + TEXT_REQ + COMMA
                + RecipeEntry.RECIPE_NAME + TEXT_REQ + COMMA
                + RecipeEntry.RECIPE_SERVINGS + INT_REQ + COMMA
                + RecipeEntry.RECIPE_INGREDIENTS + TEXT_REQ + COMMA
                + RecipeEntry.RECIPE_STEPS + TEXT_REQ + COMMA
                + RecipeEntry.RECIPE_FAVOURITED + INT_REQ + CLOSE;

        db.execSQL(SQL_CREATE_RECIPE_TABLE);

        final String SQL_CREATE_INGREDIENTS_TABLE = CREATE + IngredientsEntry.TABLE_INGREDIENTS
                + START + IngredientsEntry.INGREDIENTS_ID + INT_AUTO + COMMA
                + IngredientsEntry.INGREDIENTS_INGREDIENT + TEXT_REQ + COMMA
                + IngredientsEntry.INGREDIENTS_QUANTITY + INT_REQ + COMMA
                + IngredientsEntry.INGREDIENTS_MEASURE + INT_REQ + COMMA
                + IngredientsEntry.INGREDIENTS_ASSOCIATED_RECIPE + TEXT_REQ + CLOSE;

        db.execSQL(SQL_CREATE_INGREDIENTS_TABLE);

        final String SQL_CREATE_STEPS_TABLE = CREATE + StepsEntry.TABLE_STEPS + START
                + StepsEntry.STEPS_ID + INT_AUTO + COMMA
                + StepsEntry.STEPS_THUMBNAIL + TEXT_REQ + COMMA
                + StepsEntry.STEPS_VIDEO + TEXT_REQ + COMMA
                + StepsEntry.STEPS_SHORT_DESCRIPTION + TEXT_REQ + COMMA
                + StepsEntry.STEPS_DESCRIPTION + TEXT_REQ + COMMA
                + StepsEntry.STEPS_ASSOCIATED_RECIPE + TEXT_REQ + CLOSE;

        db.execSQL(SQL_CREATE_STEPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
