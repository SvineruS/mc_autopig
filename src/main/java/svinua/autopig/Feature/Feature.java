package svinua.autopig.Feature;

import svinua.autopig.AutoPig;

public abstract class Feature {
    public AutoPig autopig;
    boolean stopped = false;

    public Feature(AutoPig pig) {
        this.autopig = pig;
    }

    public void stop() {
        stopped = true;
//        autopig.set_state(FeatureIdle.class);
    };

    public abstract String get_name();

}
