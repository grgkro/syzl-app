package de.stuttgart.syzl3000.adapters;


// To detect clicks in the ViewHolders I need an Interface:
public interface OnRecipeListener {
    //since we use this interface for the View, where the user can select the category (chicken, lunch etc) and also for the view, where the user can select a recipe from the recipe list, we need two methods
    void onRecipeClick(int position);

    void onCategoryClick(String category);
}
