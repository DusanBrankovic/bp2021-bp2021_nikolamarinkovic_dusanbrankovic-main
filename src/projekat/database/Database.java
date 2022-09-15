package projekat.database;

import projekat.repository.DBNode;
import projekat.repository.data.Red;

import java.util.List;

public interface Database {

    DBNode loadResource();

    List<Red> readDataFromTable(String tableName);
}
