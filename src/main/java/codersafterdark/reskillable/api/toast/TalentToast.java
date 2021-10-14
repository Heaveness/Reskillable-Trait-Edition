package codersafterdark.reskillable.api.toast;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.util.text.TextComponentTranslation;

public class TalentToast extends AbstractToast{
    private final Talent talent;

    public TalentToast(Talent talent) {
        super(talent.getName(), new TextComponentTranslation("reskillable.toast.unlockable_desc").getUnformattedComponentText());
        this.talent = talent;
    }

    @Override
    protected void renderImage(GuiToast guiToast) {
        bindImage(guiToast, talent.getIcon());
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    protected boolean hasImage() {
        return true;
    }

}
