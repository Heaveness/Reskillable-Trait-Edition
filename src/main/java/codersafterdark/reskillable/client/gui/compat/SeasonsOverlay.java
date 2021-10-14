package codersafterdark.reskillable.client.gui.compat;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.client.gui.handler.KeyBindings;
import codersafterdark.reskillable.common.lib.LibMisc;
import codersafterdark.reskillable.common.lib.LibSkills;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

public class SeasonsOverlay extends Gui {

    public static SeasonsOverlay instance = new SeasonsOverlay();
    private static final ResourceLocation TEXTURE = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/compat/seasons_hud.png");
    private static final int glyphWidth = 64;
    private static final int glyphHeight = 16;
    public boolean enabled;

    @SubscribeEvent
    public void renderGameOverlayEvent(RenderGameOverlayEvent event) {
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (PlayerDataHandler.get(player).getSkillInfo(LibSkills.farming).isUnlocked(LibSkills.traitSeasons) && enabled) {

            Season season = SeasonHelper.getSeasonState(player.world).getSeason();
            Season.SubSeason subSeason = SeasonHelper.getSeasonState(player.world).getSubSeason();
            GlStateManager.disableLighting();
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            Minecraft mc = Minecraft.getMinecraft();
            mc.renderEngine.bindTexture(TEXTURE);

            int x = 5;
            int y = 5;

            if (season == Season.SPRING) {
                drawTexturedModalRect(x, y, 0, 16, 22, 22);
                drawTexturedModalRect(x + 3, y + 3, 0, 0, 16, glyphHeight);
            }

            if (season == Season.SUMMER) {
                drawTexturedModalRect(x, y, 0, 16, 22, 22);
                drawTexturedModalRect(x + 3, y + 3, 16, 0, 16, glyphHeight);
            }

            if (season == Season.AUTUMN) {
                drawTexturedModalRect(x, y, 0, 16, 22, 22);
                drawTexturedModalRect(x + 3, y + 3, 32, 0, 16, glyphHeight);
            }

            if (season == Season.WINTER) {
                drawTexturedModalRect(x, y, 0, 16, 22, 22);
                drawTexturedModalRect(x + 3, y + 3, 48, 0, 16, glyphHeight);
            }

            // Draw the text indicating the sub season
            fontRenderer.drawString(new TextComponentTranslation(localizeSubSeason(subSeason)).getUnformattedComponentText(), x, y + 24, colorizeSeasonText(subSeason));
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {keyTyped(KeyBindings.keySeasonsHUD);}

    private void keyTyped(KeyBinding binding) {
        final Minecraft minecraft = FMLClientHandler.instance().getClient();
        boolean hasTrait = PlayerDataHandler.get(minecraft.player).getSkillInfo(LibSkills.farming).isUnlocked(LibSkills.traitSeasons);
        if (hasTrait && binding.isPressed()) {
            if (minecraft.currentScreen == null) {
                enabled = !enabled;
            }
        }
    }

    public String localizeSubSeason(Season.SubSeason subSeason) {
        switch (subSeason) {
            case EARLY_SPRING: return "reskillable.hud.early_spring";
            case MID_SPRING: return "reskillable.hud.mid_spring";
            case LATE_SPRING: return "reskillable.hud.late_spring";
            case EARLY_SUMMER: return "reskillable.hud.early_summer";
            case MID_SUMMER: return "reskillable.hud.mid_summer";
            case LATE_SUMMER: return "reskillable.hud.late_summer";
            case EARLY_AUTUMN: return "reskillable.hud.early_autumn";
            case MID_AUTUMN: return "reskillable.hud.mid_autumn";
            case LATE_AUTUMN: return "reskillable.hud.late_autumn";
            case EARLY_WINTER: return "reskillable.hud.early_winter";
            case MID_WINTER: return "reskillable.hud.mid_winter";
            case LATE_WINTER: return "reskillable.hud.late_winter";
            default: return "reskillable.hud.invalid_subseason";
        }
    }

    public int colorizeSeasonText(Season.SubSeason subSeason) {
        switch (subSeason) {
            case EARLY_SPRING:
            case MID_SPRING:
            case LATE_SPRING:
                return 0xDE5479;
            case EARLY_SUMMER:
            case MID_SUMMER:
            case LATE_SUMMER:
                return 0xF4D35E;
            case EARLY_AUTUMN:
            case MID_AUTUMN:
            case LATE_AUTUMN:
                return 0xEE964B;
            case EARLY_WINTER:
            case MID_WINTER:
            case LATE_WINTER:
                return 0x2A45CB;
            //return 0x549BDE;
            default: return 0xffffffff;
        }
    }
}


