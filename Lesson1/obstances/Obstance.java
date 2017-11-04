/**
 * Java 2. Lesson 1.
 *
 *
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-08-31
 */

package obstances;

import animals.Animal;

public interface Obstance {

    boolean doIt(Animal animal);

    @Override
    String toString();

}


