package de.stuttgart.syzl3000.adapters.movies;


// To detect clicks in the ViewHolders I need an Interface:
public interface OnMovieListener {
    //since we use this interface for the View, where the user can select the category (netflix, prime, drama etc) and also for the view, where the user can select a recipe from the recipe list, we need two methods
    void onMovieClick(int position);

    void onCategoryClick(String category);
}
