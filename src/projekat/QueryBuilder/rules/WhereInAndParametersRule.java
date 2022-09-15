package projekat.QueryBuilder.rules;

import projekat.AppCore;
import projekat.QueryBuilder.validator.Function;

public class WhereInAndParametersRule extends AbstractRule{

    public WhereInAndParametersRule() {
        setName("WhereInAndParametersRule");
    }

    @Override
    public boolean check(Function f) {

        String fName = f.getName();

        switch(fName){
            case "WhereIn" -> {
                boolean found = true;
                for(Function function : AppCore.getInstance().getValidator().getFunkcije()){
                    if (!function.getName().equals("ParametarList")) {
                        found = false;
                    }else {
                        found = true;
                        break;
                    }
                }

                if(found){
                    return true;
                }else {
                    setMessage("Funkcije WhereIn i ParametarList moraju biti pozvane zajedno: Nedostaje funkcija ParametarList u promenljivoj \"" + f.getFromVariable() + "\"");
                    return false;
                }
            }
            case "ParametarList" -> {
                boolean found = true;
                for(Function function : AppCore.getInstance().getValidator().getFunkcije()){
                    if (!function.getName().equals("WhereIn")) {
                        found = false;
                    }else {
                        found = true;
                        break;
                    }
                }

                if(found){
                    return true;
                }else {
                    setMessage("Funkcije WhereIn i ParametarList moraju biti pozvane zajedno: Nedostaje funkcija WhereIn u promenljivoj \"" + f.getFromVariable() + "\"");
                    return false;
                }
            }

        }

        return false;

    }
}
