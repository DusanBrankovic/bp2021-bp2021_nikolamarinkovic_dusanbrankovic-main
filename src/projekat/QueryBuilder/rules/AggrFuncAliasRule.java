package projekat.QueryBuilder.rules;

import projekat.AppCore;
import projekat.QueryBuilder.validator.Function;

import java.util.ArrayList;

public class AggrFuncAliasRule extends AbstractRule{

    public AggrFuncAliasRule() {
        setName("AggrFuncAliasRule");
    }

    @Override
    public boolean check(Function f) {

        String fName = f.getName();
        ArrayList<Function> funkcije = AppCore.getInstance().getValidator().getFunkcije();
        Function havingF = null;

        for(Function function : funkcije){
            if (function.getName().equals("Select")) {
                havingF = function;
                break;
            }
        }

        if(havingF != null) {
            switch (fName) {
                case "Avg", "Max", "Min", "Count" -> {
                    if (f.getParameters().length == 2) {
                        return true;
                    }
                    setMessage("Alias mora biti postavljen u funkciji " + fName + " ukoliko se filtrira preko funkcije Having: Greska u promenljivoj \"" + f.getFromVariable() + "\"");
                }
            }
        }

        return false;
    }


}
