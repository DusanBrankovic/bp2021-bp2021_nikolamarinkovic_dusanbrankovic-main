package projekat.core;

import projekat.error.ErrorType;
import projekat.observer.IPublisher;

public interface ErrorHandler extends IPublisher {

    void generateError(ErrorType errorType);
}
