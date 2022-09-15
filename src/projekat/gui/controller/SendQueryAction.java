package projekat.gui.controller;

import projekat.AppCore;
import projekat.error.ErrorType;
import projekat.gui.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SendQueryAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {


        String text = MainFrame.getInstance().getJTextArea().getText();
        MainFrame.getInstance().getErrorArea().setText("");

        if(text.equals("")){
            AppCore.getInstance().getErrorHandler().generateError(ErrorType.NO_TEXT);

        } else {
           // System.out.println("Query sent!");

            for (String line : text.split("\\n")) {
                AppCore.getInstance().getValidator().parseQuery(line);
                AppCore.getInstance().getValidator().getFunkcije().clear();

            }

            AppCore.getInstance().getValidator().getGreske().clear();

            if(MainFrame.getInstance().getErrorArea().getText().equals("")) {
               // System.out.println(text + " " + AppCore.getInstance().getValidator().getGreske().toString());
                AppCore.getInstance().readDataFromTable(AppCore.getInstance().getCompiler().prepareStatement(text));
            }
        }
    }
}
