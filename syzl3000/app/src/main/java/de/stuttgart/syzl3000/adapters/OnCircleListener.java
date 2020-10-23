package de.stuttgart.syzl3000.adapters;


// To detect clicks in the ViewHolders I need an Interface:
public interface OnCircleListener {
    // we use this interface for the View, where the user can select the circle (girlfriend, Hans + Johannes etc)
    void onCircleClick(String circle);

}
