package com.numeriquepro;

class DialogMediator implements Mediator {
    private Button button;
    private TextBox textBox;
    private ListBox listBox;

    public DialogMediator(Button button, TextBox textBox, ListBox listBox) {
        this.button = button;
        this.textBox = textBox;
        this.listBox = listBox;
    }

    @Override
    public void notify(Component sender, String event) {
        if (sender == button && "click".equals(event)) {
            listBox.update();
            textBox.clear();
        }
    }
}
