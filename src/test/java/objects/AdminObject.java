package objects;

import lombok.Data;
import matcher.MatchWith;

@Data
public class AdminObject {


  @MatchWith
  String adminName;
  String companyEMail;
  String privateEMail;

  public AdminObject( String name, String companyEMail, String privateEMail) {
    this.adminName = name;
    this.companyEMail = companyEMail;
    this.privateEMail = privateEMail;
  }
}
