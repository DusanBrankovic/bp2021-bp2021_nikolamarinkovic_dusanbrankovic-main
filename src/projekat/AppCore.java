package projekat;

import lombok.Getter;
import lombok.Setter;
import projekat.QueryBuilder.Compiler.Compiler;
import projekat.QueryBuilder.validator.Validator;
import projekat.core.ApplicationFramework;
import projekat.core.ErrorHandler;
import projekat.core.Gui;
import projekat.database.Database;
import projekat.database.DatabaseImplementation;
import projekat.database.MSSQLrepository;
import projekat.database.settings.Settings;
import projekat.database.settings.SettingsImplementation;
import projekat.error.ErrorHandlerImpl;
import projekat.gui.SwingGui;
import projekat.observer.IPublisher;
import projekat.observer.ISubscriber;
import projekat.observer.Notification;
import projekat.observer.NotificationEnum;
import projekat.repository.implementation.BazaPodataka;
import projekat.repository.utils.Constants;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AppCore extends ApplicationFramework implements IPublisher {

    private static AppCore instance;

    private Database database;
    private Settings settings;
    private Validator validator;
    private Compiler compiler;

    private List<ISubscriber> subscribers;

    public AppCore() {
        settings = initSettings();
        database = new DatabaseImplementation(new MSSQLrepository(this.settings));
        compiler = new Compiler();
        validator = new Validator(compiler);
    }

    public static void main(String[] args) {
        ErrorHandler errorHandler = new ErrorHandlerImpl();
        Gui gui = new SwingGui();
        ApplicationFramework appCore = AppCore.getInstance();

        appCore.initialise(errorHandler, gui);
        appCore.run();
    }

    public static AppCore getInstance(){
        if(instance == null){
            instance = new AppCore();
        }
        return instance;
    }

    @Override
    public void run() {
        this.gui.start(this);
    }

    public void loadResource(){
        BazaPodataka bp = (BazaPodataka) this.database.loadResource();
        this.notifySubscribers(new Notification(NotificationEnum.RESOURCE_LOADED, bp));
    }

    public void readDataFromTable(String fromTable){
        if(!this.database.readDataFromTable(fromTable).isEmpty()){
            notifySubscribers(new Notification(NotificationEnum.ROWS, this.database.readDataFromTable(fromTable)));
        }else notifySubscribers(new Notification(NotificationEnum.RETURNED_NO_ROWS, ""));
    }

    private Settings initSettings() {
        Settings settingsImplementation = new SettingsImplementation();
        settingsImplementation.addParameter("mssql_ip", Constants.MSSQL_IP);
        settingsImplementation.addParameter("mssql_database", Constants.MSSQL_DATABASE);
        settingsImplementation.addParameter("mssql_username", Constants.MSSQL_USERNAME);
        settingsImplementation.addParameter("mssql_password", Constants.MSSQL_PASSWORD);
        return settingsImplementation;
    }

    public void addSubscriber(ISubscriber sub) {
        if(sub == null)
            return;
        if(this.subscribers ==null)
            this.subscribers = new ArrayList<>();
        if(this.subscribers.contains(sub))
            return;
        this.subscribers.add(sub);
    }

    public void removeSubscriber(ISubscriber sub) {
        if(sub == null || this.subscribers == null || !this.subscribers.contains(sub))
            return;
        this.subscribers.remove(sub);
    }

    public void notifySubscribers(Notification notification) {
        if(notification == null || this.subscribers == null || this.subscribers.isEmpty())
            return;

        for(ISubscriber listener : subscribers){
            listener.update(notification);
        }
    }
}
