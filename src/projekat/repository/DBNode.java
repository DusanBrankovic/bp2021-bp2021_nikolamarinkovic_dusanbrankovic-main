package projekat.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class DBNode {

    private String name;
    @ToString.Exclude
    private DBNode parent;
}
