package projekat.core;

import lombok.Getter;

@Getter
public abstract class ApplicationFramework {

    protected Gui gui;
    //protected Model model;
    protected ErrorHandler errorHandler;

    public abstract void run();

    public void initialise(ErrorHandler errorHandler, Gui gui){

        this.gui = gui;
        this.errorHandler = errorHandler;
        this.errorHandler.addSubscriber(gui);
    }
}
