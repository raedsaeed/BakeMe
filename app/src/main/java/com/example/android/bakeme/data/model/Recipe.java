package com.example.android.bakeme.data.model;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Retrofit;

/**
 * {@link Recipe} is an object holding the various infos provided by the API concerning all
 * aspects of the recipe in question. It is setup to using http://www.jsonschema2pojo.org/
 * to enable {@link Retrofit} to use it and implements {@link Parcelable} to enable
 * data persistence.
 */
public class Recipe {

    @Expose
    @SerializedName("image")
    private String image;
    @Expose
    @SerializedName("servings")
    private int servings;
    @Expose
    @SerializedName("steps")
    private List<Steps> steps;
    @Expose
    @SerializedName("ingredients")
    private List<Ingredients> ingredients;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("id")
    private int id;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static class Steps {
        @Expose
        @SerializedName("thumbnailURL")
        private String thumbnailurl;
        @Expose
        @SerializedName("videoURL")
        private String videourl;
        @Expose
        @SerializedName("description")
        private String description;
        @Expose
        @SerializedName("shortDescription")
        private String shortdescription;
        @Expose
        @SerializedName("id")
        private int id;

        public String getThumbnailurl() {
            return thumbnailurl;
        }

        public void setThumbnailurl(String thumbnailurl) {
            this.thumbnailurl = thumbnailurl;
        }

        public String getVideourl() {
            return videourl;
        }

        public void setVideourl(String videourl) {
            this.videourl = videourl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getShortdescription() {
            return shortdescription;
        }

        public void setShortdescription(String shortdescription) {
            this.shortdescription = shortdescription;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class Ingredients {
        @Expose
        @SerializedName("ingredient")
        private String ingredient;
        @Expose
        @SerializedName("measure")
        private String measure;
        @Expose
        @SerializedName("quantity")
        private int quantity;

        public String getIngredient() {
            return ingredient;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }

        public String getMeasure() {
            return measure;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
