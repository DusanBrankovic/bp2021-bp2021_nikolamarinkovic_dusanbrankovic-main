package projekat.QueryBuilder.rules;

import lombok.Getter;
import lombok.Setter;
import projekat.QueryBuilder.validator.Function;

@Setter
@Getter
public abstract class AbstractRule {

    private String name;
    private String message;

    public abstract boolean check(Function f);
}
