package com.example.android.project.places;


public class Place
{
    private String id;
    private String name;
    private String location;
    private String categories;
   private String open;
    private String rating;

    public Place(String id, String name, String location, String categories, String open, String rating)
     {
        this.id = id;
        this.name = name;
        this.location = location;
        this.categories = categories;
        this.open = open;
       this.rating = rating;
    }


    public String getId() {
        return id;
    }

    public String getOpen() {
        return open;
    }

    public String getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getCategories() {
        return categories;
    }
}
