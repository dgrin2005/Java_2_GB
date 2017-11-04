/**
 * Java 2. Lesson 1.
 *
 *
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-08-31
 */

import animals.*;
import obstances.*;

public class Lesson1 {

    public static void main(String[] args) {

        Team team1 = new Team("Команда 1", new Cat("Murzik"), new Hen("Izzy"), new Hippo("Hippopo"), new Cat("Barsik"));
        Team team2 = new Team("Команда 2", new Cat("Murzik"), new Cat("Ryzhik"), new Cat("Pushok"), new Cat("Barsik"));
        Course course = new Course(new Track(80), new Wall(3), new Water(10));

        System.out.println(team1);
        System.out.println(team2);
        System.out.println(course);

        course.doIt(team1);
        team1.showResult();
        System.out.println();
        team1.showAllResult();
        System.out.println();

        course.doIt(team2);
        team2.showResult();
        System.out.println();
        team2.showAllResult();
        System.out.println();

        Runtime r = Runtime.getRuntime();
        team1 = null;
        team2 = null;
        course = null;
        System.out.println("Памяти свободно до запуска сборщика мусора "+ r.freeMemory());
        System.gc();
        System.out.println("Памяти свободно после запуска сборщика мусора "+ r.freeMemory());
    }
}

class Team {

    private String teamName;
    private Animal[] team;
    private boolean[][] results;

    Team(String teamName, Animal animal1, Animal animal2, Animal animal3, Animal animal4) {
        this.teamName = teamName;
        team = new Animal[]{animal1, animal2, animal3, animal4};
    }

    public Animal[] getTeam() {
        return team;
    }

    public void setResults(boolean[][] results) {
        this.results = results;
    }

    void showAllResult() {
        int i, j;
        boolean teamResult = true;
        System.out.println("Вывод результатов по прохождению полосы препятствий команды " + teamName + ":");
        for (i = 0; i < results.length; i++) {
            boolean animalResult = true;
            for (j = 0; j < results[i].length; j++) {
                animalResult &= results[i][j];
            }
            System.out.println(team[i] + " - " + animalResult);
            teamResult &= animalResult;
        }
        System.out.println("Итого по команде - " + teamResult);
    }

    void showResult() {
        int i, j;
        System.out.println("Вывод животных, прошедших полосы препятствий из команды " + teamName + ":");
        for (i = 0; i < results.length; i++) {
            boolean animalResult = true;
            for (j = 0; j < results[i].length; j++) {
                animalResult &= results[i][j];
            }
            if (animalResult) System.out.println(team[i]);
        }
    }

    @Override
    public String toString() {
        String name = "Состав команды " + teamName + ":\n";
        for (int i = 0; i < team.length; i++) {
            name += "\t" + team[i] + "\n";
        }
        return name;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("Сработал сборщик мусора, удаялемый объект класса Team: " + teamName);
    }
}

class Course{

    private final int OBSTANCES_IN_COURSE = 3;
    private final int ANIMALS_IN_TEAM = 4;

    private Obstance[] course;

    Course(Obstance obstance1, Obstance obstance2, Obstance obstance3) {
        course = new Obstance[]{obstance1, obstance2, obstance3};
    }

    void doIt(Team team) {
        int i, j;
        boolean[][] results = new boolean[ANIMALS_IN_TEAM][OBSTANCES_IN_COURSE];
        for (i = 0; i < ANIMALS_IN_TEAM; i++) {
            for (j = 0; j < OBSTANCES_IN_COURSE; j++) {
                results[i][j] = course[j].doIt(team.getTeam()[i]);
            }
        }
        team.setResults(results);
    }

    @Override
    public String toString() {
        String name = "Полоса препятствий:\n";
        for (int i = 0; i < course.length; i++) {
            name += "\t" + course[i] + "\n";
        }
        return name;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("Сработал сборщик мусора, удаялемый объект класса Course");
    }

}