package com.example.android.bakeme.data.db;

import android.app.Service;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.bakeme.data.Recipe;
import com.example.android.bakeme.data.Recipe.Ingredients;
import com.example.android.bakeme.data.Recipe.Steps;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * {@link RecipeDatabase} is a {@link RoomDatabase} providing access to the db.
 * Based on RoomWithContentProvider & https://github.com/irfankhoirul/udacity-baking-app
 */
@Database(entities = {Recipe.class, Steps.class, Ingredients.class}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase {
    private static final String TAG = "RecipeDatabase";

    //weak access to Dao for the recipes.
    public abstract RecipeDao recipeDao();

    private static RecipeDatabase recipeDbInstance;

    public static synchronized RecipeDatabase getRecipeDbInstance(Context ctxt) {
        if (recipeDbInstance == null) {
            recipeDbInstance = Room.databaseBuilder(ctxt.getApplicationContext(),
                    RecipeDatabase.class, "recipeDb")
                    .allowMainThreadQueries()
                    .build();
        }
        return recipeDbInstance;
    }

    public static void switchToMemory(Context ctxt) {
        recipeDbInstance = Room.inMemoryDatabaseBuilder(ctxt.getApplicationContext(),
                RecipeDatabase.class).build();
    }

//    public static void buildDatabase(final Context ctxt, final List<Recipe> recipes) {
//        Room.databaseBuilder(ctxt.getApplicationContext(),
//                RecipeDatabase.class, "recipe.de").addCallback(new Callback() {
//            @Override
//            public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                super.onCreate(db);
//                Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        getRecipeDbInstance(ctxt).recipeDao().insertAll(recipes);
//                    }
//                });
//
//            }
//        }).build();
//    }
}