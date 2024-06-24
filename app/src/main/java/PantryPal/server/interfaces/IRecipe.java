package PantryPal.server.interfaces;

public interface IRecipe {
    String getTitle();
    void setTitle(String title);
    String getDescription();
    void setDescription(String description);
    String getMealType();
    void setMealType(String mealType);
    String getUUID();
    String getJSONString();
}
