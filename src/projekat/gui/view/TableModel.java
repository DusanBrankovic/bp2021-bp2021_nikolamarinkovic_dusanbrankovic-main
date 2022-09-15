package projekat.gui.view;

import projekat.repository.data.Red;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Vector;

public class TableModel extends DefaultTableModel {

    private List<Red> redovi;


    private void updateModel(){

            int columnCount = redovi.get(0).getPolja().keySet().size();

            Vector columnVector = DefaultTableModel.convertToVector(redovi.get(0).getPolja().keySet().toArray());
            Vector dataVector = new Vector(columnCount);

            for (int i = 0; i < redovi.size(); i++) {
                dataVector.add(DefaultTableModel.convertToVector(redovi.get(i).getPolja().values().toArray()));
            }
            setDataVector(dataVector, columnVector);
    }

    public void setRedovi(List<Red> redovi) {
        this.redovi = redovi;
        updateModel();
    }
}
