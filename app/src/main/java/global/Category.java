package global;

/**
 * Created by rodrigo on 03/02/16.
 */
public class Category{
    private String category;


    public Category(String category){
        this.category = category;
    }


    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    public String toString(){
        return category;
    }
}
