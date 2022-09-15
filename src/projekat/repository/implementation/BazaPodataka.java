package projekat.repository.implementation;

import projekat.repository.DBNode;
import projekat.repository.DBNodeComposite;

public class BazaPodataka extends DBNodeComposite {

    public BazaPodataka(String name) { super(name, null); }


    @Override
    public void addChild(DBNode child) {
        if (child != null && child instanceof Tabela) {
            Tabela tabela = (Tabela) child;
            this.getChildren().add(tabela);
        }
    }
}
