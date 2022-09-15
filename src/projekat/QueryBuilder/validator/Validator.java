package projekat.QueryBuilder.validator;

import lombok.Getter;
import lombok.Setter;
import projekat.QueryBuilder.Compiler.Compiler;
import projekat.QueryBuilder.rules.RuleManager;
import projekat.observer.IPublisher;
import projekat.observer.ISubscriber;
import projekat.observer.Notification;
import projekat.observer.NotificationEnum;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Validator implements IPublisher {

    private RuleManager ruleManager;
    private ArrayList<Function> funkcije;

    private ArrayList<String> greske;
    private ArrayList<String> varijable;
    private ArrayList<String> alijasi;
    private List<ISubscriber> subscribers;

    private Compiler compiler;

    public Validator(Compiler compiler) {
        ruleManager = new RuleManager();
        funkcije = new ArrayList<>();
        greske = new ArrayList<>();
        varijable = new ArrayList<>();
        alijasi = new ArrayList<>();

        this.compiler = compiler;
    }

    public void parseQuery(String line){

        //Splitovanje cele linije
        String[] lineParts = line.split(" ", 5);

        //Dodavanje imena varijable u listu varijabli
        String varijabla = lineParts[1].trim();
        varijable.add(varijabla);

        //Query
        String query = lineParts[4];

        //Splitovanje query-ja po ")."
        String[] funkcije = query.split("\\)\\.");
        for (int i = 0; i < funkcije.length; i++) {

            //Ulanjanje poslednje zagrade kod funkcija kojima ostaje
            if (funkcije[i].endsWith(")")) {
                funkcije[i] = funkcije[i].replace(")", "");
            }

            //Splitovanje imena funkcije od parametara
            String[] potpisFunkcije = funkcije[i].split("\\(");

            //Cuvanje imena
            String imeFunkcije = potpisFunkcije[0];

            if(potpisFunkcije.length >= 2) {
                if (potpisFunkcije[1].length() != 0) {

                    //Splitovanje parametara po zarezu
                    String[] argumentiStr = potpisFunkcije[1].split(",");

                    //Uklanjaju se apostrofi ukoliko se radi o stringu

                    Object[] argumenti = new Object[argumentiStr.length];
                    int k = 0;

                    for (int j = 0; j < argumentiStr.length; j++) {
                        if (argumentiStr[j].contains("\"")) {
                            argumenti[k++] = argumentiStr[j].replace("\"", "").trim();
                        } else if(isDate(argumentiStr[j].trim())){

                          String[] dateElements = argumentiStr[j].trim().split("-");
                          int godina = Integer.parseInt(dateElements[0]);
                          int mesec = Integer.parseInt(dateElements[1]);
                          int dan = Integer.parseInt(dateElements[2]);

                          LocalDate date = LocalDate.of(godina, mesec, dan);
                          argumenti[k++] = date;

                        } else {
                            if(varijable.contains(argumentiStr[j].trim())) {
                                argumenti[k++] = argumentiStr[j].trim();
                            } else if(isNumeric(argumentiStr[j].trim())){
                                if (argumentiStr[j].contains(".")) {
                                    argumenti[k++] = Float.parseFloat(argumentiStr[j].trim());
                                } else {
                                    argumenti[k++] = Integer.parseInt(argumentiStr[j].trim());
                                }
                            } else {
                                greske.add("Promenljiva " + argumentiStr[j].trim() + " nije deklarisana.");
                            }
                        }
                    }

                    //Kreiranje nove funkcije i dodavanje u listu svih funkcija u upitu
                    Function funkcija = new Function(imeFunkcije, argumenti, varijabla);
                    this.funkcije.add(funkcija);
                }
            } else {
                Object[] args = new Object[0];
                ruleManager.getParameterNumberRule().check(new Function(imeFunkcije, args, varijabla));
                greske.add(ruleManager.getParameterNumberRule().getMessage());
            }
        }

        //Provera broja parametara
        for (Function f : this.funkcije){
            boolean ruleNumber = ruleManager.getParameterNumberRule().check(f);
            if(ruleNumber) {
                boolean ruleType = ruleManager.getParameterTypeRule().check(f);
                if(!ruleType){
                    greske.add(ruleManager.getParameterTypeRule().getMessage());
                }
            } else {
                greske.add(ruleManager.getParameterNumberRule().getMessage());
            }
        }

        for(Function f : this.funkcije){
            if(f.getName().equals("WhereIn") || f.getName().equals("ParametarList")){
                if(!ruleManager.getWhereInAndParametersRule().check(f))
                    greske.add(ruleManager.getWhereInAndParametersRule().getMessage());
            }

        }

        for(Function f : this.funkcije){
            if(f.getName().equals("Join") || f.getName().equals("On")){
                if(!ruleManager.getJoinAndOnRule().check(f))
                    greske.add(ruleManager.getJoinAndOnRule().getMessage());
            }

        }

        for(Function f : this.funkcije){
            if(f.getName().equals("Avg") || f.getName().equals("Count") || f.getName().equals("Min") || f.getName().equals("Max")){
                if(!ruleManager.getAggrFuncInSelectRule().check(f)){
                    greske.add(ruleManager.getAggrFuncInSelectRule().getMessage());
                }
            }
        }

        boolean having = false;
        boolean orHaving = false;
        boolean andHaving = false;

        for(Function f : this.funkcije){
            switch (f.getName()) {
                case "Having" -> having = true;
                case "OrHaving" -> orHaving = true;
                case "AndHaving" -> andHaving = true;
            }
        }

        if(!having){
            if(orHaving)
                greske.add("Prvo mora biti pozvana funkcija Having kako bi se pozvala funckija OrHaving: Greska u promenljivoj \"" + varijabla + "\"");

            if(andHaving)
                greske.add("Prvo mora biti pozvana funkcija Having kako bi se pozvala funckija AndHaving: Greska u promenljivoj \"" + varijabla + "\"");
        }

        for(Function f : this.funkcije){
            if(having && (f.getName().equals("Avg") || f.getName().equals("Count") || f.getName().equals("Min") || f.getName().equals("Max"))){
                if(!ruleManager.getAggrFuncAliasRule().check(f)){
                    greske.add(ruleManager.getAggrFuncAliasRule().getMessage());
                }
            }
        }

        for(Function f : this.funkcije){
            if(having && (f.getName().equals("Avg") || f.getName().equals("Count") || f.getName().equals("Min") || f.getName().equals("Max"))){
                if(!ruleManager.getHavingParametersRule().check(f)){
                    greske.add(ruleManager.getHavingParametersRule().getMessage());
                }
            }
        }

        boolean where = false;
        boolean orWhere = false;
        boolean andWhere = false;

        for(Function f : this.funkcije){
            switch (f.getName()) {
                case "Where" -> where = true;
                case "OrWhere" -> orWhere = true;
                case "AndWhere" -> andWhere = true;
            }
        }

        if(!where){
            if(orWhere)
                greske.add("Prvo mora biti pozvana funkcija Where kako bi se pozvala funckija OrWhere: Greska u promenljivoj \"" + varijabla + "\"");

            if(andWhere)
                greske.add("Prvo mora biti pozvana funkcija Where kako bi se pozvala funckija AndWhere: Greska u promenljivoj \"" + varijabla + "\"");
        }

        this.notifySubscribers(new Notification(NotificationEnum.ERROR_MESSAGES, greske));
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean isDate(String str){
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.parse(str);
            return true;
        }
        catch(ParseException e){
            return false;
        }
    }


    @Override
    public void addSubscriber(ISubscriber sub) {
        if(sub == null)
            return;
        if(this.subscribers ==null)
            this.subscribers = new ArrayList<>();
        if(this.subscribers.contains(sub))
            return;
        this.subscribers.add(sub);
    }

    @Override
    public void removeSubscriber(ISubscriber sub) {
        if(sub == null || this.subscribers == null || !this.subscribers.contains(sub))
            return;
        this.subscribers.remove(sub);
    }

    @Override
    public void notifySubscribers(Notification notification) {
        if(notification == null || this.subscribers == null || this.subscribers.isEmpty()){
            return;
        }

        for(ISubscriber listener : subscribers) {
            listener.update(notification);

        }


    }
}
