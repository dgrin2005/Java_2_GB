/**
 * Java 2. Lesson 1.
 *
 *
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-08-31
 */

package animals;

public class Hen extends Animal implements Jumpable {
    private float jump_limit;

    public Hen(String name) {
        this.name = name;
        this.run_limit = 100;
        jump_limit = 10f;
    }

    @Override
    public String voice() {
        return "ko-ko-ko";
    }

    @Override
    public boolean jump(float height) {
        return jump_limit >= height;
    }
}