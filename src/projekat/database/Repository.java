package projekat.database;

import projekat.repository.DBNode;
import projekat.repository.data.Red;

import java.util.List;

public interface Repository {

    DBNode getSchema();

    List<Red> get(String from);
}
