package projekat.QueryBuilder.rules;

import projekat.AppCore;
import projekat.QueryBuilder.validator.Function;

public class JoinAndOnRule extends AbstractRule {

    public JoinAndOnRule() {
        setName("JoinAndOnRule");
    }

    @Override
    public boolean check(Function f) {

        String fName = f.getName();

        switch(fName){
            case "Join" -> {
                boolean found = true;
                for(Function function : AppCore.getInstance().getValidator().getFunkcije()){
                    if (!function.getName().equals("On")) {
                        found = false;
                    }else {
                        found = true;
                        break;
                    }
                }

                if(found){
                    return true;
                }else {
                    setMessage("Funkcije Join i On moraju biti pozvane zajedno: Nedostaje funkcija On u promenljivoj \"" + f.getFromVariable() + "\"");
                }
            }
            case "On" -> {
                boolean found = true;
                for(Function function : AppCore.getInstance().getValidator().getFunkcije()){
                    if (!function.getName().equals("Join")) {
                        found = false;
                    }else {
                        found = true;
                        break;
                    }
                }

                if(found){
                    return true;
                }else {
                    setMessage("Funkcije Join i On moraju biti pozvane zajedno: Nedostaje funkcija Join u promenljivoj \"" + f.getFromVariable() + "\"");
                }
            }

        }

        return false;
    }
}
