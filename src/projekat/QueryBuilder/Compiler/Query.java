package projekat.QueryBuilder.Compiler;

public class Query
{
    private String name, value;

    public Query(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
