package com.numeriquepro;

class TextArea {
    private String text;
    private String font;

    public class Memento {
        private final String text;
        private final String font;
        private Memento(String text, String font) {
            this.text = text;
            this.font = font;
        }
    }

    public void setText(String text) { this.text = text; }
    public String getText() { return text; }

    public Memento takeSnapshot() {
        return new Memento(text, font);
    }

    public void restore(Memento memento) {
        this.text = memento.text;
        this.font = memento.font;
    }
}