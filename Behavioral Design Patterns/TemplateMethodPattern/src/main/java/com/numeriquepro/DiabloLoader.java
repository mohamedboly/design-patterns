package com.numeriquepro;

public class DiabloLoader extends BaseGameLoader {
    protected void loadLocalData() {
        System.out.println("Chargement des données Diablo...");
    }

    protected void createObjects() {
        System.out.println("Création des objets Diablo...");
    }

    protected void initializeProfiles() {
        System.out.println("Initialisation profil Diablo...");
    }
}