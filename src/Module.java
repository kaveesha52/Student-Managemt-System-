import java.io.Serializable;

public class Module implements Serializable {
    private double moduleMark;

    public Module(double moduleMark) {
        this.moduleMark = moduleMark;
    }

    public double getModuleMark() {
        return moduleMark;
    }
}
