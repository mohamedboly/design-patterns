package com.numeriquepro;

import java.util.Stack;

class Editor {
    private final Stack<TextArea.Memento> history = new Stack<>();
    private final TextArea textArea;

    public Editor(TextArea textArea) {
        this.textArea = textArea;
    }

    public void write(String text) {
        history.push(textArea.takeSnapshot());
        textArea.setText(text);
    }

    public void undo() {
        if (!history.isEmpty()) {
            textArea.restore(history.pop());
        }
    }
}
