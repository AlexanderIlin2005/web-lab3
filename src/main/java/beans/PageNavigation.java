package beans;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

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
