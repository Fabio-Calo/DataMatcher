package objects;

import lombok.Data;
import matcher.MatchWith;


@Data
public class UserObject {

    int id;
    @MatchWith(primary = true, field = "adminName")
    String name;
    @MatchWith(field = "privateEMail")
    String eMail;

    public UserObject(int id, String name, String eMail) {
        this.id = id;
        this.name = name;
        this.eMail = eMail;
    }
}
