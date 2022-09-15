package projekat.error;

import projekat.core.ErrorHandler;
import projekat.observer.ISubscriber;
import projekat.observer.Notification;
import projekat.observer.NotificationEnum;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandlerImpl implements ErrorHandler {

    @Override
    public void generateError(ErrorType errorType) {
        if(errorType == ErrorType.NO_TEXT){
            notifySubscribers(new Notification(NotificationEnum.PARSING_ERROR, new MyError("Niste uneli tekst.", "Greska prilikom parsiranja")));
        } else if(errorType == ErrorType.DECLARATION_ERROR){
            notifySubscribers(new Notification(NotificationEnum.DECLARATION_ERROR, new MyError("Oblik deklarisanja koji se ocekuje je var naziv_varijable = new ...", "Greska prilikom deklarisanja promenljive")));
        }

    }

    private List<ISubscriber> subscribers;

    @Override
    public void addSubscriber(ISubscriber sub) {
        if(sub == null)
            return;
        if(this.subscribers ==null)
            this.subscribers = new ArrayList<>();
        if(this.subscribers.contains(sub))
            return;
        this.subscribers.add(sub);
    }

    @Override
    public void removeSubscriber(ISubscriber sub) {
        if(sub == null || this.subscribers == null || !this.subscribers.contains(sub))
            return;
        this.subscribers.remove(sub);
    }

    @Override
    public void notifySubscribers(Notification notification) {
        if(notification == null || this.subscribers == null || this.subscribers.isEmpty())
            return;

        for(ISubscriber listener : subscribers) {
            listener.update(notification);
        }
    }
}
