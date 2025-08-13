package com.numeriquepro;

//import org.json.JSONObject;
//import org.json.XML;

public class FancyUIServiceAdapter implements MenuRenderer {
    private final JsonMenuRenderer jsonRenderer;

    public FancyUIServiceAdapter(JsonMenuRenderer jsonRenderer) {
        this.jsonRenderer = jsonRenderer;
    }

    @Override
    public void render(String xmlMenu) {
//        JSONObject json = XML.toJSONObject(xmlMenu);
//        jsonRenderer.render(json.toString());
    }
}
