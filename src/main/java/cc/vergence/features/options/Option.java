package cc.vergence.features.options;

import cc.vergence.modules.Module;

import java.util.function.Predicate;

public abstract class Option<T> {
    protected String name;
    protected String displayName;
    protected String description;
    protected Module module;
    protected Predicate<?> invisibility;
    protected T value;

    public Option(String name, Predicate<?> invisibility) {
        this.name = name;
        this.invisibility = invisibility;
    }

    public Option(String name, T value, Predicate<?> invisibility) {
        this.name = name;
        this.value = value;
        this.invisibility = invisibility;
    }

    public Option(String name, String description, Predicate<?> invisibility) {
        this.name = name;
        this.description = description;
        this.invisibility = invisibility;
    }

    public Option(String name, String description, T value, Predicate<?> invisibility) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.invisibility = invisibility;
    }

    public void setInvisibility(Predicate<T> invisibility) {
        this.invisibility = invisibility;
    }

    public Predicate<?> getInvisibility() {
        return invisibility;
    }

    public boolean isVisible() {
        if (invisibility != null) {
            return invisibility.test(null);
        }
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract T getValue();

    public abstract void setValue(T value);

    public void setModule(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }
}
