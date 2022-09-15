package projekat.repository.implementation;

import projekat.repository.DBNode;
import projekat.repository.DBNodeComposite;

public class Tabela extends DBNodeComposite {

    public Tabela(String name, DBNode parent){ super(name, parent); }

    @Override
    public void addChild(DBNode child) {
        if (child != null && child instanceof Zaglavlje){
            Zaglavlje zaglavlje = (Zaglavlje) child;
            this.getChildren().add(zaglavlje);
        }
    }


}
