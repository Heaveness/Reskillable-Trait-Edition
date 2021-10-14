package codersafterdark.reskillable.client.gui;

import codersafterdark.reskillable.api.data.*;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.client.gui.button.GuiButtonLevelUp;
import codersafterdark.reskillable.client.gui.handler.InventoryTabHandler;
import codersafterdark.reskillable.client.gui.handler.KeyBindings;
import codersafterdark.reskillable.common.lib.LibMisc;
import codersafterdark.reskillable.common.network.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static codersafterdark.reskillable.client.core.RenderHelper.renderTooltip;

public class GuiProfessionInfo extends GuiScreen {

    public static final ResourceLocation PROFESSION_INFO_RES = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/profession_info1.png");
    public static final ResourceLocation PROFESSION_INFO_RES2 = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/profession_info1b.png");
    public static final ResourceLocation PROFESSION_INFO_RES3 = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/profession_info2.png");

    private final Profession profession;

    private int guiWidth, guiHeight;
    private ResourceLocation sprite;

    private GuiButtonLevelUp levelUpButton;
    private Talent hoveredTalent;
    private boolean hoveredLevelButton;
    private boolean hoveredSwapButton;
    private boolean canPurchase;
    private boolean canUpgrade;
    private int color;
    private int professionIndex;

    public GuiProfessionInfo(Profession profession) {this.profession = profession;}

    /** Called to load the basic GUI parameters */
    @Override
    public void initGui() {
        guiWidth = 256;
        guiHeight = 235;

        int left = width / 2 - guiWidth / 2;
        int top = height / 2 - guiHeight / 2;

        buttonList.clear();
        if (ConfigHandler.enableLevelUp && profession.hasLevelButton()) {
            buttonList.add(levelUpButton = new GuiButtonLevelUp(left - 32, top + 196));
        }

        InventoryTabHandler.addTabs(this, buttonList);
        sprite = profession.getBackground();
    }

    /** Called when the screen is drawn */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Dims the background
        drawDefaultBackground();

        // Defines the top-left anchor for the GUI; dependent on current screen size
        int left = width / 2 - guiWidth / 2;
        int top = height / 2 - guiHeight / 2;

        PlayerData data = PlayerDataHandler.get(mc.player);
        PlayerProfessionInfo professionInfo = data.getProfessionInfo(profession);

        // Draws the iterative background fill for the tree windows
        mc.renderEngine.bindTexture(sprite);
        GlStateManager.color(0.5F, 0.5F, 0.5F);
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 12; j++) {
                drawTexturedRec(left - 9 + i * 16, top + 6 + j * 16, 16, 16);
            }
        }

        // Draws the background plate and window borders in pieces, centering and fitting the unorthodox GUI width
        GlStateManager.color(1F, 1F, 1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        if (ConfigHandler.enableLevelUp & profession.hasLevelButton()) {
            mc.renderEngine.bindTexture(PROFESSION_INFO_RES);
            drawTexturedModalRect(left - 42, top, 0, 0, guiWidth, guiHeight);
        } else {            // The left side of the GUI is drawn with a texture without the level button, if appropriate
            mc.renderEngine.bindTexture(PROFESSION_INFO_RES2);
            drawTexturedModalRect(left - 42, top, 0, 0, guiWidth, guiHeight);
        }
        mc.renderEngine.bindTexture(PROFESSION_INFO_RES3);  // The right side of the GUI is drawn and offset the appropriate amount
        drawTexturedModalRect(left + 214, top, 0, 0, guiWidth - 190, guiHeight);

        // Draw the Skill Bar Fill
        float barUnit = 183.0f / (float)profession.getCap();
        int barHeight = (int)(barUnit * (float)professionInfo.getLevel());
        color = profession.getColor() == 0 ? 13619151 : profession.getColor();
        drawTexturedColoredRect(left - 33, top + 192 - barHeight, 66, 233 - barHeight, 16, barHeight, color);

        // Draw the bottom-left Profession icon
        GuiProfessions.drawProfession(left - 33, top + 212, profession);

        // Draw the information strings
        //String levelStr = String.format("%d/%d [ %s ]", professionInfo.getLevel(), profession.getCap(), new TextComponentTranslation("reskillable.rank." + professionInfo.getRank()).getUnformattedComponentText());
        String levelStr = String.format("%s %d/%d", new TextComponentTranslation("reskillable.misc.level").getUnformattedComponentText(), professionInfo.getLevel(), profession.getCap());
        drawCenteredString(mc.fontRenderer,TextFormatting.BOLD + profession.getName(), left + 128, top + 216, 15921906);
        mc.fontRenderer.drawString(levelStr, left -10 , top + 216, 4210752);
        mc.fontRenderer.drawString(new TextComponentTranslation("reskillable.misc.skill_points", professionInfo.getProfessionPoints()).getUnformattedComponentText(), left + 192, top + 216, 4210752);

        // Draw the sub profession names
        List<Profession.SubProfession> subProfessions = profession.getAllSubProfessions();
        for (int i = 0; i < subProfessions.toArray().length; i++) {
            if (subProfessions.get(i) != null) {
                int guiIndex = subProfessions.get(i).getGuiIndex();
                drawCenteredString(mc.fontRenderer, new TextComponentTranslation(subProfessions.get(i).getRegistryName()).getUnformattedComponentText(), left + 35 + guiIndex * 93, top + 199, 15921906);
            }
        }

        // Draw a tooltip when the level-up button is hovered
        int cost = professionInfo.getLevelUpCost();
        if (ConfigHandler.enableLevelUp && profession.hasLevelButton()) {
            drawButtonTooltip(professionInfo, mouseX, mouseY);
            levelUpButton.setCost(cost);
        }

        //Draw the talents
        hoveredTalent = null;
        profession.getTalents().forEach(t -> drawTalent(professionInfo, t, mouseX, mouseY));

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (hoveredTalent != null) {
            makeTalentTooltip(data, professionInfo, mouseX, mouseY);
        }
    }

    /** Used to draw the iterative background fill */
    public void drawTexturedRec(int x, int y, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double) x, (double) (y + height), (double) this.zLevel).tex(0, 1).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex(1, 1).endVertex();
        bufferbuilder.pos((double) (x + width), (double) y, (double) this.zLevel).tex(1, 0).endVertex();
        bufferbuilder.pos((double) x, (double) y, (double) this.zLevel).tex(0, 0).endVertex();
        tessellator.draw();
    }

    /** Draws a colored overlay, used to provide color to the skill bar */
    private void drawTexturedColoredRect(int x, int y, int textureX, int textureY, int width, int height, int color) {
        float a = ((color >> 24) & 255) / 255f;
        if (a <= 0f)
            a = 1f;
        float r = ((color >> 16) & 255) / 255f;
        float g = ((color >> 8) & 255) / 255f;
        float b = (color & 255) / 255f;
        GlStateManager.color(r, g, b, a);
        drawTexturedModalRect(x, y, textureX, textureY, width, height);
        GlStateManager.color(1f, 1f, 1f, 1f);
    }

    private void drawTalent(PlayerProfessionInfo info, Talent talent, int mx, int my) {
        PlayerTalentInfo talentInfo = PlayerDataHandler.get(Minecraft.getMinecraft().player).getTalentInfo(talent);

        int x = width / 2 - guiWidth / 2 - 5 + 93 * talent.getSubProfession().getGuiIndex() + talent.getX() * 27;
        int y = height / 2 - guiHeight / 2 + 10 + talent.getY() * 37;
        mc.renderEngine.bindTexture(PROFESSION_INFO_RES3);
        boolean unlocked = info.isUnlocked(talent);

        int u = 84;
        int v = 133;

        /*
        if (talent.hasSpikes()) {
            u += 26;
        }
        */

        // Unlocked, but cannot purchase
        if (unlocked) {
            if (talentInfo.isCapped()) {
                v += 22 * 3;
            } else if (info.getProfessionPoints() >= talent.getCost()) {
                v += 22 * 2;
            } else if (info.getProfessionPoints() < talent.getCost()) {
                v += 22;
            }
        }

        GlStateManager.color(1F, 1F, 1F);
        drawTexturedModalRect(x, y, u, v, 22, 22);

        mc.renderEngine.bindTexture(talent.getIcon());
        drawModalRectWithCustomSizedTexture(x + 1, y + 1, 0, 0, 20, 20, 20, 20);

        if (mx >= x && my >= y && mx < x + 24 && my < y + 24) {
            canPurchase = !unlocked && info.getProfessionPoints() >= talent.getCost();
            canUpgrade = !talentInfo.isCapped() && info.getProfessionPoints() >= talent.getCost();
            hoveredTalent = talent;
        }
    }

    /** Draws the Talent tooltip */
    private void makeTalentTooltip(PlayerData data, PlayerProfessionInfo info, int mouseX, int mouseY) {
        List<String> tooltip = new ArrayList<>();
        TextFormatting tf = hoveredTalent.hasSpikes() ? TextFormatting.AQUA : TextFormatting.YELLOW;

        tooltip.add(tf + hoveredTalent.getName());

        if (isShiftKeyDown()) {
            addLongStringToTooltip(tooltip, hoveredTalent.getDescription(), guiWidth);
        } else {
            tooltip.add(TextFormatting.GRAY + new TextComponentTranslation("reskillable.misc.shift").getUnformattedComponentText());
            tooltip.add("");
        }

        if (!info.isUnlocked(hoveredTalent)) {
            hoveredTalent.getRequirements().addRequirementsToTooltip(data, tooltip);
        } else {
            tooltip.add(TextFormatting.GREEN + new TextComponentTranslation("reskillable.misc.unlocked").getUnformattedComponentText());
        }

        int rank = data.getTalentInfo(hoveredTalent).getRank();

        tooltip.add(TextFormatting.GRAY + new TextComponentTranslation("reskillable.misc.skill_points", hoveredTalent.getCost()).getUnformattedComponentText());
        tooltip.add(TextFormatting.GRAY + new TextComponentTranslation("reskillable.misc.talent_rank", rank, hoveredTalent.getCap()).getUnformattedComponentText());

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

    /** Draws the tooltip of the level-up button, displaying the cost to level up */
    private void drawButtonTooltip(PlayerProfessionInfo info, int mx, int my) {
        int x = width / 2 - guiWidth / 2 - 32;
        int y = height / 2 - guiHeight / 2 + 196;

        if (mx >= x && my >= y && mx <= x + 16 && my < y + 16) {
            hoveredLevelButton = true;
        } else {
            hoveredLevelButton = false;
        }

        String costStr = String.format("%s %d %s", new TextComponentTranslation("reskillable.misc.cost").getUnformattedComponentText(), info.getLevelUpCost(), new TextComponentTranslation("reskillable.misc.levels").getUnformattedComponentText());
        String desc;

        if (info.getLevel() < 1) {
            desc = new TextComponentTranslation("reskillable.misc.profession_button", profession.getName()).getUnformattedComponentText();
        } else {
            desc = null;
        }

        if (info.isCapped()) {
            costStr = new TextComponentTranslation("reskillable.misc.capped").getUnformattedComponentText();
        }

        GlStateManager.color(1F, 1F, 1F);
        if (hoveredLevelButton) {
            levelUpButton.drawLevelButtonTooltip(desc, costStr, mx, my);
        }
    }

    /** Called when the level-up button is pressed, sending a packet to the server containing the level information */
    @Override
    protected void actionPerformed(GuiButton button) {
        if (ConfigHandler.enableLevelUp && profession.hasLevelButton() && button == levelUpButton) {
            MessageLevelUpProfession message = new MessageLevelUpProfession(profession.getRegistryName());
            PacketHandler.INSTANCE.sendToServer(message);
        }
    }

    /** Called when the mouse is clicked to supply sounds, detect when a talent is unlocked, and return to the previous menu */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0 && hoveredTalent != null && canPurchase) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            MessageUnlockTalent message = new MessageUnlockTalent(profession.getRegistryName(), hoveredTalent.getRegistryName());
            PacketHandler.INSTANCE.sendToServer(message);
        } else if (mouseButton == 0 && hoveredTalent != null && canUpgrade) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            MessageUpgradeTalent message = new MessageUpgradeTalent(profession.getRegistryName(), hoveredTalent.getRegistryName());
            PacketHandler.INSTANCE.sendToServer(message);
        } else if (mouseButton == 1 || mouseButton == 3) {
            mc.displayGuiScreen(new GuiProfessions());
        }
    }

    /** Called when either the profession keybind or RM2 are pressed */
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

    /** Opening the Profession Info GUI does not pause the game */
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
