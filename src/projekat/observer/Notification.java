package projekat.observer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Notification {

    private NotificationEnum code;
    private Object data;
}
