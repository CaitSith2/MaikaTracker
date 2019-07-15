/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg4e.maikatracker;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.LogManager;
import sg4e.ff4stats.fe.FlagSet;

/**
 *
 * @author CaitSith2
 */
public class BossLabel extends StativeLabel {
    
    private static final List<BossLabel> bossNames = new ArrayList<>();
    
    private final String bossName;
    private final ImageIcon greyBoss;
    private final ImageIcon colorBoss;
    private final ImageIcon checkedBoss;
    
    private JPanel holder;
    private BossLabel bossLocation;
    private BossLabel contains;
    public static final BufferedImage checkedImage;
    
    static {
        BufferedImage cImage;
        try {
            cImage = MaikaTracker.loadImageResource("bosses/checkmark.png");
        }
        catch (Exception ex) {
            LogManager.getLogger().error("Error loading checkmark image.", ex);
            cImage = null;
        }
        checkedImage = cImage;
    }
    
    public static ImageIcon CheckMarkIcon(ImageIcon colorBoss) {
        if (checkedImage == null)
            return null;
        BufferedImage image = new BufferedImage(colorBoss.getIconWidth(), colorBoss.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        float[] scales = {.5f, .5f, .5f, 1f};
        float[] offsets = new float[4];
        RescaleOp op = new RescaleOp(scales, offsets, null); 
        
        Graphics2D cImage = (Graphics2D) image.getGraphics();
        cImage.drawImage(colorBoss.getImage(), 0, 0, null);
        cImage.dispose();
        image = op.filter(image, null);
        cImage = (Graphics2D) image.getGraphics();
        cImage.drawImage(checkedImage, 0, 0, null);
        cImage.dispose();
        
        return new ImageIcon(image);
    }
    
    //loadBossIcon("2Soldier", "2Soldier", "Baron Soldiers");
    public BossLabel(String imageName, String bossName) {
        super();
        
        greyBoss = new ImageIcon(MaikaTracker.loadImageResource("bosses/grayscale/FFIVFE-Bosses-" + imageName + "-Gray.png"));
        colorBoss = new ImageIcon(MaikaTracker.loadImageResource("bosses/color/FFIVFE-Bosses-" + imageName + "-Color.png"));
        checkedBoss = CheckMarkIcon(colorBoss);
        
        setCheckedBoss(true);
        
        setToolTipText(bossName);
        this.bossName = bossName;
        bossNames.add(this);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    
                    JPopupMenu locationMenu = getBossNameMenu(bn -> {setBossLocation(bn);});
                    if(bossLocation != null && (MaikaTracker.tracker.flagsetContains("B"))) {
                        locationMenu.add(new JSeparator(), 0);
                        JMenuItem resetMenu = new JMenuItem("Reset");
                        resetMenu.addActionListener((ae) -> setBossLocation(null));
                        locationMenu.add(resetMenu, 0);
                    }
                    locationMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    
    @Override
    public void setBackground(Color color) {
        super.setBackground(color);
        if(holder != null)
            holder.setBackground(color);
    }
    
    @Override
    public void reset() {
        super.reset();
        setBossLocation((MaikaTracker.tracker.flagsetContains("B")) ? null : this);
    }
    
    public final void setCheckedBoss(boolean checked) {
        if(checked && checkedBoss != null)
            setNewIconState(greyBoss, colorBoss, checkedBoss);
        else
            setNewIconState(greyBoss, colorBoss);
    }
    
    private JPopupMenu getBossNameMenu(Consumer<BossLabel> actionOnEachItem) {
        JPopupMenu bossMenu = new JPopupMenu("Boss Locations");
        bossNames.forEach(bn -> {
            if(bn.contains == null)
                bossMenu.add(bn.bossName).addActionListener((ae) -> actionOnEachItem.accept(bn));
        });
        return bossMenu;
    }
    
    private void setBossLocation(BossLabel label) {        
        if(bossLocation != null) {
            bossLocation.contains = null;
            bossLocation = null;
        }
        
        if(label == null) {
            setToolTipText(bossName);
        }
        else {
            setToolTipText("<html>" + bossName + "<br>Located at " + label.bossName + "</html>");
            label.contains = this;
            bossLocation = label;
        }
    }
    
    public JPanel getHolder() {
        if(holder == null) {
            holder = new JPanel(new FlowLayout());
            holder.add(this);
        }
        return holder;
    }
}
