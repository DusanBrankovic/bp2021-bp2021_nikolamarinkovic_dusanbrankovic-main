package projekat.gui;

import projekat.AppCore;
import projekat.core.Gui;
import projekat.error.MyError;
import projekat.gui.view.MainFrame;
import projekat.gui.view.TableModel;
import projekat.observer.Notification;
import projekat.observer.NotificationEnum;
import projekat.repository.data.Red;

import java.util.ArrayList;
import java.util.List;

public class SwingGui implements Gui {

    private MainFrame instance;
    private AppCore appCore;
    private TableModel tableModel;

    @Override
    public void start(AppCore appCore) {
        this.appCore = appCore;
        this.appCore.addSubscriber(this);
        this.appCore.getValidator().addSubscriber(this);
        this.appCore.getCompiler().addSubscriber(this);


        tableModel = new TableModel();

        instance = MainFrame.getInstance();
        instance.initialiseGUI();
        instance.getJTable().setModel(tableModel);
    }

    @Override
    public void update(Notification notification) {
        if(notification.getCode() == NotificationEnum.PARSING_ERROR){
            MainFrame.getInstance().showError((MyError)notification.getData());
        } else if(notification.getCode() == NotificationEnum.ERROR_MESSAGES){

            ArrayList<String> msgs = (ArrayList<String>)notification.getData();
            MainFrame.getInstance().getErrorArea().setText("");

            for(String msg : msgs){
                MainFrame.getInstance().getErrorArea().append(msg + "\n");
            }
        } else if(notification.getCode() == NotificationEnum.DECLARATION_ERROR){
            MainFrame.getInstance().showError((MyError) notification.getData());
        } else if(notification.getCode() == NotificationEnum.ROWS) {
            tableModel.setRedovi((List<Red>) notification.getData());
        }else if(notification.getCode() == NotificationEnum.RETURNED_NO_ROWS){
            MainFrame.getInstance().getErrorArea().append("Upit je prosao dobro, ali u databazi nema podataka koji se podudaraju sa datim zahtevom.\n");
        }
        else if(notification.getCode() == NotificationEnum.GROUP_BY_ERROR){
            MainFrame.getInstance().getErrorArea().append("U funkciji GroupBy fale " + notification.getData() + "\n");
        }
    }
}
