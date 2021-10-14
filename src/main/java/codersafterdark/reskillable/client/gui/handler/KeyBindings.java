package codersafterdark.reskillable.client.gui.handler;

import codersafterdark.reskillable.common.Reskillable;
import codersafterdark.reskillable.client.gui.GuiProfessions;
import codersafterdark.reskillable.client.gui.GuiSkills;
import codersafterdark.reskillable.common.lib.LibMisc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBindings {
    public static KeyBinding openGUI = new KeyBinding(Reskillable.proxy.getLocalizedString("key.open_skills_gui"), Keyboard.KEY_Y, Reskillable.proxy.getLocalizedString("key.controls." + LibMisc.MOD_ID));
    public static KeyBinding keyProfessionGui = new KeyBinding(Reskillable.proxy.getLocalizedString("key.open_professions_gui"), Keyboard.KEY_P, Reskillable.proxy.getLocalizedString("key.controls." + LibMisc.MOD_ID));
    public static KeyBinding keySeasonsHUD = new KeyBinding(Reskillable.proxy.getLocalizedString("key.toggle_seasons_hud"), Keyboard.KEY_H, Reskillable.proxy.getLocalizedString("key.controls." + LibMisc.MOD_ID));
    public static KeyBinding keyClassSpell = new KeyBinding(Reskillable.proxy.getLocalizedString("key.activate.ability"), Keyboard.KEY_F, Reskillable.proxy.getLocalizedString("key.controls." + LibMisc.MOD_ID));

    public static void init() {
        ClientRegistry.registerKeyBinding(openGUI);
        ClientRegistry.registerKeyBinding(keyProfessionGui);
        ClientRegistry.registerKeyBinding(keySeasonsHUD);
        ClientRegistry.registerKeyBinding(keyClassSpell);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        keyTyped(openGUI);
    }

    private void keyTyped(KeyBinding binding) {
        final Minecraft minecraft = FMLClientHandler.instance().getClient();
        if (binding.isPressed()) {
            if (minecraft.currentScreen == null) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiSkills());
            }
        }
    }

    @SubscribeEvent
    public void keyInput(InputEvent.KeyInputEvent event) {
        // Exit on key de-press
        if (!Keyboard.getEventKeyState()) {
            return;
        }
        // If Profession key is pressed, and F3 key is not being held (F3+N toggles Spectator mode)
        if (keyProfessionGui.isKeyDown() && !Keyboard.isKeyDown(Keyboard.KEY_F3)) {
            openProfessionGui();
        }
    }

    // Opens GUI to show profession menu
    @SideOnly(Side.CLIENT)
    private void openProfessionGui () {
        Minecraft mc = Minecraft.getMinecraft();
        mc.displayGuiScreen(new GuiProfessions());
    }
}