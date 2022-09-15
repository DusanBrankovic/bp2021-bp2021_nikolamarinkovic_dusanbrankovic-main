package projekat.repository.implementation;

import projekat.repository.DBNode;
import projekat.repository.DBNodeComposite;
import projekat.repository.enums.VrstaZaglavlja;

public class Zaglavlje extends DBNodeComposite {

    private VrstaZaglavlja vrstaZaglavlja;
    private int lenght;

    public Zaglavlje(String name, DBNode parent, VrstaZaglavlja vrstaZaglavlja, int lenght){
        super(name, parent);
        this.vrstaZaglavlja = vrstaZaglavlja;
        this.lenght = lenght;
    }


    @Override
    public void addChild(DBNode child) {
        if (child != null && child instanceof OgranicenjeZaglavlja){
            OgranicenjeZaglavlja ogranicenjeZaglavlja = (OgranicenjeZaglavlja) child;
            this.getChildren().add(ogranicenjeZaglavlja);
        }
    }
}
