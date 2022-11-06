package EarthInvaders.Core;

import EarthInvaders.Interfaces.*;

import java.util.ArrayList;

public class Physics {

    static EntityA entityA;
    static EntityB entityB;
    static EntityC entityC;
    static EntityD entityD;
    public static EntityE entityE;

    // enemy bullet against player
    public static boolean collisionDA(EntityD entD, Player player) {
        if (entD.getBounds().intersects(player.getBounds())) {
            entityA = player;
            return true;
        }

        return false;
    }

    // player bullet against enemy
    public static boolean collisionBC(EntityB entB, ArrayList<EntityC> entC) {
        for (int i = 0; i < entC.size(); i++) {
            if (entB.getBounds().intersects(entC.get(i).getBounds())) {
                entityC = entC.get(i);
                return true;
            }
        }

        return false;
    }


    // player bullet against enemy bullet
    public static boolean collisionBD(EntityB entB, ArrayList<EntityD> entD) {
        for (int i = 0; i < entD.size(); i++) {
            if (entB.getBounds().intersects(entD.get(i).getBounds())) {
                entityD = entD.get(i);
                return true;
            }
        }

        return false;
    }

    // enemy against enemy (move if true)
    public static boolean collisionCC(EntityC entC, ArrayList<EntityC> entCs) {
        for (int i = 0; i < entCs.size(); i++) {
            if (entC.getBounds().intersects(entCs.get(i).getBounds())) {
                entityC = entCs.get(i);
                return true;
            }
        }

        return false;
    }

    public static boolean collisionEA(EntityE entE, ArrayList<EntityA> entAs)
    {
        for (int i = 0; i < entAs.size(); i++) {
            if (entE.getBounds().intersects(entAs.get(i).getBounds())) {
                entityA = entAs.get(i);
                return true;
            }
        }

        return false;
    }
}
