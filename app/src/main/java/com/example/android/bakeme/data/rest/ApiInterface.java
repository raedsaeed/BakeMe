package com.example.android.bakeme.data.rest;

import com.example.android.bakeme.data.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * {@link ApiInterface} uses {@link retrofit2.Retrofit} to retrieve the information from the API.
 * Based on https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/
 */

public interface ApiInterface {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();


}
