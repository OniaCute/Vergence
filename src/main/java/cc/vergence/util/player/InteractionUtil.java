package cc.vergence.util.player;

import net.minecraft.client.network.ClientPlayerInteractionManager;

import java.lang.reflect.Method;

public class InteractionUtil {
    private static Method nextInteractionSequenceMethod;

    static {
        try {
            nextInteractionSequenceMethod = ClientPlayerInteractionManager.class.getDeclaredMethod("method_61088"); // seq
            nextInteractionSequenceMethod.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getNextSequence(ClientPlayerInteractionManager interactionManager) {
        try {
            return (int) nextInteractionSequenceMethod.invoke(interactionManager);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}