package objects;

import lombok.Data;
import matcher.MatchWith;


@Data
public class UserObject {

    @MatchWith(primary = true, field = "adminName")
    String name;
    @MatchWith(field = "privateEMail")
    String eMail;

    public UserObject(String name, String eMail) {
        this.name = name;
        this.eMail = eMail;
    }
}
