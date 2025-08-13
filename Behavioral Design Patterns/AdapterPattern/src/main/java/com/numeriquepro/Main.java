package com.numeriquepro;

public class Main {
    public static void main(String[] args) {
        String xmlData = "<menu><item>Pizza</item></menu>";

        // Ancienne version
        MenuRenderer xmlRenderer = new XmlMenuRenderer();
        xmlRenderer.render(xmlData);

        // Nouvelle version avec Adapter
        FancyUIService fancyUIService = new FancyUIService();
        JsonMenuRenderer jsonRenderer = new JsonMenuRenderer(fancyUIService);
        MenuRenderer adapter = new FancyUIServiceAdapter(jsonRenderer);
        adapter.render(xmlData);
    }
}