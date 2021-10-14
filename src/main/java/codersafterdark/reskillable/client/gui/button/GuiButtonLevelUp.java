package codersafterdark.reskillable.client.gui.button;

import static codersafterdark.reskillable.client.core.RenderHelper.renderTooltip;

import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.client.gui.GuiSkillInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonLevelUp extends GuiButton {
    int cost;
    float renderTicks;

    public GuiButtonLevelUp(int x, int y) {
        super(0, x, y, 14, 14, "");
        cost = Integer.MAX_VALUE;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float f) {
        enabled = mc.player.experienceLevel >= cost || mc.player.isCreative();

        if (ConfigHandler.enableLevelUp) {
            if (enabled) {
                GlStateManager.color(1F, 1F, 1F);
                mc.renderEngine.bindTexture(GuiSkillInfo.SKILL_INFO_RES);

                int x = this.x;
                int y = this.y;
                int u = 176;
                int v = 0;
                int w = width;
                int h = height;

                if (mouseX > this.x && mouseY > this.y && mouseX < this.x + width && mouseY < this.y + height) {
                    v += h;
                } else {
                    float speedModifier = 4;
                    GlStateManager.color(1, 1, 1, (float) (Math.sin(mc.player.ticksExisted / speedModifier) + 1) / 2);
                }
                drawTexturedModalRect(x, y, u, v, w, h);
            }
        }
    }

    public void drawLevelButtonTooltip(String desc, String cost, int mouseX, int mouseY) {
        List<String> tooltip = new ArrayList<>();

        if (!(desc == null)) {tooltip.add(TextFormatting.YELLOW + desc);}
        tooltip.add(TextFormatting.GRAY + cost);

        renderTooltip(mouseX, mouseY, tooltip);
    }

}