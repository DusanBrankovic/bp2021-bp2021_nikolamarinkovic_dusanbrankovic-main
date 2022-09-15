package projekat.QueryBuilder.rules;

import projekat.AppCore;
import projekat.QueryBuilder.validator.Function;

import java.util.ArrayList;

public class AggrFuncInSelectRule extends AbstractRule {

    public AggrFuncInSelectRule() {
        setName("AggrFuncInSelectRule");
    }

    @Override
    public boolean check(Function f) {

        String fName = f.getName();
        ArrayList<Function> funkcije = AppCore.getInstance().getValidator().getFunkcije();
        Function selectF = null;

        for(Function function : funkcije){
            if (function.getName().equals("Select")) {
                selectF = function;
                break;
            }
        }

        if(selectF != null) {
            switch (fName) {
                case "Avg", "Max", "Min", "Count" -> {
                    if (containsAggrFunc(f, selectF)) {
                        return true;
                    }
                    setMessage("Argumenti funkcije " + fName + " moraju da budu obuhvaceni u funkciji Select: Greska u promenljivoj \"" + f.getFromVariable() + "\"");
                }
            }
        }else {
            setMessage("U upitu mora postojati funkcija Select koja obuhvata argumente iz funkcija agregacije: Greska u promenljivoj \"" + f.getFromVariable() + "\"");
        }

        return false;
    }

    public boolean containsAggrFunc(Function f, Function selectF){

        if(f.getParameters().length == 1){
            for (int i = 0; i < selectF.getParameters().length; i++) {
                if(f.getParameters()[0].equals(selectF.getParameters()[i])){
                    return true;
                }
            }
        }

        if(f.getParameters().length == 2){
            for (int i = 0; i < selectF.getParameters().length; i++) {
                if(f.getParameters()[0].equals(selectF.getParameters()[i]) || f.getParameters()[1].equals(selectF.getParameters()[i])){
                    return true;
                }
            }
        }

        return false;
    }
}
