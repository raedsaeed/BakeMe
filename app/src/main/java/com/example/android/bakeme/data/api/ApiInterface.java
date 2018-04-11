package com.example.android.bakeme.data.api;

import com.example.android.bakeme.data.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.example.android.bakeme.data.Recipe.*;

/**
 * {@link ApiInterface} uses {@link retrofit2.Retrofit} to retrieve the information from the API.
 * Based on https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/
 */

public interface ApiInterface {
    String api_path = "topher/2017/May/59121517_baking/baking.json";
    String api_id = "/{id}";
    String api_array = "/{array}";
    String api_with_id_and_array = api_path +api_id + api_array;

    @GET(api_path)
    Call<List<Recipe>> getRecipes();

    @GET(api_with_id_and_array)
    Call<List<Ingredients>> getIngredients(@Path("id") long id, @Path("array") String array);

    @GET(api_with_id_and_array)
    Call<List<Steps>> getSteps(@Path("id") long id, @Path("array") String array);

}
