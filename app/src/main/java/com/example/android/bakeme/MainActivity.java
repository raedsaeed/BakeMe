package com.example.android.bakeme;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.example.android.bakeme.data.model.Recipe;
import com.example.android.bakeme.data.rest.ApiClient;
import com.example.android.bakeme.data.rest.ApiInterface;
import com.example.android.bakeme.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeCardAdapter.RecipeClickHandler {

    String LOG_TAG = MainActivity.class.getSimpleName();

    ActivityMainBinding mainBinder;
    RecipeCardAdapter recipeCardAdapter;
    ArrayList<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinder = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ApiInterface apiCall = ApiClient.getClient().create(ApiInterface.class);

        Call<List<Recipe>> call = apiCall.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {

                    List<Recipe> recipes = response.body();
                    if (recipes.size() != 0) Log.v(LOG_TAG, "list size: " + recipes.size());

                    recipeList.addAll(recipes);
                    if (recipeList.size() != 0) Log.v(LOG_TAG, "array size: " + recipeList.size());

                    setAdapter(MainActivity.this, recipeList, MainActivity.this);
                } else {
                    Log.w(LOG_TAG, "HTTP status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());
            }
        });
    }

    private void setAdapter(Context ctxt, ArrayList<Recipe> recipeList,
                            RecipeCardAdapter.RecipeClickHandler clicker) {
        //set up adapter and RecyclerView.
        recipeCardAdapter = new RecipeCardAdapter(ctxt, recipeList, clicker);
        mainBinder.recipeOverviewRv.setAdapter(recipeCardAdapter);
        mainBinder.recipeOverviewRv.setLayoutManager(new GridLayoutManager(this,
                GridLayoutManager.DEFAULT_SPAN_COUNT));
        recipeCardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(Recipe recipe) {


    }
}
