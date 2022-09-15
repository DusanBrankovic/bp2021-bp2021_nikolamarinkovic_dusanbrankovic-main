package projekat.QueryBuilder.rules;

import projekat.AppCore;
import projekat.QueryBuilder.validator.Function;
import projekat.QueryBuilder.validator.Validator;

public class ParameterNumberRule extends AbstractRule {

    public ParameterNumberRule() {
        setName("ParameterNumberRule");
    }

    @Override
    public boolean check(Function f) {

        String fName = f.getName();
        String varName = f.getFromVariable();

        switch (fName) {
            case "Query", "Join", "WhereIn" -> {
                if (f.getParameters().length == 1)
                    return true;
                setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresan broj parametara: " + fName + " ocekuje (String table_name)");
            }
            case "Avg", "Count", "Max", "Min" -> {
                if (f.getParameters().length == 1 || f.getParameters().length == 2) {
                    if (f.getParameters().length == 1)
                        AppCore.getInstance().getValidator().getAlijasi().add((String) f.getParameters()[0]);
                    else if (f.getParameters().length == 2) {
                        AppCore.getInstance().getValidator().getAlijasi().add((String) f.getParameters()[0]);
                        AppCore.getInstance().getValidator().getAlijasi().add((String) f.getParameters()[1]);
                    }
                    return true;
                }
                setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresan broj parametara: "  + fName + " ocekuje (String column_name) ili (String column_name, String alias)");
            }
            case "WhereEndsWith", "WhereStartsWith", "WhereContains", "WhereInQ", "WhereEqQ" -> {
                if (f.getParameters().length == 2)
                    return true;
                if(fName.equals("WhereInQ") || fName.equals("WhereEqQ"))
                    setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresan broj parametara: " + fName + " ocekuje (String column_name, String query)");
                else
                    setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresan broj parametara: " + fName + " ocekuje (String column_name, String pattern)");
            }
            case "Select", "OrderBy", "OrderByDesc", "ParametarList", "GroupBy" -> {
                if (f.getParameters().length >= 1)
                    return true;
                if(fName.equals("ParametarList"))
                    setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresan broj parametara: " + fName + " ocekuje (p1, p2, ...)");
                else
                    setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresan broj parametara: " + fName + " ocekuje (String column_name1, String column_name2, ...)");
            }
            case "Where", "OrWhere", "AndWhere", "WhereBetween", "Having", "AndHaving", "OrHaving", "On" -> {
                if (f.getParameters().length == 3)
                    return true;
                if(fName.equals("WhereBetween"))
                    setMessage("Funckija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresan broj parametara: " + fName + " ocekuje (String column_name, int lowerLimit, int upperLimit)");
                else if (fName.equals("On"))
                    setMessage("Funckija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresan broj parametara: " + fName + " ocekuje (String column_name1, String operator, String column_name2)");
                else
                    setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresan broj parametara: " + fName + " ocekuje (String column_name, String operator, criteria)");
            }
        }

        return false;
    }
}
