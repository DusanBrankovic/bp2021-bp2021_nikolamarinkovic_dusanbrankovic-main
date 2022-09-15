package projekat.QueryBuilder.rules;

import projekat.QueryBuilder.validator.Function;
import projekat.QueryBuilder.validator.Validator;

public class ParameterTypeRule extends AbstractRule {

    public ParameterTypeRule() {
        setName("ParameterTypeRule");
    }

    @Override
    public boolean check(Function f) {

        String fName = f.getName();
        String varName = f.getFromVariable();

        switch (fName) {
            case "Query", "Join", "WhereIn" -> {
                if (f.getParameters()[0] instanceof String)
                    return true;
                if(fName.equals("WhereIn"))
                    setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresan tip parametra: " + fName + " ocekuje (String column_name)");
                else
                    setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresan tip parametra: " + fName + " ocekuje (String table_name)");
            }
            case "Avg", "Count", "Max", "Min" -> {
                if(f.getParameters().length == 1) {
                    if (f.getParameters()[0] instanceof String)
                        return true;
                } else if (f.getParameters().length == 2){
                    if(f.getParameters()[0] instanceof String && f.getParameters()[1] instanceof String)
                        return true;
                }

                if(f.getParameters().length == 1)
                    setMessage("Funkcija " + " u pomenljivoj \"" + varName + "\" ima pogresan tip parametra: " + fName + " ocekuje (String column_name) ili (String column_name, String alias)");
                else if(f.getParameters().length == 2)
                    setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresne tipove parametara: " + fName + " ocekuje (String column_name) ili (String column_name, String alias)");
            }
            case "WhereEndsWith", "WhereStartsWith", "WhereContains", "WhereInQ", "WhereEqQ" -> {
                if (f.getParameters()[0] instanceof String && f.getParameters()[1] instanceof String)
                    return true;
                if(fName.equals("WhereInQ") || fName.equals("WhereEqQ"))
                    setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresne tipove parametara: " + fName + " ocekuje (String column_name, String query)");
                else
                    setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresne tipove parametara: " + fName + " ocekuje (String column_name, String pattern)");
            }
            case "Select", "OrderBy", "OrderByDesc", "ParametarList", "GroupBy" -> {

                boolean strings = true;
                if(!fName.equals("ParametarList")) {
                    for (int i = 0; i < f.getParameters().length; i++) {
                        if (!(f.getParameters()[i] instanceof String)) {
                            strings = false;
                            break;
                        }
                    }

                    if (strings)
                        return true;
                    else {
                        setMessage("U funkciji " + fName + " u promenljivoj \"" + varName + "\" nisu svi parametri stringovi: " + fName + " očekuje (String column_name1, String column_name2, ...)");
                    }
                } else {
                    return true;
                }
            }
            case "Where", "OrWhere", "AndWhere", "Having", "AndHaving", "OrHaving" -> {

                if(f.getParameters()[0] instanceof String
                        && f.getParameters()[1] instanceof String
                        && f.getParameters()[2] instanceof Object){

                    if(isOperatorValid((String)f.getParameters()[1])){
                        return true;
                    } else {
                        setMessage("Pogresan oblik operatora u funkciji " + fName);
                    }
                } else
                    setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresne tipove parametara: " + fName + " ocekuje (String column_name, String operator, criteria)");
            }
            case "WhereBetween" -> {

                if(f.getParameters()[0] instanceof String
                    && f.getParameters()[1] instanceof Integer
                    && f.getParameters()[2] instanceof Integer){
                    return true;
                }

                setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresne tipove parametara: " + fName + " ocekuje (String column_name, int lowerLimit, int upperLimit)");
            }

            case "On" -> {

                if(f.getParameters()[0] instanceof String
                        && f.getParameters()[1] instanceof String
                        && f.getParameters()[2] instanceof String){

                    if(isOperatorValid((String)f.getParameters()[1])){
                        return true;
                    } else {
                        setMessage("Pogresan oblik operatora u funkciji " + fName);
                    }
                } else {
                    setMessage("Funkcija " + fName + " u pomenljivoj \"" + varName + "\" ima pogresne tipove parametara: " + fName + " ocekuje (String column_name1, String operator, String column_name2)");
                }
            }
        }

        return false;
    }

    public boolean isOperatorValid(String operator){

        switch(operator){
            case "=", ">=", "<=", ">", "<", "like", "<>", "!=" -> {
                return true;
            }
        }

        return false;
    }
}
