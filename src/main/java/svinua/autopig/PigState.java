package svinua.autopig;

import svinua.autopig.Feature.Feature;
import svinua.autopig.Feature.FeatureFarmer;
import svinua.autopig.Feature.FeatureIdle;
import svinua.autopig.Feature.FeatureMining;

import java.lang.reflect.Constructor;

public enum PigState {
    Idle(FeatureIdle.class),
    Farm(FeatureFarmer.class),
    Mine(FeatureMining.class);

    public Class<? extends Feature> class_;
    public Constructor<? extends Feature> constructor;

    PigState(Class<?extends Feature> class_) {
        this.class_ = class_;
        try {
            constructor = class_.getConstructor(AutoPig.class);
        } catch (NoSuchMethodException ignored) {}
    }
}
