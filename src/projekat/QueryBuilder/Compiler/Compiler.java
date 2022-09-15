package projekat.QueryBuilder.Compiler;

import projekat.QueryBuilder.validator.Validator;
import projekat.observer.IPublisher;
import projekat.observer.ISubscriber;
import projekat.observer.Notification;
import projekat.observer.NotificationEnum;

import java.util.ArrayList;
import java.util.List;

public class Compiler implements IPublisher {

    private String selected;
    private List<Query> queries = new ArrayList<Query>();
    private List<AgrrFun> aliases = new ArrayList<AgrrFun>();
    private List<ISubscriber> subscribers;

    String[] queryOrder = {"Query", "Select",  "Avg", "Count", "Min", "Max", "Join", "On",
            "Where", "WhereInQ", "WhereEqQ", "WhereEndsWith", "WhereStartsWith", "WhereContains",  "WhereBetween", "WhereIn", "ParametarList", "OrWhere", "AndWhere",
            "GroupBy", "Having", "OrHaving", "AndHaving", "OrderBy", "OrderByDesc"};



    public String prepareStatement(String initialStatement){

        queries.clear();
        aliases.clear();

        String statements[] = initialStatement.split("\\n");

        //System.out.println(initialStatement);

        for(String s: statements){
            if(s.equals("")) continue;
            String args[] = s.split("\s* \s*",5);
            String name = args[1];
            String value = sortStatement(args[4]);
            Query q = new Query(name, value);
            queries.add(q);
            //System.out.println("Name " + q.getName() + " " + q.getValue());
        }

        return queries.get(queries.size() - 1).getValue();
    }


    private String sortStatement(String initialStatement){

        String[] partArray = initialStatement.split("\\)\s*.\s*");
        String statement = new String();
        String statementExt = new String();

        selected = new String("*");
        String tabele = new String("");


        for(String query : queryOrder) {
            for (String part : partArray) {
                if((part.substring(part.length()-1)).equals(")"));
                else part += ")" ;
                //System.out.println(part);
                if (part.substring(0,part.indexOf("(")).equalsIgnoreCase(query)){
                    switch (query){
                        case "Query": part = part.substring(7); tabele = part.split("\"")[0].toUpperCase(); break;
                        case "Select": processSelect(part); break;
//                        case "Select": part = part.substring(7); for(String s: part.split(",")){s = s.substring(1, s.length()-1).toUpperCase();
//                                if(selected.equals("*")) selected = s; else selected += ", " + s;}
//                                selected = selected.substring(0, selected.length()-1);
//                        break;
                        case "Avg": processAvg(part); break;
                        case "Count": processCount(part); break;
                        case "Min": processMin(part); break;
                        case "Max": processMax(part); break;
                        case "Join": statementExt = processJoin(part); break;
                        case "On": statementExt += processOn(part); break;
                        case "WhereIn": statementExt += processWhereIn(part); break;
                        case "ParametarList": statementExt += processParametarList(part); break;
                        case "WhereInQ": statementExt += processWhereInQ(part); break;
                        case "WhereEqQ": statementExt += processWhereEqQ(part); break;
                        case "WhereEndsWith": statementExt += processWhereEndsWith(part); break;
                        case "WhereStartsWith": statementExt += processWhereStartsWith(part); break;
                        case "WhereContains": statementExt += processWhereContains(part); break;
                        case "WhereBetween": statementExt += processWhereBetween(part); break;
                        case "Where": statementExt += processWhere(part); break;
                        case "OrWhere": statementExt += processOrWhere(part); break;
                        case "AndWhere": statementExt += processAndWhere(part); break;
                        case "GroupBy": statementExt += processGroupBy(part); break;
                        case "Having": statementExt += processHaving(part); break;
                        case "OrHaving": statementExt += processOrHaving(part); break;
                        case "AndHaving": statementExt += processAndHaving(part); break;
                        case "OrderBy": statementExt += processOrderBy(part); break;
                        case "OrderByDesc": statementExt += processOrderByDesc(part); break;
                        default: break;
                    }
                }
            }
        }

        statement = "SELECT " + selected + " FROM " + tabele + statementExt;

        //System.out.println(statement);

        statement = parseAliases(statement);


        return statement + ";";
    }
//   var a = new Query("jobs").Select("job_id","plata").GroupBy("job_id").Avg("min_salary","plata");

    private String parseAliases(String statement){

        for(AgrrFun alias: aliases){
           // System.out.println(alias.getAlias() + "  " + alias.getFunction());
            statement = statement.replaceAll( alias.getAlias().toUpperCase(),alias.getFunction().toUpperCase());
        }
        return statement;
    }

    private void processSelect(String part){
        part = part.substring(7,part.length()-1);

        String args[] = part.split(",\s*");

        for(String s: args){
            if(selected.equals("*")){
                selected = s.substring(1,s.length()-1).toUpperCase();
            }
            else selected += ", " + s.substring(1,s.length()-1).toUpperCase();
        }
    }

    private void processAvg(String part){

        part = part.substring(4, part.length()-1);

        String args[] = part.split(",\s*");

        if(args.length == 2){
            //processedPart += args[0].substring(1,args[0].length()-1).toUpperCase() +") AS \'" + args[1].substring(1,args[1].length()-1).toUpperCase() + "\'";
            aliases.add(new AgrrFun("AVG(" + args[0].substring(1,args[0].length()-1).toUpperCase() + ")",args[1].substring(1,args[1].length()-1).toUpperCase()));
        }
        //else processedPart += args[0].substring(1,args[0].length()-1).toUpperCase() +") ";

        /*if(selected.equals("*")) selected = processedPart;
        else selected += ", "  + processedPart;*/
    }//+

    private void processCount(String part){

        part = part.substring(6, part.length()-1);

        String args[] = part.split(",\s*");

        if(args.length == 2){
            //processedPart += args[0].substring(1,args[0].length()-1).toUpperCase() +") AS \'" + args[1].substring(1,args[1].length()-1).toUpperCase() + "\'";
            aliases.add(new AgrrFun("COUNT(" + args[0].substring(1,args[0].length()-1).toUpperCase() + ")",args[1].substring(1,args[1].length()-1).toUpperCase()));
        }

//        String processedPart = new String("COUNT(");
//
//        part = part.substring(7, part.length()-2);
//
//        if(part.split(",").length == 2){
//            processedPart += part.split(",")[0].toUpperCase().substring(0,part.split(",")[0].length()-1) + ") AS \'" +
//                    part.split(",")[1].toUpperCase().substring(2) + "\'";
//            String newSelected = new String("");
//            for(String table : selected.split(", ")){
//                if(!table.equals(part.split(",")[1].substring(2).toUpperCase())){
//                    if(newSelected.equals("")) newSelected = table;
//                    else newSelected += ", " + table;
//                }
//            }
//            selected = newSelected;
//        }
//        else processedPart += part.toUpperCase();
//
//        if(selected.equals("*")) selected = processedPart;
//        else selected += ", "  + processedPart;
    }//+

    private void processMin(String part){
        part = part.substring(4, part.length()-1);

        String args[] = part.split(",\s*");

        if(args.length == 2){
            //processedPart += args[0].substring(1,args[0].length()-1).toUpperCase() +") AS \'" + args[1].substring(1,args[1].length()-1).toUpperCase() + "\'";
            aliases.add(new AgrrFun("MIN(" + args[0].substring(1,args[0].length()-1).toUpperCase() + ")",args[1].substring(1,args[1].length()-1).toUpperCase()));
        }

//        if(part.split(",").length == 2){
//            processedPart += part.split(",")[0].toUpperCase().substring(0,part.split(",")[0].length()-1) + ") AS \'" +
//                    part.split(",")[1].toUpperCase().substring(2) + "\'";
//            String newSelected = new String("");
//            for(String table : selected.split(", ")){
//                if(!table.equals(part.split(",")[1].substring(2).toUpperCase())){
//                    if(newSelected.equals("")) newSelected = table;
//                    else newSelected += ", " + table;
//                }
//            }
//            selected = newSelected;
//        }
//        else processedPart += part.toUpperCase();
//
//        if(selected.equals("*")) selected = processedPart;
//        else selected += ", "  + processedPart;
    }//+

    private void processMax(String part){

        part = part.substring(4, part.length()-1);

        String args[] = part.split(",\s*");

        if(args.length == 2){
            //processedPart += args[0].substring(1,args[0].length()-1).toUpperCase() +") AS \'" + args[1].substring(1,args[1].length()-1).toUpperCase() + "\'";
            aliases.add(new AgrrFun("MAX(" + args[0].substring(1,args[0].length()-1).toUpperCase() + ")",args[1].substring(1,args[1].length()-1).toUpperCase()));
        }
//        String processedPart = new String("MAX(");
//
//        part = part.substring(5, part.length()-2);
//
//        if(part.split(",").length == 2){
//            processedPart += part.split(",")[0].toUpperCase().substring(0,part.split(",")[0].length()-1) + ") AS \'" +
//                    part.split(",")[1].toUpperCase().substring(2) + "\'";
//            String newSelected = new String("");
//            for(String table : selected.split(", ")){
//                if(!table.equals(part.split(",")[1].substring(2).toUpperCase())){
//                    if(newSelected.equals("")) newSelected = table;
//                    else newSelected += ", " + table;
//                }
//            }
//            selected = newSelected;
//        }
//        else processedPart += part.toUpperCase();
//
//        if(selected.equals("*")) selected = processedPart;
//        else selected += ", "  + processedPart;
    }//+

    private String processJoin(String part){

        String processedPart = new String();

        part = part.substring( 6,part.length()-2);

        processedPart = " JOIN " + part.toUpperCase();

        return processedPart;
    }//+

    private String processOn(String part){
        String processedPart = new String();

        part = part.substring(4);

        String args[] = part.split("\",\s*");

        processedPart = " ON " + args[0].toUpperCase()  + " = " + args[2].substring(1, args[2].length()-2).toUpperCase();

        return processedPart;
    }//+

    private String processWhere(String part) {
        String processedPart = new String(" WHERE ");

        part = part.substring(7);

        String args[] = part.split("\",\s*");

        switch (args[1].substring(1)){
            case "=": processedPart += args[0].toUpperCase() + " = "; break;
            case ">": processedPart += args[0].toUpperCase() + " > "; break;
            case "<": processedPart += args[0].toUpperCase() + " < "; break;
            case ">=": processedPart += args[0].toUpperCase() + " >= "; break;
            case "<=": processedPart += args[0].toUpperCase() + " <= "; break;
            case "!=":
            case "<>":  processedPart += "NOT " +  args[0].toUpperCase() + " = "; break;
            case "like" : processedPart += args[0].toUpperCase() + " LIKE ";break;
            default: break;
        }

        if(Validator.isNumeric(args[2].substring(0,args[2].length()-1))){
            processedPart += args[2].substring(0,args[2].length()-1);
        }
        else if(Validator.isDate(args[2].substring(0,args[2].length()-1))){
            processedPart += "'" + args[2].substring(0,args[2].length()-1) + "'";
        }
        else processedPart += "'" + args[2].substring(1,args[2].length()-2) + "'";
        return  processedPart;
    }//+

    private String processWhereEqQ(String part) {
        String processedPart = new String(" WHERE ");

        part = part.substring(10, part.length()-1);

        String args[] = part.split(",\s*");

        processedPart += args[0].substring(0,args[0].length()-1) + " = ";

        for(Query q: queries){
            if(q.getName().equals(args[1])){
                processedPart += "(" + q.getValue().substring(0,q.getValue().length()-1) + ")";
                break;
            }
        }



        return  processedPart;
    }//+

    private String processWhereInQ(String part) {
        String processedPart = new String(" WHERE ");

        part = part.substring(10, part.length()-1);

        String args[] = part.split(",\s*");

        processedPart += args[0].substring(0,args[0].length()-1) + " IN ";

        for(Query q: queries){
            if(q.getName().equals(args[1])){
                processedPart += "(" + q.getValue().substring(0,q.getValue().length()-1) + ")";
                break;
            }
        }

        return  processedPart;
    }//+

    private String processWhereEndsWith(String part) {
        String processedPart = new String(" WHERE ");

            part =  part.substring(15,part.length()-2);

            String args[] = part.split("\",\s*");

            processedPart += args[0].toUpperCase() + " LIKE \'%" + args[1].substring(1) + "\' ";


        return  processedPart;
    }//+

    private String processWhereStartsWith(String part) {
        String processedPart = new String(" WHERE ");

        part = part.substring(17, part.length() - 2);

        String args[] = part.split("\",\s*");

        processedPart += args[0].toUpperCase() + " LIKE \'" + args[1].substring(1) + "%\' ";


        return  processedPart;
    }//+

    private String processWhereContains(String part) {
        String processedPart = new String(" WHERE ");

        part = part.substring(15, part.length() - 2);

        String args[] = part.split("\s*,\s*");

        processedPart += args[0].substring(0,args[0].length()-1).toUpperCase() + " LIKE \'%" + args[1].substring(1) + "%\' ";


        return  processedPart;
    }//+

    private String processWhereBetween(String part) {
        String processedPart = new String(" WHERE ");

        part = part.substring(14);

        String args[] = part.split(",\s*");

        processedPart += args[0].substring(0, args[0].length()-1).toUpperCase() + " BETWEEN " + args[1] + " AND " + args[2].substring(0,args[2].length()-1);

        return  processedPart;
    }//+

    private String processWhereIn(String part) {
        String processedPart = new String(" WHERE ");

        part = part.substring(9);

        processedPart += part.substring(0,part.length()-2);

        return  processedPart;
    }//+

    private String processParametarList(String part) {
        String processedPart = new String(" IN (");

        part = part.substring(14,part.length()-1);

        String args[] = part.split(",");

        for(String arg : args){
            if(Validator.isNumeric(arg)){
                processedPart += arg + ",";
            }
            else processedPart += "'" + arg.substring(1,arg.length()-1) + "',";
        }

        processedPart = processedPart.substring(0,processedPart.length()-1) + ")";

        return  processedPart;
    }//+

    private String processOrWhere(String part) {
        String processedPart = new String(" OR ");

        part = part.substring(2);
        processedPart += processWhere(part).substring(6);

        return  processedPart;
    }//+

    private String processAndWhere(String part) {
        String processedPart = new String(" AND ");

        part = part.substring(3);
        processedPart += processWhere(part).substring(6);

        return  processedPart;
    }//+

    private String processGroupBy(String part) {
        String processedPart = new String(" GROUP BY ");

        part = part.substring(8,part.length()-1);

        String args[] = part.split(",\s*");

        ArrayList<String> missing = checkGroupBy(part);

        if(!missing.isEmpty()) notifySubscribers(new Notification(NotificationEnum.GROUP_BY_ERROR, missing));


        String arguments = "";

        for(String s: args){
            if(arguments.equals("")) arguments = s.toUpperCase().substring(1,s.length()-1);
            else arguments += ", " + s.toUpperCase().substring(1,s.length()-1);
        }

        processedPart += arguments.toUpperCase();

        return  processedPart;
    }//+

    private String processHaving(String part) {
        String processedPart = new String(" HAVING ");

        part = part.substring(8);

        String args[] = part.split(",\s*");

        processedPart += args[0].substring(0,args[0].length()-1).toUpperCase() + " " + args[1].substring(1,args[1].length()-1).toUpperCase() + " ";

        if(Validator.isNumeric(args[2].substring(0,args[2].length()-1))){
            processedPart += args[2].substring(0,args[2].length()-1);
        }
        else if(Validator.isDate(args[2].substring(0,args[2].length()-1))){
            processedPart += "'" + args[2].substring(0,args[2].length()-1).toUpperCase() + "'";
        }
        else processedPart += "'" + args[2].substring(1,args[2].length()-2).toUpperCase() + "'";

        return  processedPart;
    }//+

    private String processOrHaving(String part) {
        String processedPart = new String(" OR ");

        part = part.substring(2);

        processedPart += processHaving(part).substring(8);

        return  processedPart;
    }//+

    private String processAndHaving(String part) {
        String processedPart = new String(" AND ");

        part = part.substring(3);

        processedPart += processHaving(part).substring(8);

        return  processedPart;
    }//+

    private String processOrderBy(String part) {
        String processedPart = new String(" ORDER BY ");

        part = part.substring(9, part.length()-2);

        processedPart += part.toUpperCase();

        return  processedPart;
    }//+

    private String processOrderByDesc(String part) {
        String processedPart = new String(" ORDER BY ");

        part = part.substring(13, part.length()-2);

        processedPart += part.toUpperCase();

        processedPart += " DESC";

        return  processedPart;
    }//+

    private ArrayList<String> checkGroupBy(String part){

        String inGroupBy[] = part.split(",\s*");

        String inSelected[] = selected.split("\s*,\s*");

        ArrayList<String> missing = new ArrayList<String>();

        for(String s: inSelected){
            Boolean isAlias = false;
            Boolean existsSomewhere = false;
            for(AgrrFun as: aliases){
                if(s.equals(as.getAlias())){
                    isAlias = true;
                    existsSomewhere = true;
                    break;
                }
            }
            if(!isAlias){
                for(String str: inGroupBy){
                    if(s.equalsIgnoreCase(str.substring(1,str.length()-1))){
                        existsSomewhere = true;
                        break;
                    }
                }
            }
            if(existsSomewhere) continue;
            missing.add(s);
        }
        return missing;
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
