package graduation.spendiary.security.google;

import lombok.Data;

@Data
public class GoogleUser {
    private String azp;
    private String aud;
    private String sub;
    private String iss;

    private String iat;
    private String exp;
    private String alg;
    private String kid;
    private String typ;

    private String email;
    private String emailVerified;
    private String name;
    private String picture;
    private String givenName;
    private String familyName;
    private String locale;
    private String scope;
    private String atHash;
    private int expiresIn;
    private String accessType;
}
