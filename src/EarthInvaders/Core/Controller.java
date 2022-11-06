package EarthInvaders.Core;

import EarthInvaders.Enemies.Boss;
import EarthInvaders.Enemies.Enemy;
import EarthInvaders.Interfaces.*;

import java.awt.*;
import java.util.*;

public class Controller {

    public ArrayList<EntityA> ea = new ArrayList<>(); // player
    public ArrayList<EntityB> eb = new ArrayList<>(); // player bullet
    public ArrayList<EntityC> ec = new ArrayList<>(); // enemy
    public ArrayList<EntityD> ed = new ArrayList<>(); // enemy bullet
    public ArrayList<EntityE> ee = new ArrayList<>(); // power ups (and maybe similar stuff that'll get added later)

    private final Textures t;
    private final Game g;
    private final Random r = new Random();

    public HashMap<Integer, Integer> coordinatesOfExplosions = new HashMap<>();
    public ArrayList<Map.Entry<Integer, Integer>> explosionsToDelete = new ArrayList<>();

    public int explosionGifLength = (150 * 4) - 50; // in ms

    public Controller(Textures t, Game g) {
        this.t = t;
        this.g = g;
    }

    public void tick() {

        // ENTITY A
        try {
            for (int i = 0; i < ea.size(); i++) {
                ea.get(i).tick();
            }
        } catch (Exception e) {
            System.out.println("Entity A tick failure");
        }

        // ENTITY B
        try {
            for (int i = 0; i < eb.size(); i++) {
                eb.get(i).tick();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Entity B tick failure");
        }

        try {
            for (int i = 0; i < ec.size(); i++) {
                ec.get(i).tick();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Entity C tick failure");
        }

        try {
            for (int i = 0; i < ed.size(); i++) {
                ed.get(i).tick();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Entity D tick failure");
        }

        try {
            for (int i = 0; i < ee.size(); i++) {
                ee.get(i).tick();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Entity E tick failure");
        }
    }


    public void render(Graphics g) {

        // ENTITY A
        try {
            for (int i = 0; i < ea.size(); i++) {
                ea.get(i).render(g);
            }
        } catch (Exception e) {
            System.out.println("Entity A rendering failure");
        }

        // ENTITY B
        try {
            for (int i = 0; i < eb.size(); i++) {
                eb.get(i).render(g);
            }
        } catch (Exception e) {
            System.out.println("Entity B rendering failure");
        }

        try {
            for (int i = 0; i < ec.size(); i++) {
                ec.get(i).render(g);
            }
        } catch (Exception e) {
            System.out.println("Entity C rendering failure");
        }

        try {
            for (int i = 0; i < ed.size(); i++) {
                ed.get(i).render(g);
            }
        } catch (Exception e) {
            System.out.println("Entity D rendering failure");
        }

        try {
            for (int i = 0; i < ee.size(); i++) {
                ee.get(i).render(g);
            }
        } catch (Exception e) {
            System.out.println("Entity E rendering failure");
        }
    }

    public void createExplosion(double x, double y, boolean playSound)
    {
        coordinatesOfExplosions.put((int) x, (int) y);

        if (Game.PLAY_SFX && playSound) {
            Game.playSound("explosion_sound_2.wav");
        }
    }

    public void addEntity(EntityA block) {
        ea.add(block);
    }

    public void removeEntity(EntityA block) {
        ea.remove(block);
    }

    public void addEntity(EntityB block) {
        eb.add(block);
    }

    public void removeEntity(EntityB block) {
        eb.remove(block);
    }

    public void addEntity(EntityC block) {
        ec.add(block);
    }

    public void removeEntity(EntityC block) {
        ec.remove(block);
    }

    public void addEntity(EntityD block) {
        ed.add(block);
    }

    public void removeEntity(EntityD block) {
        ed.remove(block);
    }

    public void addEntity(EntityE block) {
        ee.add(block);
    }

    public void removeEntity(EntityE block) {
        ee.remove(block);
    }

    public void createEnemy(int count) {
        for (int i = 0; i < count; i++) {
            addEntity(new Enemy(r.nextInt(Game.WIDTH), r.nextInt(Game.HEIGHT / 4), Game.ENEMY_BULLET_COOLDOWN, t, g, this));
        }
    }

    public void spawnBoss(int count)
    {
        for (int i = 0; i < count; i++)
        {
            addEntity(new Boss(r.nextInt(Game.WIDTH), r.nextInt(Game.HEIGHT / 4), t, g, this));
        }
    }

    public ArrayList<EntityA> getEntityA() {
        return ea;
    }

    public ArrayList<EntityB> getEntityB() {
        return eb;
    }

    public ArrayList<EntityC> getEntityC() {
        return ec;
    }

    public ArrayList<EntityD> getEntityD() {
        return ed;
    }

    public ArrayList<EntityE> getEntityE() {
        return ee;
    }
}
