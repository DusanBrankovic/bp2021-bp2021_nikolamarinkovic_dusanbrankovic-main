package projekat.QueryBuilder.rules;

import projekat.AppCore;
import projekat.QueryBuilder.validator.Function;

import java.util.ArrayList;

public class HavingParametersRule extends AbstractRule{

    public HavingParametersRule() {
        setName("HavingParametersRule");
    }

    @Override
    public boolean check(Function f) {

        String fName = f.getName();
        ArrayList<Function> funkcije = AppCore.getInstance().getValidator().getFunkcije();
        Function havingF = null;

        for(Function function : funkcije){
            if (function.getName().equals("Having")) {
                havingF = function;
                break;
            }
        }

        if(havingF != null) {
            switch (fName) {
                case "Avg" -> {
                    if (havingF.getParameters()[0].equals(f.getParameters()[1])) {
                        return true;
                    }
                    setMessage("U funkciji " + havingF.getName() + " kao prvi parametar moze da se koristi samo alias iz funkcije agregacije " + fName + ": Greska u promenljivoj \"" + f.getFromVariable() + "\"");
                }
                case "Count" -> {
                    if (havingF.getParameters()[0].equals(f.getParameters()[1])) {
                        return true;
                    }
                    setMessage("U funkciji " + havingF.getName() + " kao prvi parametar moze da se koristi samo alias iz funkcije agregacije " + fName + ": Greska u promenljivoj \"" + f.getFromVariable() + "\"");
                }
                case "Min" -> {
                    if (havingF.getParameters()[0].equals(f.getParameters()[1])) {
                        return true;
                    }
                    setMessage("U funkciji " + havingF.getName() + " kao prvi parametar moze da se koristi samo alias iz funkcije agregacije " + fName + ": Greska u promenljivoj \"" + f.getFromVariable() + "\"");
                }
                case "Max" -> {
                    if (havingF.getParameters()[0].equals(f.getParameters()[1])) {
                        return true;
                    }
                    setMessage("U funkciji " + havingF.getName() + " kao prvi parametar moze da se koristi samo alias iz funkcije agregacije " + fName + ": Greska u promenljivoj \"" + f.getFromVariable() + "\"");
                }
            }
        }

        return false;
    }
}
