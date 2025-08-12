package com.numeriquepro;

class Button extends Component {
    public Button(Mediator mediator) {
        super(mediator);
    }

    public void click() {
        System.out.println("Bouton cliqué !");
        mediator.notify(this, "click");
    }
}
