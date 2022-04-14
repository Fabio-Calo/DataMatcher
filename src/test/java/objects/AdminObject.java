package objects;

import lombok.Data;
import matcher.MatchWith;

@Data
public class AdminObject {

  int id;
  String adminName;
  String companyEMail;
  String privateEMail;

  public AdminObject(int id, String name, String companyEMail, String privateEMail) {
    this.id = id;
    this.adminName = name;
    this.companyEMail = companyEMail;
    this.privateEMail = privateEMail;
  }
}
