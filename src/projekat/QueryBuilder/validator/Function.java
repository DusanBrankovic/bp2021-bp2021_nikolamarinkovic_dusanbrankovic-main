package projekat.QueryBuilder.validator;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class Function {

    private String fromVariable;
    private String name;
    private Object[] parameters;

    public Function(String name, Object[] parameters, String fromVariable) {
        this.fromVariable = fromVariable;
        this.name = name;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return name + Arrays.toString(parameters);
    }
}
