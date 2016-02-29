package global;


import global.Category;

/**
 * Created by rodrigo on 03/02/16.
 */
public class Task {
    private String title;
    private Category category;
    private String description;
    private long date;
    private int ID;


    public Task(){}


    public Task(String title, Category category, String description, long date, int ID) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.date = date;
        this.ID = ID;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public Category getCategory() {
        return category;
    }


    public void setCategory(Category category) {
        this.category = category;
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
