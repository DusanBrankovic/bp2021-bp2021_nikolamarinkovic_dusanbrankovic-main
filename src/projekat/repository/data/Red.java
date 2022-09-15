package projekat.repository.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class Red {

    private String ime;
    private Map<String, Object> polja;


    public Red() {
        this.polja = new HashMap<>();
    }

    public void dodajPolje(String imePolja, Object value) {
        this.polja.put(imePolja, value);
    }

    public void ukloniPolje(String imePolja) {
        this.polja.remove(imePolja);
    }

}
