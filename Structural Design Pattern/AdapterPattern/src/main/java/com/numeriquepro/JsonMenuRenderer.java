package com.numeriquepro;

public class JsonMenuRenderer {
    private final FancyUIService fancy;

    public JsonMenuRenderer(FancyUIService fancy) {
        this.fancy = fancy;
    }

    public void render(String jsonMenu) {
        fancy.renderJson(jsonMenu);
    }
}
