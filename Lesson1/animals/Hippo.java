/**
 * Java 2. Lesson 1.
 *
 *
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-08-31
 */

package animals;

public class Hippo extends Animal implements Swimable {
    private int swim_limit;

    public Hippo(String name) {
        this.name = name;
        this.run_limit = 50;
        swim_limit = 200;
    }

    @Override
    public String voice() {
        return "uf-uf-uf";
    }

    @Override
    public boolean swim(int length) {
        return swim_limit >= length;
    }
}