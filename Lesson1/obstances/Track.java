/**
 * Java 2. Lesson 1.
 *
 *
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-08-31
 */

package obstances;
import animals.*;

public class Track implements Obstance{
    private int length;

    public Track(int length) {
        this.length = length;
    }

    public boolean doIt(Animal animal) {
        return animal.run(length);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " (" + length + ")";
    }
}