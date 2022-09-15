package projekat.database;

import lombok.AllArgsConstructor;
import projekat.repository.DBNode;
import projekat.repository.data.Red;

import java.util.List;

@AllArgsConstructor
public class DatabaseImplementation implements Database {

    private Repository repository;


    @Override
    public DBNode loadResource() {
        return repository.getSchema();
    }

    @Override
    public List<Red> readDataFromTable(String tableName) {
        return repository.get(tableName);
    }
}
