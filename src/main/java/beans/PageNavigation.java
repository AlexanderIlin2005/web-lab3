package beans;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class PageNavigation {

    public String goToMain() {
        return "go";
    }

    public String goToIndex() {
        return "go";
    }

}
