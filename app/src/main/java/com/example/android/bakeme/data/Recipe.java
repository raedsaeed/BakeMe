package com.example.android.bakeme.data;

import android.os.Parcel;
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
public class Recipe implements Parcelable {

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

    protected Recipe(Parcel in) {
        image = in.readString();
        servings = in.readInt();
        name = in.readString();
        id = in.readInt();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeInt(servings);
        dest.writeString(name);
        dest.writeInt(id);
    }

    public static class Steps implements Parcelable {
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

        protected Steps(Parcel in) {
            thumbnailurl = in.readString();
            videourl = in.readString();
            description = in.readString();
            shortdescription = in.readString();
            id = in.readInt();
        }

        public static final Creator<Steps> CREATOR = new Creator<Steps>() {
            @Override
            public Steps createFromParcel(Parcel in) {
                return new Steps(in);
            }

            @Override
            public Steps[] newArray(int size) {
                return new Steps[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(thumbnailurl);
            dest.writeString(videourl);
            dest.writeString(description);
            dest.writeString(shortdescription);
            dest.writeInt(id);
        }
    }

    public static class Ingredients implements Parcelable {
        @Expose
        @SerializedName("ingredient")
        private String ingredient;
        @Expose
        @SerializedName("measure")
        private String measure;
        @Expose
        @SerializedName("quantity")
        private double quantity;

        protected Ingredients(Parcel in) {
            ingredient = in.readString();
            measure = in.readString();
            quantity = in.readDouble();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(ingredient);
            dest.writeString(measure);
            dest.writeDouble(quantity);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
            @Override
            public Ingredients createFromParcel(Parcel in) {
                return new Ingredients(in);
            }

            @Override
            public Ingredients[] newArray(int size) {
                return new Ingredients[size];
            }
        };

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

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "– " + quantity + " " + measure +  " " + ingredient;
        }
    }
}