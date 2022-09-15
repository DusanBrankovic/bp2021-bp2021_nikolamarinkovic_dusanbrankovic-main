package projekat.database;

import projekat.database.settings.Settings;
import projekat.repository.DBNode;
import projekat.repository.data.Red;
import projekat.repository.enums.VrstaZaglavlja;
import projekat.repository.implementation.BazaPodataka;
import projekat.repository.implementation.Tabela;
import projekat.repository.implementation.Zaglavlje;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MSSQLrepository implements Repository {

    private Settings settings;
    private Connection connection;

    public MSSQLrepository(Settings settings) { this.settings = settings; }

    private void initConnection() throws SQLException, ClassNotFoundException{
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        String ip = (String) settings.getParameter("mssql_ip");
        String database = (String) settings.getParameter("mssql_database");
        String username = (String) settings.getParameter("mssql_username");
        String password = (String) settings.getParameter("mssql_password");
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:jtds:sqlserver://"+ip+"/"+database,username,password);
    }

    private void closeConnection(){
        try{
            connection.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            connection = null;
        }
    }

    @Override
    public DBNode getSchema() {

        try{
            this.initConnection();

            DatabaseMetaData metaData = connection.getMetaData();
            BazaPodataka ir = new BazaPodataka("RAF_BP_Primer");

            String tableType[] = {"TABLE"};
            ResultSet tables = metaData.getTables(connection.getCatalog(), null, null, tableType);

            while (tables.next()){
                String tableName = tables.getString("TABLE_NAME");
                //System.out.println(tableName);
                Tabela newTable = new Tabela(tableName, ir);
                ir.addChild(newTable);

                ResultSet columns = metaData.getColumns(connection.getCatalog(), null, tableName, null);

                while (columns.next()){
                    String columnName = columns.getString("COLUMN_NAME");
                   // System.out.println(tableName + " - " + columnName);
                    String columnType = columns.getString("TYPE_NAME");
                    int columnSize = Integer.parseInt(columns.getString("COLUMN_SIZE"));
                    //System.out.println(columnName + " " + columnType + " " + columnSize);
                    Zaglavlje attribute = new Zaglavlje(columnName, newTable, VrstaZaglavlja.valueOf(columnType.toUpperCase()), columnSize);
                    newTable.addChild(attribute);

                }

            }

            return ir;
            // String isNullable = columns.getString("IS_NULLABLE");
            // ResultSet foreignKeys = metaData.getImportedKeys(connection.getCatalog(), null, table.getName());
            // ResultSet primaryKeys = metaData.getPrimaryKeys(connection.getCatalog(), null, table.getName());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.closeConnection();
        }

        return null;
    }

    @Override
    public List<Red> get(String from) {

        List<Red> redovi = new ArrayList<>();


        try{
            this.initConnection();

            //System.out.println(from);

            String query = from;//"SELECT * FROM EMPLOYEES;";//"SELECT * FROM " + from;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){

                Red red = new Red();
                red.setIme(from);

                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                for (int i = 1; i<=resultSetMetaData.getColumnCount(); i++){

                    String name = resultSetMetaData.getColumnName(i);
                    String type = resultSetMetaData.getColumnTypeName(i);

                    if(resultSetMetaData.getColumnType(i) == Types.TIMESTAMP){

                        //String s = rs.getString(i);

                        SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy");

                        Timestamp ts = rs.getTimestamp(i);

                        Date date = new Date(ts.getTime());

                        String x = format.format(date);

                        //date = format.parse(x);

                        red.dodajPolje(resultSetMetaData.getColumnName(i), x);

                        continue;
                    }
                    else if(resultSetMetaData.getColumnType(i) == Types.DATE){

                            java.sql.Date sqlDate = rs.getDate(i);

                            SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy");

                            Date date = new Date(sqlDate.getTime());

                            String x = format.format(date);

                            //date = format.parse(x);

                            red.dodajPolje(resultSetMetaData.getColumnName(i), x);

                            continue;
                        }
                    red.dodajPolje(resultSetMetaData.getColumnName(i), rs.getString(i));
                }
                redovi.add(red);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.closeConnection();
        }

        //if(redovi.isEmpty()) System.out.println("Prazni redovi");
       // System.out.println(redovi);
            return redovi;
    }
}
