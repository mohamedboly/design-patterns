package com.numeriquepro;

class TextBox extends Component {
    public TextBox(Mediator mediator) {
        super(mediator);
    }

    public void clear() {
        System.out.println("TextBox vidé");
    }
}