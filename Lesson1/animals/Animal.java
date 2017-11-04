/**
 * Java 2. Lesson 1.
 *
 *
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-08-31
 */

package animals;

public abstract class Animal {
    protected String name;
    protected int run_limit;

    public abstract String voice();

    public boolean run(int length) {
        return run_limit >= length;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + name;
    }
}