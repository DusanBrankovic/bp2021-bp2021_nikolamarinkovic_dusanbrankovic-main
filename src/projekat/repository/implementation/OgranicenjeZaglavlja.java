package projekat.repository.implementation;

import projekat.repository.DBNode;
import projekat.repository.enums.VrstaOgranicenja;

public class OgranicenjeZaglavlja extends DBNode {

    private VrstaOgranicenja vrstaOgranicenja;

    public OgranicenjeZaglavlja(String name, DBNode parent, VrstaOgranicenja vrstaOgranicenja){
        super(name, parent);
        this.vrstaOgranicenja = vrstaOgranicenja;
    }
}
