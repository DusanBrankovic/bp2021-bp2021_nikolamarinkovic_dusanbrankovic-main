package projekat.gui.controller;

import lombok.Getter;

@Getter
public class ActionManager {

    private SendQueryAction sendQueryAction;

    public ActionManager(){ initialiseActions(); }

    public void initialiseActions(){

        sendQueryAction = new SendQueryAction();
    }
}
