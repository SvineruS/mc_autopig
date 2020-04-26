package svinua.autopig.Feature;

import svinua.autopig.AutoPig;

public abstract class Feature {
    public AutoPig pig;
    boolean stopped = false;

    public Feature(AutoPig pig) {
        this.pig = pig;
    }

    public void stop() {
        stopped = true;
        pig.set_state(FeatureIdle.class);
    };

    public abstract String get_name();

}
