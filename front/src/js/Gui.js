import 'firebase/auth';

export default class Gui {

    static setDefaultButtonCharacteristics(button, left, top) {
        button.isClicked = false;
        button.width = "50px";
        button.height = "50px";
        button.cornerRadius = 40;
        button.alpha = 0.9;
        button.color = "grey";
        button.top = top + "%";
        button.left = left + "%";
    }

    static surroundWithColor(button) {
        button.onPointerClickObservable.add(() => {
            button.isClicked = !button.isClicked;
            if (button.isClicked) {
                button.color = "orange";
                button.thickness = 4;
            } else {
                button.color = "grey";
                button.thickness = 1;
            }
        });
    }
}
