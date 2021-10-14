package codersafterdark.reskillable.client.gui;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerProfessionInfo;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.client.core.RenderHelper;
import codersafterdark.reskillable.client.gui.handler.InventoryTabHandler;
import codersafterdark.reskillable.client.gui.handler.KeyBindings;
import codersafterdark.reskillable.common.lib.LibMisc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static codersafterdark.reskillable.client.core.RenderHelper.renderTooltip;

public class GuiProfessions extends GuiScreen {
    public static final ResourceLocation SKILLS_RES = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/professions.png");
    public static final ResourceLocation SELECTED_PROFS = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/selected_professions.png");

    private int guiWidth, guiHeight;
    private Profession hoveredProfession;

    private int offset;

    private int left;
    private int top;
    private int lastY;

    private Map<Integer, Profession> enabledProfessions = new HashMap<>();
    private Map<Integer, Profession> professions = new HashMap<>();

    public GuiProfessions() {
        ReskillableRegistries.PROFESSIONS.getValuesCollection().stream().filter(Profession::isEnabled).forEach(enabledProfession -> enabledProfessions.put(enabledProfession.getGuiIndex(), enabledProfession));
    }

    @Override
    public void initGui() {
        guiWidth = 176;
        guiHeight = 166;

        buttonList.clear();
        InventoryTabHandler.addTabs(this, buttonList);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        mc.renderEngine.bindTexture(SKILLS_RES);
        GlStateManager.color(1F, 1F, 1F);

        left = width / 2 - guiWidth / 2;
        top = height / 2 - guiHeight / 2;
        drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);

        PlayerData data = PlayerDataHandler.get(mc.player);

        hoveredProfession = null;

        professions = new HashMap<>();

        enabledProfessions.entrySet().stream().filter(entry -> !entry.getValue().isHidden()).forEach(entry -> professions.put(entry.getKey(), entry.getValue()));
        int index = 0;

        if (data.hasSecondaryProfession()) {
            for (int j = offset; j < data.getUnlockedProfessions().size() && index < 2; j ++) {
                Profession profession = data.getUnlockedProfessions().get(j);

                int i = index++;
                int w = 79;
                int h = 110;
                int x = left + (i % 2) * (w + 3) + 8;
                int y = top + (i / 2) * (h + 3) + 18;

                lastY = y;
                int u = 176;
                int v = 0;

                if (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h) {
                    v += 110;
                    hoveredProfession = profession;
                }

                mc.renderEngine.bindTexture(SELECTED_PROFS);
                GlStateManager.color(1F, 1F, 1F);
                drawTexturedModalRect(x, y, u, v, w, h);
                drawProfession(x + 5, y + 9, profession);

                mc.fontRenderer.drawString(profession.getName(), x + 26, y + 12, 0xFFFFFF);
            }
        } else {
            for (int j = offset; j < professions.size() && index < 8; j++) {
                Profession profession = professions.get(j);
                PlayerProfessionInfo professionInfo = data.getProfessionInfo(profession);

                int i = index++;
                int w = 79;
                int h = 32;
                int x = left + (i % 2) * (w + 3) + 8;
                int y = top + (i / 2) * (h + 3) + 18;

                lastY = y;
                int u = 0;
                int v = guiHeight;

                if (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h) {
                    u += w;
                    hoveredProfession = profession;
                }
                if (professionInfo.getLevel() > 0) {
                    v += h;
                }

                mc.renderEngine.bindTexture(SKILLS_RES);
                GlStateManager.color(1F, 1F, 1F);
                drawTexturedModalRect(x, y, u, v, w, h);
                drawProfession(x + 5, y + 9, profession);

                mc.fontRenderer.drawString(profession.getName(), x + 26, y + 12, 0xFFFFFF);
                //mc.fontRenderer.drawString(professionInfo.getLevel() + "/" + profession.getCap(), x + 26, y + 17, 0x888888);
            }
        }

        GL11.glColor4f(1, 1, 1, 1);
        drawScrollButtonsTop(left + 49, top + 14);
        if (!data.hasSecondaryProfession()) {
            drawScrollButtonsBottom(left + 49, lastY + 32);
        } else {
            drawScrollButtonsBottom(left + 49, lastY + 110);
        }

        String professionsStr = new TextComponentTranslation("reskillable.misc.professions").getUnformattedComponentText();
        fontRenderer.drawString(professionsStr, width / 2 - fontRenderer.getStringWidth(professionsStr) / 2, top + 5, 4210752);
        String infoString = new TextComponentTranslation("reskillable.misc.profession_info").getUnformattedComponentText();
        if (!data.hasSecondaryProfession()) {
            fontRenderer.drawSplitString(infoString, left + 6, top + 126, guiWidth - 8, 4210752);
        }

        if (hoveredProfession != null) {
            makeProfessionTooltip(mouseX, mouseY);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public static void drawProfession(int x, int y, Profession profession) {
        Minecraft mc = Minecraft.getMinecraft();
        int rank = PlayerDataHandler.get(mc.player).getProfessionInfo(profession).getRank();
        if (profession.hasCustomSprites()) {
            ResourceLocation sprite = profession.getSpriteLocation(rank);
            if (sprite != null) {
                mc.renderEngine.bindTexture(sprite);
                drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
            }
        } else {
            mc.renderEngine.bindTexture(profession.getSpriteLocation());
            Pair<Integer, Integer> pair = profession.getSpriteFromRank(rank);
            RenderHelper.drawTexturedModalRect(x, y, 1, pair.getKey(), pair.getValue(), 16, 16, 1f / 64, 1f / 64);
        }
    }

    public static void drawScrollButtonsTop(int x, int y) {
        Minecraft.getMinecraft().renderEngine.bindTexture(SKILLS_RES);
        RenderHelper.drawTexturedModalRect(x, y, 1, 0, 230, 80, 4);
    }

    public static void drawScrollButtonsBottom(int x, int y) {
        Minecraft.getMinecraft().renderEngine.bindTexture(SKILLS_RES);
        RenderHelper.drawTexturedModalRect(x, y, 1, 0, 235, 80, 4);
    }

    private void makeProfessionTooltip(int mouseX, int mouseY) {
        List<String> tooltip = new ArrayList<>();

        tooltip.add(TextFormatting.YELLOW + hoveredProfession.getName());

        if (isShiftKeyDown()) {
            addLongStringToTooltip(tooltip, hoveredProfession.getDescription(), guiWidth);
        } else {
            tooltip.add(TextFormatting.GRAY + new TextComponentTranslation("reskillable.misc.shift").getUnformattedComponentText());
            tooltip.add("");
        }

        renderTooltip(mouseX, mouseY, tooltip);
    }

    private void addLongStringToTooltip(List<String> tooltip, String longStr, int maxLen) {
        String[] tokens = longStr.split(" ");
        String curr = TextFormatting.GRAY.toString();
        int i = 0;

        while (i < tokens.length) {
            while (fontRenderer.getStringWidth(curr) < maxLen && i < tokens.length) {
                curr = curr + tokens[i] + ' ';
                i++;
            }
            tooltip.add(curr);
            curr = TextFormatting.GRAY.toString();
        }
        tooltip.add(curr);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        } else if (keyCode == KeyBindings.keyProfessionGui.getKeyCode() || keyCode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.displayGuiScreen(null);

            if (this.mc.currentScreen != null) {
                this.mc.setIngameFocus();
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0 && hoveredProfession != null) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            GuiProfessionInfo gui = new GuiProfessionInfo(hoveredProfession);
            mc.displayGuiScreen(gui);
        }
        if (mouseButton == 0) {
            if (mouseX >= left + 49 && mouseX <= left + 128 && mouseY >= top + 14) {
                if (mouseY <= top + 18) {
                    scrollUp();
                } else if (mouseY <= lastY + 36) {
                    scrollDown();
                }
            }
        }
    }

    private void scrollUp() {
        offset = Math.max(offset - 2, 0);
    }

    private void scrollDown() {
        int off = 2;
        if (professions.size() % 2 == 1) {
            off = 1;
        }
        offset = Math.min(offset + 2, Math.max(professions.size() - 6 - off, 0));
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (Mouse.getEventDWheel() > 0) {
            scrollUp();
        } else if (Mouse.getEventDWheel() < 0) {
            scrollDown();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
