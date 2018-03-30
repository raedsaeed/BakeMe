package com.example.android.bakeme.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Retrofit;

/**
 * {@link Recipe} is an object holding the various infos provided by the API concerning all
 * aspects of the recipe in question. It is setup to using http://www.jsonschema2pojo.org/
 * to enable {@link Retrofit} to use it and implements {@link Parcelable} to enable
 * data persistence.
 */
public class Recipe implements Parcelable {

    private String image;

    private int servings;

    private List<Steps> steps;

    private List<Ingredients> ingredients;

    private String name;

    private long id;

    private int favourited;

    public Recipe(int id, String image, String name, int servings, int favourited) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.servings = servings;
        this.favourited = favourited;
    }

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        image = in.readString();
        servings = in.readInt();
        name = in.readString();
        id = in.readInt();
        favourited = in.readInt();

        //inner classes
        ingredients = new ArrayList<>();
        in.readList(ingredients, Ingredients.class.getClassLoader());

        steps = new ArrayList<>();
        in.readList(steps, Steps.class.getClassLoader());
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

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFavourited() {
        return favourited;
    }

    public void setFavourited(int favourited) {
        this.favourited = favourited;
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
        dest.writeLong(id);
        dest.writeInt((favourited));

        //inner classes
        dest.writeList(ingredients);
        dest.writeList(steps);
    }

    public static class Steps implements Parcelable {

        private String thumbnail;

        private String video;

        private String description;

        private String shortDescription;

        private long id;

        public Steps() {
        }

        public Steps(long id, String shortDescription, String description, String video,
                     String thumbnail) {
            this.id = id;
            this.shortDescription = shortDescription;
            this.description = description;
            this.video = video;
            this.thumbnail = thumbnail;
        }

        protected Steps(Parcel in) {
            thumbnail = in.readString();
            video = in.readString();
            description = in.readString();
            shortDescription = in.readString();
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

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public long getId() {
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
            dest.writeString(thumbnail);
            dest.writeString(video);
            dest.writeString(description);
            dest.writeString(shortDescription);
            dest.writeLong(id);
        }
    }

    public static class Ingredients implements Parcelable {

        private String ingredient;

        private String measure;

        private double quantity;

        private long id;

        private int checked;

        public Ingredients() {
        }

        public Ingredients(long id, String ingredient, String measure, int quantity, int checked) {
            this.id = id;
            this.ingredient = ingredient;
            this.measure = measure;
            this.quantity = quantity;
            this.checked = checked;
        }

        protected Ingredients(Parcel in) {
            ingredient = in.readString();
            measure = in.readString();
            quantity = in.readDouble();
            checked = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(ingredient);
            dest.writeString(measure);
            dest.writeDouble(quantity);
            dest.writeInt(checked);
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

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getChecked() {
            return checked;
        }

        public void setChecked(int checked) {
            this.checked = checked;
        }

        @Override
        public String toString() {
            String measureDisplay = getMeasurementString();
            String quantityDisplay = getQuantityString();

            return quantityDisplay + " " + measureDisplay + " " + ingredient;
        }

        /**
         * remove ".0" where necessary
         *
         * @return a readable number ready for display
         */
        private String getQuantityString() {
            String quantityValue = String.valueOf(quantity);
            String quantityDisplay = null;
            if (quantityValue.endsWith(".0")) {
                StringTokenizer quantitySplit = new StringTokenizer(quantityValue, ".");
                quantityDisplay = quantitySplit.nextToken();
            } else {
                quantityDisplay = quantityValue;
            }
            return quantityDisplay;
        }

        /**
         * retrieve String to display readable measurement
         *
         * @return String ready to display
         */
        private String getMeasurementString() {
            HashMap<String, String> measurements = new HashMap<>();
            measurements.put("CUP", "cup");
            measurements.put("TBLSP", "tbsp");
            measurements.put("TSP", "tsp");
            measurements.put("K", "kg");
            measurements.put("G", "g");
            measurements.put("OZ", "oz");
            measurements.put("UNIT", "");

            return measurements.get(measure);
        }
    }
}