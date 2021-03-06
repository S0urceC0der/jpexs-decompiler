/*
 *  Copyright (C) 2010-2015 JPEXS
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jpexs.decompiler.flash.gui;

import com.jpexs.decompiler.flash.AbortRetryIgnoreHandler;
import com.jpexs.decompiler.flash.ApplicationInfo;
import com.jpexs.decompiler.flash.EventListener;
import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.SWFBundle;
import com.jpexs.decompiler.flash.abc.ABC;
import com.jpexs.decompiler.flash.abc.RenameType;
import com.jpexs.decompiler.flash.abc.ScriptPack;
import com.jpexs.decompiler.flash.abc.types.traits.Trait;
import com.jpexs.decompiler.flash.configuration.Configuration;
import com.jpexs.decompiler.flash.configuration.ConfigurationItem;
import com.jpexs.decompiler.flash.dumpview.DumpInfo;
import com.jpexs.decompiler.flash.dumpview.DumpInfoSwfNode;
import com.jpexs.decompiler.flash.exporters.BinaryDataExporter;
import com.jpexs.decompiler.flash.exporters.FontExporter;
import com.jpexs.decompiler.flash.exporters.FrameExporter;
import com.jpexs.decompiler.flash.exporters.ImageExporter;
import com.jpexs.decompiler.flash.exporters.MorphShapeExporter;
import com.jpexs.decompiler.flash.exporters.MovieExporter;
import com.jpexs.decompiler.flash.exporters.ShapeExporter;
import com.jpexs.decompiler.flash.exporters.SoundExporter;
import com.jpexs.decompiler.flash.exporters.TextExporter;
import com.jpexs.decompiler.flash.exporters.modes.BinaryDataExportMode;
import com.jpexs.decompiler.flash.exporters.modes.FontExportMode;
import com.jpexs.decompiler.flash.exporters.modes.FramesExportMode;
import com.jpexs.decompiler.flash.exporters.modes.ImageExportMode;
import com.jpexs.decompiler.flash.exporters.modes.MorphShapeExportMode;
import com.jpexs.decompiler.flash.exporters.modes.MovieExportMode;
import com.jpexs.decompiler.flash.exporters.modes.ScriptExportMode;
import com.jpexs.decompiler.flash.exporters.modes.ShapeExportMode;
import com.jpexs.decompiler.flash.exporters.modes.SoundExportMode;
import com.jpexs.decompiler.flash.exporters.modes.TextExportMode;
import com.jpexs.decompiler.flash.exporters.script.AS2ScriptExporter;
import com.jpexs.decompiler.flash.exporters.settings.BinaryDataExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.FontExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.FramesExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.ImageExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.MorphShapeExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.MovieExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.ShapeExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.SoundExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.TextExportSettings;
import com.jpexs.decompiler.flash.exporters.swf.SwfJavaExporter;
import com.jpexs.decompiler.flash.exporters.swf.SwfXmlExporter;
import com.jpexs.decompiler.flash.gui.abc.ABCPanel;
import com.jpexs.decompiler.flash.gui.abc.ClassesListTreeModel;
import com.jpexs.decompiler.flash.gui.abc.DeobfuscationDialog;
import com.jpexs.decompiler.flash.gui.action.ActionPanel;
import com.jpexs.decompiler.flash.gui.dumpview.DumpTree;
import com.jpexs.decompiler.flash.gui.dumpview.DumpTreeModel;
import com.jpexs.decompiler.flash.gui.dumpview.DumpViewPanel;
import com.jpexs.decompiler.flash.gui.player.FlashPlayerPanel;
import com.jpexs.decompiler.flash.gui.tagtree.TagTree;
import com.jpexs.decompiler.flash.gui.tagtree.TagTreeModel;
import com.jpexs.decompiler.flash.gui.timeline.TimelineViewPanel;
import com.jpexs.decompiler.flash.helpers.Freed;
import com.jpexs.decompiler.flash.importers.BinaryDataImporter;
import com.jpexs.decompiler.flash.importers.ImageImporter;
import com.jpexs.decompiler.flash.importers.ShapeImporter;
import com.jpexs.decompiler.flash.importers.SwfXmlImporter;
import com.jpexs.decompiler.flash.importers.TextImporter;
import com.jpexs.decompiler.flash.tags.ABCContainerTag;
import com.jpexs.decompiler.flash.tags.DefineBinaryDataTag;
import com.jpexs.decompiler.flash.tags.DefineSoundTag;
import com.jpexs.decompiler.flash.tags.DefineSpriteTag;
import com.jpexs.decompiler.flash.tags.DoActionTag;
import com.jpexs.decompiler.flash.tags.DoInitActionTag;
import com.jpexs.decompiler.flash.tags.FileAttributesTag;
import com.jpexs.decompiler.flash.tags.Tag;
import com.jpexs.decompiler.flash.tags.base.ASMSource;
import com.jpexs.decompiler.flash.tags.base.BoundedTag;
import com.jpexs.decompiler.flash.tags.base.ButtonTag;
import com.jpexs.decompiler.flash.tags.base.CharacterTag;
import com.jpexs.decompiler.flash.tags.base.DrawableTag;
import com.jpexs.decompiler.flash.tags.base.FontTag;
import com.jpexs.decompiler.flash.tags.base.ImageTag;
import com.jpexs.decompiler.flash.tags.base.MissingCharacterHandler;
import com.jpexs.decompiler.flash.tags.base.MorphShapeTag;
import com.jpexs.decompiler.flash.tags.base.ShapeTag;
import com.jpexs.decompiler.flash.tags.base.SoundStreamHeadTypeTag;
import com.jpexs.decompiler.flash.tags.base.SoundTag;
import com.jpexs.decompiler.flash.tags.base.TextImportErrorHandler;
import com.jpexs.decompiler.flash.tags.base.TextTag;
import com.jpexs.decompiler.flash.tags.text.TextAlign;
import com.jpexs.decompiler.flash.tags.text.TextParseException;
import com.jpexs.decompiler.flash.timeline.DepthState;
import com.jpexs.decompiler.flash.timeline.Frame;
import com.jpexs.decompiler.flash.timeline.TagScript;
import com.jpexs.decompiler.flash.timeline.Timeline;
import com.jpexs.decompiler.flash.timeline.Timelined;
import com.jpexs.decompiler.flash.treeitems.FolderItem;
import com.jpexs.decompiler.flash.treeitems.HeaderItem;
import com.jpexs.decompiler.flash.treeitems.SWFList;
import com.jpexs.decompiler.flash.treeitems.TreeItem;
import com.jpexs.decompiler.flash.types.MATRIX;
import com.jpexs.decompiler.flash.types.RECT;
import com.jpexs.decompiler.flash.types.sound.SoundFormat;
import com.jpexs.decompiler.flash.xfl.FLAVersion;
import com.jpexs.helpers.CancellableWorker;
import com.jpexs.helpers.Helper;
import com.jpexs.helpers.Path;
import com.jpexs.helpers.ProgressListener;
import com.jpexs.helpers.SerializableImage;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreePath;

/**
 *
 * @author JPEXS
 */
public final class MainPanel extends JPanel implements ActionListener, TreeSelectionListener, SearchListener<TextTag>, Freed {

    private final MainFrame mainFrame;

    private final List<SWFList> swfs;

    private ABCPanel abcPanel;

    private ActionPanel actionPanel;

    private final JPanel welcomePanel;

    private final TimelineViewPanel timelineViewPanel;

    private final MainFrameStatusPanel statusPanel;

    private final MainFrameMenu mainMenu;

    private final JProgressBar progressBar = new JProgressBar(0, 100);

    private DeobfuscationDialog deobfuscationDialog;

    public TagTree tagTree;

    public DumpTree dumpTree;

    private final FlashPlayerPanel flashPanel;

    private final JPanel contentPanel;

    private final JPanel displayPanel;

    public FolderPreviewPanel folderPreviewPanel;

    private boolean isWelcomeScreen = true;

    private static final String CARDPREVIEWPANEL = "Preview card";

    private static final String CARDFOLDERPREVIEWPANEL = "Folder preview card";

    private static final String CARDEMPTYPANEL = "Empty card";

    private static final String CARDDUMPVIEW = "Dump view";

    private static final String CARDACTIONSCRIPTPANEL = "ActionScript card";

    private static final String CARDACTIONSCRIPT3PANEL = "ActionScript3 card";

    private static final String CARDHEADER = "Header card";

    private static final String DETAILCARDAS3NAVIGATOR = "Traits list";

    private static final String DETAILCARDEMPTYPANEL = "Empty card";

    private static final String SPLIT_PANE1 = "SPLITPANE1";

    private static final String WELCOME_PANEL = "WELCOMEPANEL";

    private static final String TIMELINE_PANEL = "TIMELINEPANEL";

    private final JSplitPane splitPane1;

    private final JSplitPane splitPane2;

    private boolean splitsInited = false;

    private JPanel detailPanel;

    private JTextField filterField = new MyTextField("");

    private JPanel searchPanel;

    private final PreviewPanel previewPanel;

    private final HeaderInfoPanel headerPanel;

    private DumpViewPanel dumpViewPanel;

    private final JPanel treePanel;

    private TreePanelMode treePanelMode;

    private AbortRetryIgnoreHandler errorHandler = new GuiAbortRetryIgnoreHandler();

    private CancellableWorker setSourceWorker;

    public TreeItem oldItem;

    private SearchDialog searchDialog;

    private SearchDialog replaceDialog;

    public static final String ACTION_SELECT_BKCOLOR = "SELECTCOLOR";

    public static final String ACTION_REPLACE = "REPLACE";

    // play morph shape in 2 second(s)
    public static final int MORPH_SHAPE_ANIMATION_LENGTH = 2;

    public static final int MORPH_SHAPE_ANIMATION_FRAME_RATE = 30;

    private static final Logger logger = Logger.getLogger(MainPanel.class.getName());

    public void setPercent(int percent) {
        progressBar.setValue(percent);
        progressBar.setVisible(true);
    }

    public void hidePercent() {
        if (progressBar.isVisible()) {
            progressBar.setVisible(false);
        }
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    static {
        try {
            File.createTempFile("temp", ".swf").delete(); //First call to this is slow, so make it first
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private static void addTab(JTabbedPane tabbedPane, Component tab, String title, Icon icon) {
        tabbedPane.add(tab);

        JLabel lbl = new JLabel(title);
        lbl.setIcon(icon);
        lbl.setIconTextGap(5);
        lbl.setHorizontalTextPosition(SwingConstants.RIGHT);

        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, lbl);
    }

    public void setStatus(String s) {
        statusPanel.setStatus(s);
    }

    public void setWorkStatus(String s, CancellableWorker worker) {
        statusPanel.setWorkStatus(s, worker);
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        JLabel welcomeToLabel = new JLabel(translate("startup.welcometo"));
        welcomeToLabel.setFont(welcomeToLabel.getFont().deriveFont(40));
        welcomeToLabel.setAlignmentX(0.5f);
        JPanel appNamePanel = new JPanel(new FlowLayout());
        JLabel jpLabel = new JLabel("JPEXS ");
        jpLabel.setAlignmentX(0.5f);
        jpLabel.setForeground(new Color(0, 0, 160));
        jpLabel.setFont(new Font("Tahoma", Font.BOLD, 50));
        jpLabel.setHorizontalAlignment(SwingConstants.CENTER);
        appNamePanel.add(jpLabel);

        JLabel ffLabel = new JLabel("Free Flash ");
        ffLabel.setAlignmentX(0.5f);
        ffLabel.setFont(new Font("Tahoma", Font.BOLD, 50));
        ffLabel.setHorizontalAlignment(SwingConstants.CENTER);
        appNamePanel.add(ffLabel);

        JLabel decLabel = new JLabel("Decompiler");
        decLabel.setAlignmentX(0.5f);
        decLabel.setForeground(Color.red);
        decLabel.setFont(new Font("Tahoma", Font.BOLD, 50));
        decLabel.setHorizontalAlignment(SwingConstants.CENTER);
        appNamePanel.add(decLabel);
        appNamePanel.setAlignmentX(0.5f);
        welcomePanel.add(Box.createGlue());
        welcomePanel.add(welcomeToLabel);
        welcomePanel.add(appNamePanel);
        JLabel startLabel = new JLabel(translate("startup.selectopen"));
        startLabel.setAlignmentX(0.5f);
        startLabel.setFont(startLabel.getFont().deriveFont(30));
        welcomePanel.add(startLabel);
        welcomePanel.add(Box.createGlue());
        return welcomePanel;
    }

    private JPanel createFolderPreviewCard() {
        JPanel folderPreviewCard = new JPanel(new BorderLayout());
        folderPreviewPanel = new FolderPreviewPanel(this, new ArrayList<TreeItem>());
        folderPreviewCard.add(new JScrollPane(folderPreviewPanel), BorderLayout.CENTER);

        return folderPreviewCard;
    }

    private JPanel createDumpPreviewCard() {
        JPanel dumpViewCard = new JPanel(new BorderLayout());
        dumpViewPanel = new DumpViewPanel(dumpTree);
        dumpViewCard.add(new JScrollPane(dumpViewPanel), BorderLayout.CENTER);

        return dumpViewCard;
    }

    public String translate(String key) {
        return mainFrame.translate(key);
    }

    public MainPanel(MainFrame mainFrame, MainFrameMenu mainMenu, FlashPlayerPanel flashPanel) {
        super();

        this.mainFrame = mainFrame;
        this.mainMenu = mainMenu;
        this.flashPanel = flashPanel;

        mainFrame.setTitle(ApplicationInfo.applicationVerName);

        setLayout(new BorderLayout());
        swfs = new ArrayList<>();

        detailPanel = new JPanel();
        detailPanel.setLayout(new CardLayout());
        JPanel whitePanel = new JPanel();
        whitePanel.setBackground(Color.white);
        detailPanel.add(whitePanel, DETAILCARDEMPTYPANEL);
        CardLayout cl2 = (CardLayout) (detailPanel.getLayout());
        cl2.show(detailPanel, DETAILCARDEMPTYPANEL);

        UIManager.getDefaults().put("TreeUI", BasicTreeUI.class.getName());
        tagTree = new TagTree(null, this);
        tagTree.addTreeSelectionListener(this);

        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(tagTree, DnDConstants.ACTION_COPY_OR_MOVE, new DragGestureListener() {
            @Override
            public void dragGestureRecognized(DragGestureEvent dge) {
                dge.startDrag(DragSource.DefaultCopyDrop, new Transferable() {
                    @Override
                    public DataFlavor[] getTransferDataFlavors() {
                        return new DataFlavor[]{DataFlavor.javaFileListFlavor};
                    }

                    @Override
                    public boolean isDataFlavorSupported(DataFlavor flavor) {
                        return flavor.equals(DataFlavor.javaFileListFlavor);
                    }

                    @Override
                    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                        if (flavor.equals(DataFlavor.javaFileListFlavor)) {
                            List<File> files;
                            String tempDir = System.getProperty("java.io.tmpdir");
                            if (!tempDir.endsWith(File.separator)) {
                                tempDir += File.separator;
                            }
                            Random rnd = new Random();
                            tempDir += "ffdec" + File.separator + "export" + File.separator + System.currentTimeMillis() + "_" + rnd.nextInt(1000);
                            File fTempDir = new File(tempDir);
                            if (!fTempDir.exists()) {
                                if (!fTempDir.mkdirs()) {
                                    if (!fTempDir.exists()) {
                                        throw new IOException("cannot create directory " + fTempDir);
                                    }
                                }
                            }

                            File ftemp = new File(tempDir);
                            ExportDialog exd = new ExportDialog(null);
                            files = exportSelection(errorHandler, tempDir, exd);
                            files.clear();

                            File[] fs = ftemp.listFiles();
                            files.addAll(Arrays.asList(fs));

                            Main.stopWork();

                            for (File f : files) {
                                f.deleteOnExit();
                            }
                            new File(tempDir).deleteOnExit();
                            return files;

                        }
                        return null;
                    }
                }, new DragSourceListener() {
                    @Override
                    public void dragEnter(DragSourceDragEvent dsde) {
                        enableDrop(false);
                    }

                    @Override
                    public void dragOver(DragSourceDragEvent dsde) {
                    }

                    @Override
                    public void dropActionChanged(DragSourceDragEvent dsde) {
                    }

                    @Override
                    public void dragExit(DragSourceEvent dse) {
                    }

                    @Override
                    public void dragDropEnd(DragSourceDropEvent dsde) {
                        enableDrop(true);
                    }
                });
            }
        });

        tagTree.createContextMenu();

        dumpTree = new DumpTree(null, this);
        dumpTree.addTreeSelectionListener(this);
        dumpTree.createContextMenu();

        statusPanel = new MainFrameStatusPanel(this);
        add(statusPanel, BorderLayout.SOUTH);

        displayPanel = new JPanel(new CardLayout());

        previewPanel = new PreviewPanel(this, flashPanel);
        displayPanel.add(previewPanel, CARDPREVIEWPANEL);
        displayPanel.add(createFolderPreviewCard(), CARDFOLDERPREVIEWPANEL);
        displayPanel.add(createDumpPreviewCard(), CARDDUMPVIEW);

        headerPanel = new HeaderInfoPanel();
        displayPanel.add(headerPanel, CARDHEADER);

        displayPanel.add(new JPanel(), CARDEMPTYPANEL);
        showCard(CARDEMPTYPANEL);

        searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.add(filterField, BorderLayout.CENTER);
        searchPanel.add(new JLabel(View.getIcon("search16")), BorderLayout.WEST);
        JLabel closeSearchButton = new JLabel(View.getIcon("cancel16"));
        closeSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                filterField.setText("");
                doFilter();
                searchPanel.setVisible(false);
            }
        });
        searchPanel.add(closeSearchButton, BorderLayout.EAST);
        treePanel = new JPanel(new BorderLayout());
        treePanel.add(searchPanel, BorderLayout.SOUTH);

        filterField.addActionListener(this);

        searchPanel.setVisible(false);

        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                doFilter();
            }
        });

        //displayPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, treePanel, detailPanel);
        splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane2, displayPanel);

        welcomePanel = createWelcomePanel();
        add(welcomePanel, BorderLayout.CENTER);

        splitPane1.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (splitsInited) {
                    Configuration.guiSplitPane1DividerLocation.set((Integer) pce.getNewValue());
                }
            }
        });

        splitPane2.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (detailPanel.isVisible()) {
                    Configuration.guiSplitPane2DividerLocation.set((Integer) pce.getNewValue());
                }
            }
        });

        timelineViewPanel = new TimelineViewPanel();

        CardLayout cl3 = new CardLayout();
        contentPanel = new JPanel(cl3);
        contentPanel.add(welcomePanel, WELCOME_PANEL);
        contentPanel.add(splitPane1, SPLIT_PANE1);
        contentPanel.add(timelineViewPanel, TIMELINE_PANEL);
        add(contentPanel);
        cl3.show(contentPanel, WELCOME_PANEL);

        tagTree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == 'F') && (e.isControlDown())) {
                    searchPanel.setVisible(true);
                    filterField.requestFocusInWindow();
                }
            }
        });
        detailPanel.setVisible(false);

        showView(getCurrentView());
        updateUi();

        //Opening files with drag&drop to main window
        enableDrop(true);
    }

    public void load(SWFList newSwfs, boolean first) {

        previewPanel.clear();

        for (SWF swf : newSwfs) {

            if (Configuration.autoRenameIdentifiers.get()) {
                try {
                    swf.deobfuscateIdentifiers(RenameType.TYPENUMBER);
                    swf.assignClassesToSymbols();
                    swf.clearScriptCache();
                    if (abcPanel != null) {
                        abcPanel.reload();
                    }
                    updateClassesList();
                } catch (InterruptedException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }

        showDetail(DETAILCARDEMPTYPANEL);
        showCard(CARDEMPTYPANEL);
        swfs.add(newSwfs);
        SWF swf = newSwfs.size() > 0 ? newSwfs.get(0) : null;
        if (swf != null) {
            updateUi(swf);
        }
        refreshTree();
    }

    private ABCPanel getABCPanel() {
        if (abcPanel == null) {
            abcPanel = new ABCPanel(this);
            displayPanel.add(abcPanel, CARDACTIONSCRIPT3PANEL);
            detailPanel.add(abcPanel.tabbedPane, DETAILCARDAS3NAVIGATOR);
        }
        return abcPanel;
    }

    private void ensureActionPanel() {
        if (actionPanel == null) {
            View.execInEventDispatch(new Runnable() {
                @Override
                public void run() {
                    actionPanel = new ActionPanel(MainPanel.this);
                    displayPanel.add(actionPanel, CARDACTIONSCRIPTPANEL);
                }
            });
        }
    }

    private ActionPanel getActionPanel() {
        ensureActionPanel();
        return actionPanel;
    }

    private void updateUi(final SWF swf) {

        mainFrame.setTitle(ApplicationInfo.applicationVerName + (Configuration.displayFileName.get() ? " - " + swf.getFileTitle() : ""));

        List<ABCContainerTag> abcList = swf.getAbcList();

        boolean hasAbc = !abcList.isEmpty();

        if (hasAbc) {
            getABCPanel().setAbc(abcList.get(0).getABC());
        }

        if (isWelcomeScreen) {
            CardLayout cl = (CardLayout) (contentPanel.getLayout());
            cl.show(contentPanel, SPLIT_PANE1);
            isWelcomeScreen = false;
        }

        mainMenu.updateComponents(swf);
    }

    private void updateUi() {
        if (!isWelcomeScreen && swfs.isEmpty()) {
            CardLayout cl = (CardLayout) (contentPanel.getLayout());
            cl.show(contentPanel, WELCOME_PANEL);
            isWelcomeScreen = true;
        }

        mainFrame.setTitle(ApplicationInfo.applicationVerName);
        mainMenu.updateComponents(null);
    }

    private boolean closeConfirmation(SWFList swfList) {
        if (swfList == null) {
            if (View.showConfirmDialog(this, translate("message.confirm.closeAll"), translate("message.warning"), JOptionPane.OK_CANCEL_OPTION, Configuration.showCloseConfirmation, JOptionPane.OK_OPTION) != JOptionPane.OK_OPTION) {
                return false;
            }
        } else {
            if (View.showConfirmDialog(this, translate("message.confirm.close").replace("{swfName}", swfList.toString()), translate("message.warning"), JOptionPane.OK_CANCEL_OPTION, Configuration.showCloseConfirmation, JOptionPane.OK_OPTION) != JOptionPane.OK_OPTION) {
                return false;
            }
        }

        return true;
    }

    public boolean closeAll() {
        boolean modified = false;
        for (SWFList swfList : swfs) {
            for (SWF swf : swfList) {
                if (swf.isModified()) {
                    modified = true;
                }
            }
        }

        if (modified) {
            boolean closeConfirmResult = closeConfirmation(swfs.size() == 1 ? swfs.get(0) : null);
            if (!closeConfirmResult) {
                return false;
            }
        }

        /*for (SWFList swfList : swfs) {
         for (SWF swf : swfList) {
         swf.clearTagSwfs();
         }
         }*/
        swfs.clear();
        oldItem = null;
        clear();
        updateUi();
        refreshTree();

        return true;
    }

    public boolean close(SWFList swfList) {
        boolean modified = false;
        for (SWF swf : swfList) {
            if (swf.isModified()) {
                modified = true;
            }
        }

        if (modified) {
            boolean closeConfirmResult = closeConfirmation(swfList);
            if (!closeConfirmResult) {
                return false;
            }
        }

        swfs.remove(swfList);
        oldItem = null;
        clear();
        updateUi();
        refreshTree();
        return true;
    }

    public void enableDrop(boolean value) {
        if (value) {
            setDropTarget(new DropTarget() {
                @Override
                public synchronized void drop(DropTargetDropEvent dtde) {
                    try {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        @SuppressWarnings("unchecked")
                        List<File> droppedFiles = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        if (!droppedFiles.isEmpty()) {
                            String path = droppedFiles.get(0).getAbsolutePath();
                            Main.openFile(path, null);
                        }
                    } catch (UnsupportedFlavorException | IOException ex) {
                    }
                }
            });
        } else {
            setDropTarget(null);
        }
    }

    public void updateClassesList() {
        List<TreeItem> nodes = getASTreeNodes(tagTree);
        boolean updateNeeded = false;
        for (TreeItem n : nodes) {
            if (n instanceof ClassesListTreeModel) {
                ((ClassesListTreeModel) n).update();
                updateNeeded = true;
            }
        }

        refreshTree();

        if (updateNeeded) {
            View.execInEventDispatch(new Runnable() {
                @Override
                public void run() {
                    tagTree.updateUI();
                }
            });
        }
    }

    public void doFilter() {
        List<TreeItem> nodes = getASTreeNodes(tagTree);
        boolean updateNeeded = false;
        for (TreeItem n : nodes) {
            if (n instanceof ClassesListTreeModel) {
                ((ClassesListTreeModel) n).setFilter(filterField.getText());
                updateNeeded = true;
            }
        }

        if (updateNeeded) {
            View.execInEventDispatch(new Runnable() {
                @Override
                public void run() {
                    tagTree.updateUI();
                }
            });
        }
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            if (abcPanel != null) {
                abcPanel.initSplits();
            }
            if (actionPanel != null) {
                actionPanel.initSplits();
            }

            View.execInEventDispatchLater(new Runnable() {
                @Override
                public void run() {
                    splitPane1.setDividerLocation(Configuration.guiSplitPane1DividerLocation.get(getWidth() / 3));
                    int confDivLoc = Configuration.guiSplitPane2DividerLocation.get(splitPane2.getHeight() * 3 / 5);
                    if (confDivLoc > splitPane2.getHeight() - 10) { //In older releases, divider location was saved when detailPanel was invisible too
                        confDivLoc = splitPane2.getHeight() * 3 / 5;
                    }
                    splitPane2.setDividerLocation(confDivLoc);
                    previewPanel.setDividerLocation(Configuration.guiPreviewSplitPaneDividerLocation.get(previewPanel.getWidth() / 2));

                    splitPos = splitPane2.getDividerLocation();
                    splitsInited = true;
                    previewPanel.setSplitsInited();
                }
            });

        }
    }

    public void renameIdentifier(SWF swf, String identifier) throws InterruptedException {
        String oldName = identifier;
        String newName = View.showInputDialog(translate("rename.enternew"), oldName);
        if (newName != null) {
            if (!oldName.equals(newName)) {
                swf.renameAS2Identifier(oldName, newName);
                View.showMessageDialog(null, translate("rename.finished.identifier"));
                updateClassesList();
                reload(true);
            }
        }
    }

    public void renameMultiname(List<ABCContainerTag> abcList, int multiNameIndex) {
        String oldName = "";
        if (getABCPanel().abc.constants.getMultiname(multiNameIndex).name_index > 0) {
            oldName = getABCPanel().abc.constants.getString(getABCPanel().abc.constants.getMultiname(multiNameIndex).name_index);
        }
        String newName = View.showInputDialog(translate("rename.enternew"), oldName);
        if (newName != null) {
            if (!oldName.equals(newName)) {
                int mulCount = 0;
                for (ABCContainerTag cnt : abcList) {
                    ABC abc = cnt.getABC();
                    for (int m = 1; m < abc.constants.getMultinameCount(); m++) {
                        int ni = abc.constants.getMultiname(m).name_index;
                        String n = "";
                        if (ni > 0) {
                            n = abc.constants.getString(ni);
                        }
                        if (n.equals(oldName)) {
                            abc.renameMultiname(m, newName);
                            mulCount++;
                        }
                    }
                }
                View.showMessageDialog(null, translate("rename.finished.multiname").replace("%count%", Integer.toString(mulCount)));
                if (abcPanel != null) {
                    abcPanel.reload();
                }
                updateClassesList();
                reload(true);
                getABCPanel().hilightScript(getABCPanel().getSwf(), getABCPanel().decompiledTextArea.getScriptLeaf().getClassPath().toString());
            }
        }
    }

    public List<TreeItem> getASTreeNodes(TagTree tree) {
        List<TreeItem> result = new ArrayList<>();
        TagTreeModel tm = (TagTreeModel) tree.getModel();
        if (tm == null) {
            return result;
        }
        TreeItem root = tm.getRoot();
        for (int i = 0; i < tm.getChildCount(root); i++) {
            // first level node can be SWF and SWFBundle
            TreeItem node = tm.getChild(root, i);
            if (node instanceof SWFBundle) {
                for (int j = 0; j < tm.getChildCount(node); j++) {
                    // child of SWFBundle should be SWF
                    SWF swfNode = (SWF) tm.getChild(node, j);
                    result.add(tm.getScriptsNode(swfNode));
                }
            } else if (node instanceof SWF) {
                SWF swfNode = (SWF) tm.getChild(root, i);
                result.add(tm.getScriptsNode(swfNode));
            }
        }
        return result;
    }

    public boolean confirmExperimental() {
        return View.showConfirmDialog(null, translate("message.confirm.experimental"), translate("message.warning"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION;
    }

    public List<File> exportSelection(AbortRetryIgnoreHandler handler, String selFile, ExportDialog export) throws IOException {

        List<File> ret = new ArrayList<>();
        List<TreeItem> sel = folderPreviewPanel.selectedItems.isEmpty() ? tagTree.getAllSelected(tagTree) : new ArrayList<>(folderPreviewPanel.selectedItems.values());

        List<SWF> allSwfs = new ArrayList<>();
        for (SWFList swfList : swfs) {
            for (SWF swf : swfList) {
                allSwfs.add(swf);
            }
        }

        for (int j = 0; j < allSwfs.size(); j++) {
            List<ScriptPack> as3scripts = new ArrayList<>();
            List<Tag> images = new ArrayList<>();
            List<Tag> shapes = new ArrayList<>();
            List<Tag> morphshapes = new ArrayList<>();
            List<Tag> movies = new ArrayList<>();
            List<Tag> sounds = new ArrayList<>();
            List<Tag> texts = new ArrayList<>();
            List<TreeItem> as12scripts = new ArrayList<>();
            List<Tag> binaryData = new ArrayList<>();
            Map<Integer, List<Integer>> frames = new HashMap<>();
            List<Tag> fonts = new ArrayList<>();

            SWF swf = allSwfs.get(j);
            for (TreeItem d : sel) {
                SWF selectedNodeSwf = d.getSwf();
                if (!allSwfs.contains(selectedNodeSwf)) {
                    allSwfs.add(selectedNodeSwf);
                }

                if (selectedNodeSwf != swf) {
                    continue;
                }

                if (d instanceof TagScript) {
                    Tag tag = ((TagScript) d).getTag();
                    if (tag instanceof DoActionTag || tag instanceof DoInitActionTag) {
                        as12scripts.add(d);
                    }
                }

                if (d instanceof Tag || d instanceof ASMSource) {
                    TreeNodeType nodeType = TagTree.getTreeNodeType(d);
                    if (nodeType == TreeNodeType.IMAGE) {
                        images.add((Tag) d);
                    }
                    if (nodeType == TreeNodeType.SHAPE) {
                        shapes.add((Tag) d);
                    }
                    if (nodeType == TreeNodeType.MORPH_SHAPE) {
                        morphshapes.add((Tag) d);
                    }
                    if (nodeType == TreeNodeType.AS) {
                        as12scripts.add(d);
                    }
                    if (nodeType == TreeNodeType.MOVIE) {
                        movies.add((Tag) d);
                    }
                    if (nodeType == TreeNodeType.SOUND) {
                        sounds.add((Tag) d);
                    }
                    if (nodeType == TreeNodeType.BINARY_DATA) {
                        binaryData.add((Tag) d);
                    }
                    if (nodeType == TreeNodeType.TEXT) {
                        texts.add((Tag) d);
                    }
                    if (nodeType == TreeNodeType.FONT) {
                        fonts.add((Tag) d);
                    }
                }

                if (d instanceof Frame) {
                    Frame fn = (Frame) d;
                    Timelined parent = fn.timeline.timelined;
                    int frame = fn.frame;
                    int parentId = 0;
                    if (parent instanceof CharacterTag) {
                        parentId = ((CharacterTag) parent).getCharacterId();
                    }
                    if (!frames.containsKey(parentId)) {
                        frames.put(parentId, new ArrayList<Integer>());
                    }
                    frames.get(parentId).add(frame);
                }
                if (d instanceof ScriptPack) {
                    as3scripts.add((ScriptPack) d);
                }
            }

            if (selFile == null) {
                selFile = selectExportDir();
                if (selFile == null) {
                    return new ArrayList<>();
                }
            }

            EventListener evl = swf.getExportEventListener();

            final ScriptExportMode scriptMode = export.getValue(ScriptExportMode.class);
            ret.addAll(new ImageExporter().exportImages(handler, selFile + File.separator + "images", images,
                    new ImageExportSettings(export.getValue(ImageExportMode.class)), evl));
            ret.addAll(new ShapeExporter().exportShapes(handler, selFile + File.separator + "shapes", shapes,
                    new ShapeExportSettings(export.getValue(ShapeExportMode.class), export.getZoom()), evl));
            ret.addAll(new MorphShapeExporter().exportMorphShapes(handler, selFile + File.separator + "morphshapes", morphshapes,
                    new MorphShapeExportSettings(export.getValue(MorphShapeExportMode.class), export.getZoom()), evl));
            ret.addAll(new TextExporter().exportTexts(handler, selFile + File.separator + "texts", texts,
                    new TextExportSettings(export.getValue(TextExportMode.class), Configuration.textExportSingleFile.get(), export.getZoom()), evl));
            ret.addAll(new MovieExporter().exportMovies(handler, selFile + File.separator + "movies", movies,
                    new MovieExportSettings(export.getValue(MovieExportMode.class)), evl));
            ret.addAll(new SoundExporter().exportSounds(handler, selFile + File.separator + "sounds", sounds,
                    new SoundExportSettings(export.getValue(SoundExportMode.class)), evl));
            ret.addAll(new BinaryDataExporter().exportBinaryData(handler, selFile + File.separator + "binaryData", binaryData,
                    new BinaryDataExportSettings(export.getValue(BinaryDataExportMode.class)), evl));
            ret.addAll(new FontExporter().exportFonts(handler, selFile + File.separator + "fonts", fonts,
                    new FontExportSettings(export.getValue(FontExportMode.class)), evl));

            FrameExporter frameExporter = new FrameExporter();
            FramesExportSettings fes = new FramesExportSettings(export.getValue(FramesExportMode.class), export.getZoom());
            for (Entry<Integer, List<Integer>> entry : frames.entrySet()) {
                ret.addAll(frameExporter.exportFrames(handler, selFile + File.separator + "frames", swf, entry.getKey(), entry.getValue(), fes, evl));
            }

            if (swf.isAS3()) {
                for (int i = 0; i < as3scripts.size(); i++) {
                    ScriptPack tls = as3scripts.get(i);
                    Main.startWork(translate("work.exporting") + " " + (i + 1) + "/" + as3scripts.size() + " " + tls.getPath() + " ...");
                    ret.add(tls.export(selFile, scriptMode, Configuration.parallelSpeedUp.get()));
                }
            } else {
                Map<String, ASMSource> asmsToExport = swf.getASMs(true, as12scripts, false);
                ret.addAll(new AS2ScriptExporter().exportAS2ScriptsTimeout(handler, selFile + File.separator + "scripts", asmsToExport, scriptMode, evl));
            }
        }
        return ret;
    }

    public void exportAll(SWF swf, AbortRetryIgnoreHandler handler, String selFile, ExportDialog export) throws IOException {
        EventListener evl = swf.getExportEventListener();
        new ImageExporter().exportImages(handler, selFile + File.separator + "images", swf.tags,
                new ImageExportSettings(export.getValue(ImageExportMode.class)), evl);
        new ShapeExporter().exportShapes(handler, selFile + File.separator + "shapes", swf.tags,
                new ShapeExportSettings(export.getValue(ShapeExportMode.class), export.getZoom()), evl);
        new MorphShapeExporter().exportMorphShapes(handler, selFile + File.separator + "morphshapes", swf.tags,
                new MorphShapeExportSettings(export.getValue(MorphShapeExportMode.class), export.getZoom()), evl);
        new TextExporter().exportTexts(handler, selFile + File.separator + "texts", swf.tags,
                new TextExportSettings(export.getValue(TextExportMode.class), Configuration.textExportSingleFile.get(), export.getZoom()), evl);
        new MovieExporter().exportMovies(handler, selFile + File.separator + "movies", swf.tags,
                new MovieExportSettings(export.getValue(MovieExportMode.class)), evl);
        new SoundExporter().exportSounds(handler, selFile + File.separator + "sounds", swf.tags,
                new SoundExportSettings(export.getValue(SoundExportMode.class)), evl);
        new BinaryDataExporter().exportBinaryData(handler, selFile + File.separator + "binaryData", swf.tags,
                new BinaryDataExportSettings(export.getValue(BinaryDataExportMode.class)), evl);
        new FontExporter().exportFonts(handler, selFile + File.separator + "fonts", swf.tags,
                new FontExportSettings(export.getValue(FontExportMode.class)), evl);

        FrameExporter frameExporter = new FrameExporter();
        FramesExportSettings fes = new FramesExportSettings(export.getValue(FramesExportMode.class), export.getZoom());
        frameExporter.exportFrames(handler, selFile + File.separator + "frames", swf, 0, null, fes, evl);
        for (CharacterTag c : swf.getCharacters().values()) {
            if (c instanceof DefineSpriteTag) {
                frameExporter.exportFrames(handler, selFile + File.separator + "frames", swf, c.getCharacterId(), null, fes, evl);
            }
        }

        ScriptExportMode exportMode = export.getValue(ScriptExportMode.class);
        swf.exportActionScript(handler, selFile, exportMode, Configuration.parallelSpeedUp.get(), evl);
    }

    public List<SWFList> getSwfs() {
        return swfs;
    }

    public SWFList getCurrentSwfList() {
        SWF swf = getCurrentSwf();
        if (swf == null) {
            return null;
        }

        return swf.swfList;
    }

    public SWF getCurrentSwf() {
        if (swfs == null || swfs.isEmpty()) {
            return null;
        }

        if (treePanelMode == TreePanelMode.TAG_TREE) {
            TreeItem treeNode = (TreeItem) tagTree.getLastSelectedPathComponent();
            if (treeNode == null) {
                return swfs.get(0).get(0);
            }

            if (treeNode instanceof SWFList) {
                return null;
            }

            return treeNode.getSwf();
        } else if (treePanelMode == TreePanelMode.DUMP_TREE) {
            DumpInfo dumpInfo = (DumpInfo) dumpTree.getLastSelectedPathComponent();

            if (dumpInfo == null) {
                return null;
            }

            return DumpInfoSwfNode.getSwfNode(dumpInfo).getSwf();
        }

        return null;
    }

    public void gotoFrame(int frame) {
        TreeItem treeItem = (TreeItem) tagTree.getLastSelectedPathComponent();
        if (treeItem == null) {
            return;
        }
        if (treeItem instanceof Timelined) {
            Timelined t = (Timelined) treeItem;
            Timeline tim = t.getTimeline();
            Frame f = ((TagTreeModel) tagTree.getModel()).getFrame(treeItem.getSwf(), t, frame);
            if (f != null) {
                setTagTreeSelectedNode(f);
            }
        }
    }

    public void gotoDocumentClass(SWF swf) {
        if (swf == null) {
            return;
        }
        String documentClass = swf.getDocumentClass();
        if (documentClass != null && !Configuration.dumpView.get()) {
            List<ABCContainerTag> abcList = swf.getAbcList();
            if (!abcList.isEmpty()) {
                showDetail(DETAILCARDAS3NAVIGATOR);
                showCard(CARDACTIONSCRIPT3PANEL);
                getABCPanel().setAbc(abcList.get(0).getABC());
                getABCPanel().hilightScript(swf, documentClass);
            }
        }
    }

    public void disableDecompilationChanged() {
        clearAllScriptCache();

        if (abcPanel != null) {
            abcPanel.reload();
        }

        updateClassesList();
    }

    private void clearAllScriptCache() {
        for (SWFList swfList : swfs) {
            for (SWF swf : swfList) {
                swf.clearScriptCache();
            }
        }
    }

    public void searchInActionScriptOrText(Boolean searchInText) {
        if (searchDialog == null) {
            searchDialog = new SearchDialog(getMainFrame().getWindow(), false);
        }

        if (searchInText != null) {
            if (searchInText) {
                searchDialog.searchInTextsRadioButton.setSelected(true);
            } else {
                searchDialog.searchInASRadioButton.setSelected(true);
            }
        }

        searchDialog.setVisible(true);
        if (searchDialog.result) {
            final String txt = searchDialog.searchField.getText();
            if (!txt.isEmpty()) {
                final SWF swf = getCurrentSwf();

                new CancellableWorker() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        boolean found = false;
                        if (searchDialog.searchInASRadioButton.isSelected()) {
                            if (swf.isAS3()) {
                                if (abcPanel != null && abcPanel.search(txt, searchDialog.ignoreCaseCheckBox.isSelected(), searchDialog.regexpCheckBox.isSelected())) {
                                    found = true;
                                    View.execInEventDispatch(new Runnable() {
                                        @Override
                                        public void run() {
                                            showDetail(DETAILCARDAS3NAVIGATOR);
                                            showCard(CARDACTIONSCRIPT3PANEL);
                                        }
                                    });
                                }
                            } else {
                                if (getActionPanel().search(txt, searchDialog.ignoreCaseCheckBox.isSelected(), searchDialog.regexpCheckBox.isSelected())) {
                                    found = true;
                                    View.execInEventDispatch(new Runnable() {
                                        @Override
                                        public void run() {
                                            showCard(CARDACTIONSCRIPTPANEL);
                                        }
                                    });
                                }
                            }
                        } else if (searchDialog.searchInTextsRadioButton.isSelected()) {
                            if (searchText(txt, searchDialog.ignoreCaseCheckBox.isSelected(), searchDialog.regexpCheckBox.isSelected(), swf)) {
                                found = true;
                            }
                        }

                        if (!found) {
                            View.showMessageDialog(null, translate("message.search.notfound").replace("%searchtext%", txt), translate("message.search.notfound.title"), JOptionPane.INFORMATION_MESSAGE);
                        }

                        return null;
                    }
                }.execute();
            }
        }
    }

    public void replaceText() {
        if (replaceDialog == null) {
            replaceDialog = new SearchDialog(getMainFrame().getWindow(), true);
        }

        replaceDialog.setVisible(true);
        if (replaceDialog.result) {
            final String txt = replaceDialog.searchField.getText();
            if (!txt.isEmpty()) {
                final SWF swf = getCurrentSwf();

                new CancellableWorker() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        int findCount = 0;
                        boolean ignoreCase = replaceDialog.ignoreCaseCheckBox.isSelected();
                        boolean regexp = replaceDialog.regexpCheckBox.isSelected();
                        String replacement = replaceDialog.replaceField.getText();
                        if (!regexp) {
                            replacement = Matcher.quoteReplacement(replacement);
                        }
                        Pattern pat;
                        if (regexp) {
                            pat = Pattern.compile(txt, ignoreCase ? Pattern.CASE_INSENSITIVE : 0);
                        } else {
                            pat = Pattern.compile(Pattern.quote(txt), ignoreCase ? Pattern.CASE_INSENSITIVE : 0);
                        }
                        List<TextTag> textTags = new ArrayList<>();
                        for (Tag tag : swf.tags) {
                            if (tag instanceof TextTag) {
                                textTags.add((TextTag) tag);
                            }
                        }
                        for (TextTag textTag : textTags) {
                            if (!replaceDialog.replaceInParametersCheckBox.isSelected()) {
                                List<String> texts = textTag.getTexts();
                                boolean found = false;
                                for (int i = 0; i < texts.size(); i++) {
                                    String text = texts.get(i);
                                    if (pat.matcher(text).find()) {
                                        texts.set(i, text.replaceAll(txt, replacement));
                                        found = true;
                                        findCount++;
                                    }
                                }
                                if (found) {
                                    String[] textArray = texts.toArray(new String[texts.size()]);
                                    textTag.setFormattedText(getMissingCharacterHandler(), textTag.getFormattedText(), textArray);
                                }
                            } else {
                                String text = textTag.getFormattedText();
                                if (pat.matcher(text).find()) {
                                    textTag.setFormattedText(getMissingCharacterHandler(), text.replaceAll(txt, replacement), null);
                                    findCount++;
                                }
                            }
                        }

                        if (findCount > 0) {
                            swf.clearImageCache();
                            refreshTree();
                        }
                        return null;
                    }
                }.execute();
            }
        }
    }

    private boolean searchText(String txt, boolean ignoreCase, boolean regexp, SWF swf) {
        if (txt != null && !txt.isEmpty()) {
            SearchPanel<TextTag> textSearchPanel = previewPanel.getTextPanel().getSearchPanel();
            textSearchPanel.setOptions(ignoreCase, regexp);
            List<TextTag> found = new ArrayList<>();
            Pattern pat;
            if (regexp) {
                pat = Pattern.compile(txt, ignoreCase ? (Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE) : 0);
            } else {
                pat = Pattern.compile(Pattern.quote(txt), ignoreCase ? (Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE) : 0);
            }
            for (Tag tag : swf.tags) {
                if (tag instanceof TextTag) {
                    TextTag textTag = (TextTag) tag;
                    if (pat.matcher(textTag.getFormattedText()).find()) {
                        found.add(textTag);
                    }
                }
            }
            textSearchPanel.setSearchText(txt);
            return textSearchPanel.setResults(found);
        }
        return false;
    }

    @Override
    public void updateSearchPos(TextTag item) {
        setTagTreeSelectedNode(item);
        previewPanel.getTextPanel().updateSearchPos();
    }

    private void setDumpTreeSelectedNode(DumpInfo dumpInfo) {
        DumpTreeModel dtm = (DumpTreeModel) dumpTree.getModel();
        TreePath tp = dtm.getDumpInfoPath(dumpInfo);
        if (tp != null) {
            dumpTree.setSelectionPath(tp);
            dumpTree.scrollPathToVisible(tp);
        } else {
            showCard(CARDEMPTYPANEL);
        }
    }

    public void setTagTreeSelectedNode(TreeItem treeItem) {
        TagTreeModel ttm = (TagTreeModel) tagTree.getModel();
        TreePath tp = ttm.getTagPath(treeItem);
        if (tp != null) {
            tagTree.setSelectionPath(tp);
            tagTree.scrollPathToVisible(tp);
        } else {
            showCard(CARDEMPTYPANEL);
        }
    }

    public void autoDeobfuscateChanged() {
        Helper.decompilationErrorAdd = AppStrings.translate(Configuration.autoDeobfuscate.get() ? "deobfuscation.comment.failed" : "deobfuscation.comment.tryenable");
        clearAllScriptCache();
        if (abcPanel != null) {
            abcPanel.reload();
        }
        reload(true);
        updateClassesList();
    }

    public void renameOneIdentifier(final SWF swf) {
        if (swf == null) {
            return;
        }

        FileAttributesTag fileAttributes = swf.getFileAttributes();
        if (fileAttributes != null && fileAttributes.actionScript3) {
            final int multiName = getABCPanel().decompiledTextArea.getMultinameUnderCaret();
            final List<ABCContainerTag> abcList = swf.getAbcList();
            if (multiName > 0) {
                new CancellableWorker() {
                    @Override
                    public Void doInBackground() throws Exception {
                        Main.startWork(translate("work.renaming") + "...");
                        renameMultiname(abcList, multiName);
                        return null;
                    }

                    @Override
                    protected void done() {
                        Main.stopWork();
                    }
                }.execute();

            } else {
                View.showMessageDialog(null, translate("message.rename.notfound.multiname"), translate("message.rename.notfound.title"), JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            final String identifier = getActionPanel().getStringUnderCursor();
            if (identifier != null) {
                new CancellableWorker() {
                    @Override
                    public Void doInBackground() throws Exception {
                        Main.startWork(translate("work.renaming") + "...");
                        try {
                            renameIdentifier(swf, identifier);
                        } catch (InterruptedException ex) {
                            logger.log(Level.SEVERE, null, ex);
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        Main.stopWork();
                    }
                }.execute();
            } else {
                View.showMessageDialog(null, translate("message.rename.notfound.identifier"), translate("message.rename.notfound.title"), JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void exportFla(final SWF swf) {
        if (swf == null) {
            return;
        }
        JFileChooser fc = new JFileChooser();
        String selDir = Configuration.lastOpenDir.get();
        fc.setCurrentDirectory(new File(selDir));
        if (!selDir.endsWith(File.separator)) {
            selDir += File.separator;
        }
        String fileName = new File(swf.getFile()).getName();
        fileName = fileName.substring(0, fileName.length() - 4) + ".fla";
        fc.setSelectedFile(new File(selDir + fileName));
        List<FileFilter> flaFilters = new ArrayList<>();
        List<FileFilter> xflFilters = new ArrayList<>();
        List<FLAVersion> versions = new ArrayList<>();
        boolean isAS3 = swf.isAS3();
        for (int i = FLAVersion.values().length - 1; i >= 0; i--) {
            final FLAVersion v = FLAVersion.values()[i];
            if (!isAS3 && v.minASVersion() > 2) {
                // This version does not support AS1/2
            } else {
                versions.add(v);
                FileFilter f = new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || (f.getName().toLowerCase().endsWith(".fla"));
                    }

                    @Override
                    public String getDescription() {
                        return translate("filter.fla").replace("%version%", v.applicationName());
                    }
                };
                if (v == FLAVersion.CS6) {
                    fc.setFileFilter(f);
                } else {
                    fc.addChoosableFileFilter(f);
                }
                flaFilters.add(f);
                f = new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || (f.getName().toLowerCase().endsWith(".xfl"));
                    }

                    @Override
                    public String getDescription() {
                        return translate("filter.xfl").replace("%version%", v.applicationName());
                    }
                };
                fc.addChoosableFileFilter(f);
                xflFilters.add(f);
            }
        }

        fc.setAcceptAllFileFilterUsed(false);
        JFrame f = new JFrame();
        View.setWindowIcon(f);
        if (fc.showSaveDialog(f) == JFileChooser.APPROVE_OPTION) {
            Configuration.lastOpenDir.set(Helper.fixDialogFile(fc.getSelectedFile()).getParentFile().getAbsolutePath());
            File sf = Helper.fixDialogFile(fc.getSelectedFile());

            Main.startWork(translate("work.exporting.fla") + "...");
            final boolean compressed = flaFilters.contains(fc.getFileFilter());
            if (!compressed) {
                if (sf.getName().endsWith(".fla")) {
                    sf = new File(sf.getAbsolutePath().substring(0, sf.getAbsolutePath().length() - 4) + ".xfl");
                }
            }
            final FLAVersion selectedVersion = versions.get(compressed ? flaFilters.indexOf(fc.getFileFilter()) : xflFilters.indexOf(fc.getFileFilter()));
            final File selfile = sf;
            new CancellableWorker() {
                @Override
                protected Void doInBackground() throws Exception {
                    Helper.freeMem();
                    try {
                        if (compressed) {
                            swf.exportFla(errorHandler, selfile.getAbsolutePath(), new File(swf.getFile()).getName(), ApplicationInfo.APPLICATION_NAME, ApplicationInfo.applicationVerName, ApplicationInfo.version, Configuration.parallelSpeedUp.get(), selectedVersion);
                        } else {
                            swf.exportXfl(errorHandler, selfile.getAbsolutePath(), new File(swf.getFile()).getName(), ApplicationInfo.APPLICATION_NAME, ApplicationInfo.applicationVerName, ApplicationInfo.version, Configuration.parallelSpeedUp.get(), selectedVersion);
                        }
                    } catch (IOException ex) {
                        View.showMessageDialog(null, translate("error.export") + ": " + ex.getClass().getName() + " " + ex.getLocalizedMessage(), translate("error"), JOptionPane.ERROR_MESSAGE);
                    }
                    Helper.freeMem();
                    return null;
                }

                @Override
                protected void done() {
                    Main.stopWork();
                    if (Configuration.openFolderAfterFlaExport.get()) {
                        try {
                            Desktop.getDesktop().open(selfile.getAbsoluteFile().getParentFile());
                        } catch (IOException ex) {
                            logger.log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }.execute();
        }
    }

    public void importText(final SWF swf) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(Configuration.lastExportDir.get()));
        chooser.setDialogTitle(translate("import.select.directory"));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String selFile = Helper.fixDialogFile(chooser.getSelectedFile()).getAbsolutePath();
            File textsFile = new File(Path.combine(selFile, TextExporter.TEXT_EXPORT_FOLDER, TextExporter.TEXT_EXPORT_FILENAME_FORMATTED));
            TextImporter textImporter = new TextImporter(getMissingCharacterHandler(), new TextImportErrorHandler() {

                // "configuration items" for the current replace only
                private final ConfigurationItem<Boolean> showAgainImportError = new ConfigurationItem<>("showAgainImportError", true, true);

                private final ConfigurationItem<Boolean> showAgainInvalidText = new ConfigurationItem<>("showAgainInvalidText", true, true);

                private String getTextTagInfo(TextTag textTag) {
                    String ret = "";
                    if (textTag != null) {
                        ret += " TextId: " + textTag.getCharacterId() + " (" + String.join(", ", textTag.getTexts()) + ")";
                    }

                    return ret;
                }

                @Override
                public boolean handle(TextTag textTag) {
                    String msg = translate("error.text.import");
                    logger.log(Level.SEVERE, msg + getTextTagInfo(textTag));
                    return View.showConfirmDialog(MainPanel.this, msg, translate("error"), JOptionPane.OK_CANCEL_OPTION, showAgainImportError, JOptionPane.OK_OPTION) != JOptionPane.OK_OPTION;
                }

                @Override
                public boolean handle(TextTag textTag, String message, long line) {
                    String msg = translate("error.text.invalid.continue").replace("%text%", message).replace("%line%", Long.toString(line));
                    logger.log(Level.SEVERE, msg + getTextTagInfo(textTag));
                    return View.showConfirmDialog(MainPanel.this, msg, translate("error"), JOptionPane.OK_CANCEL_OPTION, showAgainInvalidText, JOptionPane.OK_OPTION) != JOptionPane.OK_OPTION;
                }
            });

            // try to import formatted texts
            if (textsFile.exists()) {
                textImporter.importTextsSingleFileFormatted(textsFile, swf);
            } else {
                textsFile = new File(Path.combine(selFile, TextExporter.TEXT_EXPORT_FOLDER, TextExporter.TEXT_EXPORT_FILENAME_PLAIN));
                // try to import plain texts
                if (textsFile.exists()) {
                    textImporter.importTextsSingleFile(textsFile, swf);
                } else {
                    textImporter.importTextsMultipleFiles(selFile, swf);
                }
            }

            swf.clearImageCache();
            reload(true);
        }
    }

    private String selectExportDir() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(Configuration.lastExportDir.get()));
        chooser.setDialogTitle(translate("export.select.directory"));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Main.startWork(translate("work.exporting") + "...");
            final String selFile = Helper.fixDialogFile(chooser.getSelectedFile()).getAbsolutePath();
            Configuration.lastExportDir.set(Helper.fixDialogFile(chooser.getSelectedFile()).getAbsolutePath());
            return selFile;
        }
        return null;
    }

    public void export(final boolean onlySel) {

        final SWF swf = getCurrentSwf();
        List<TreeItem> sel = tagTree.getSelection(swf);
        if (!onlySel) {
            sel = null;
        } else {
            if (sel.isEmpty()) {
                return;
            }
        }
        final ExportDialog export = new ExportDialog(sel);
        export.setVisible(true);
        if (!export.cancelled) {
            final String selFile = selectExportDir();
            if (selFile != null) {
                final long timeBefore = System.currentTimeMillis();
                Main.startWork(translate("work.exporting") + "...");

                new CancellableWorker() {
                    @Override
                    public Void doInBackground() throws Exception {
                        try {
                            if (onlySel) {
                                exportSelection(errorHandler, selFile, export);
                            } else {
                                exportAll(swf, errorHandler, selFile, export);
                            }
                        } catch (Exception ex) {
                            logger.log(Level.SEVERE, "Error during export", ex);
                            View.showMessageDialog(null, translate("error.export") + ": " + ex.getClass().getName() + " " + ex.getLocalizedMessage());
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        Main.stopWork();
                        long timeAfter = System.currentTimeMillis();
                        final long timeMs = timeAfter - timeBefore;

                        View.execInEventDispatchLater(new Runnable() {

                            @Override
                            public void run() {
                                setStatus(translate("export.finishedin").replace("%time%", Helper.formatTimeSec(timeMs)));
                            }
                        });
                    }
                }.execute();

            }
        }
    }

    public void exportJavaSource() {
        List<TreeItem> sel = tagTree.getSelected(tagTree);
        for (TreeItem item : sel) {
            if (item instanceof SWF) {
                SWF swf = (SWF) item;
                final String selFile = selectExportDir();
                if (selFile != null) {
                    try {
                        new SwfJavaExporter().exportJavaCode(swf, selFile);
                        Main.stopWork();
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public void exportSwfXml() {
        List<TreeItem> sel = tagTree.getSelected(tagTree);
        Set<SWF> swfs = new HashSet<>();

        for (TreeItem item : sel) {
            swfs.add(item.getSwf());
        }

        for (SWF swf : swfs) {
            final String selFile = selectExportDir();
            if (selFile != null) {
                try {
                    File outFile = new File(selFile + File.separator + Helper.makeFileName("swf.xml"));
                    new SwfXmlExporter().exportXml(swf, outFile);
                    Main.stopWork();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void importSwfXml() {
        List<TreeItem> sel = tagTree.getSelected(tagTree);
        Set<SWF> swfs = new HashSet<>();
        for (TreeItem item : sel) {
            swfs.add(item.getSwf());
        }
        if (swfs.size() > 1) {
            return;
        }

        for (SWF swf : swfs) {
            File selectedFile = showImportFileChooser("filter.xml|*.xml");
            if (selectedFile != null) {
                File selfile = Helper.fixDialogFile(selectedFile);
                String xml = Helper.readTextFile(selfile.getPath());
                try {
                    new SwfXmlImporter().importSwf(swf, xml);
                    swf.clearAllCache();
                    swf.assignExportNamesToSymbols();
                    swf.assignClassesToSymbols();
                    refreshTree();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void restoreControlFlow(final boolean all) {
        Main.startWork(translate("work.restoringControlFlow"));
        if ((!all) || confirmExperimental()) {
            new CancellableWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    int cnt = 0;
                    if (all) {
                        for (ABCContainerTag tag : getABCPanel().getAbcList()) {
                            tag.getABC().restoreControlFlow();
                        }
                    } else {
                        int bi = getABCPanel().detailPanel.methodTraitPanel.methodCodePanel.getBodyIndex();
                        if (bi != -1) {
                            getABCPanel().abc.bodies.get(bi).restoreControlFlow(getABCPanel().abc.constants, getABCPanel().decompiledTextArea.getCurrentTrait(), getABCPanel().abc.method_info.get(getABCPanel().abc.bodies.get(bi).method_info));
                        }
                        getABCPanel().detailPanel.methodTraitPanel.methodCodePanel.setBodyIndex(bi, getABCPanel().abc, getABCPanel().decompiledTextArea.getCurrentTrait());
                    }
                    return true;
                }

                @Override
                protected void done() {
                    Main.stopWork();
                    View.showMessageDialog(null, translate("work.restoringControlFlow.complete"));

                    View.execInEventDispatch(new Runnable() {

                        @Override
                        public void run() {
                            getABCPanel().reload();
                            updateClassesList();
                        }
                    });
                }
            }.execute();
        }
    }

    public void renameIdentifiers(final SWF swf) {
        if (swf == null) {
            return;
        }
        if (confirmExperimental()) {
            final RenameType renameType = new RenameDialog().display();
            if (renameType != null) {
                Main.startWork(translate("work.renaming.identifiers") + "...");
                new CancellableWorker<Integer>() {
                    @Override
                    protected Integer doInBackground() throws Exception {
                        int cnt = swf.deobfuscateIdentifiers(renameType);
                        return cnt;
                    }

                    @Override
                    protected void done() {
                        View.execInEventDispatch(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    int cnt = get();
                                    Main.stopWork();
                                    View.showMessageDialog(null, translate("message.rename.renamed").replace("%count%", Integer.toString(cnt)));
                                    swf.assignClassesToSymbols();
                                    swf.clearScriptCache();
                                    if (abcPanel != null) {
                                        abcPanel.reload();
                                    }
                                    updateClassesList();
                                    reload(true);
                                } catch (Exception ex) {
                                    logger.log(Level.SEVERE, "Error during renaming identifiers", ex);
                                    Main.stopWork();
                                    View.showMessageDialog(null, translate("error.occured").replace("%error%", ex.getClass().getSimpleName()));
                                }
                            }
                        });
                    }
                }.execute();
            }
        }
    }

    public void deobfuscate() {
        if (deobfuscationDialog == null) {
            deobfuscationDialog = new DeobfuscationDialog();
        }
        deobfuscationDialog.setVisible(true);
        if (deobfuscationDialog.ok) {
            Main.startWork(translate("work.deobfuscating") + "...");
            new CancellableWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        if (deobfuscationDialog.processAllCheckbox.isSelected()) {
                            for (ABCContainerTag tag : getABCPanel().getAbcList()) {
                                if (deobfuscationDialog.codeProcessingLevel.getValue() == DeobfuscationDialog.LEVEL_REMOVE_DEAD_CODE) {
                                    tag.getABC().removeDeadCode();
                                } else if (deobfuscationDialog.codeProcessingLevel.getValue() == DeobfuscationDialog.LEVEL_REMOVE_TRAPS) {
                                    tag.getABC().removeTraps();
                                } else if (deobfuscationDialog.codeProcessingLevel.getValue() == DeobfuscationDialog.LEVEL_RESTORE_CONTROL_FLOW) {
                                    tag.getABC().removeTraps();
                                    tag.getABC().restoreControlFlow();
                                }
                            }
                        } else {
                            int bi = getABCPanel().detailPanel.methodTraitPanel.methodCodePanel.getBodyIndex();
                            Trait t = getABCPanel().decompiledTextArea.getCurrentTrait();
                            if (bi != -1) {
                                if (deobfuscationDialog.codeProcessingLevel.getValue() == DeobfuscationDialog.LEVEL_REMOVE_DEAD_CODE) {
                                    getABCPanel().abc.bodies.get(bi).removeDeadCode(getABCPanel().abc.constants, t, getABCPanel().abc.method_info.get(getABCPanel().abc.bodies.get(bi).method_info));
                                } else if (deobfuscationDialog.codeProcessingLevel.getValue() == DeobfuscationDialog.LEVEL_REMOVE_TRAPS) {
                                    getABCPanel().abc.bodies.get(bi).removeTraps(getABCPanel().abc.constants, getABCPanel().abc, t, getABCPanel().decompiledTextArea.getScriptLeaf().scriptIndex, getABCPanel().decompiledTextArea.getClassIndex(), getABCPanel().decompiledTextArea.getIsStatic(), ""/*FIXME*/);
                                } else if (deobfuscationDialog.codeProcessingLevel.getValue() == DeobfuscationDialog.LEVEL_RESTORE_CONTROL_FLOW) {
                                    getABCPanel().abc.bodies.get(bi).removeTraps(getABCPanel().abc.constants, getABCPanel().abc, t, getABCPanel().decompiledTextArea.getScriptLeaf().scriptIndex, getABCPanel().decompiledTextArea.getClassIndex(), getABCPanel().decompiledTextArea.getIsStatic(), ""/*FIXME*/);
                                    getABCPanel().abc.bodies.get(bi).restoreControlFlow(getABCPanel().abc.constants, t, getABCPanel().abc.method_info.get(getABCPanel().abc.bodies.get(bi).method_info));
                                }
                            }
                            getABCPanel().detailPanel.methodTraitPanel.methodCodePanel.setBodyIndex(bi, getABCPanel().abc, t);
                        }
                    } catch (Exception ex) {
                        logger.log(Level.SEVERE, "Deobfuscation error", ex);
                    }
                    return true;
                }

                @Override
                protected void done() {
                    Main.stopWork();
                    View.showMessageDialog(null, translate("work.deobfuscating.complete"));

                    View.execInEventDispatch(new Runnable() {

                        @Override
                        public void run() {
                            clearAllScriptCache();
                            getABCPanel().reload();
                            updateClassesList();
                        }
                    });
                }
            }.execute();
        }
    }

    public void removeNonScripts(SWF swf) {
        if (swf == null) {
            return;
        }
        List<Tag> tags = new ArrayList<>(swf.tags);
        for (Tag tag : tags) {
            System.out.println(tag.getClass());
            if (!(tag instanceof ABCContainerTag || tag instanceof ASMSource)) {
                swf.removeTag(tag, true);
            }
        }
        refreshTree();
    }

    private void clear() {
        dumpViewPanel.clear();
        previewPanel.clear();
        if (abcPanel != null) {
            abcPanel.clearSwf();
        }
        if (actionPanel != null) {
            actionPanel.clearSource();
        }
    }

    public void refreshTree() {
        clear();
        showCard(CARDEMPTYPANEL);
        TreeItem treeItem = tagTree.getCurrentTreeItem();
        DumpInfo dumpInfo = (DumpInfo) dumpTree.getLastSelectedPathComponent();

        if (tagTree.getModel() != null) {
            View.refreshTree(tagTree, new TagTreeModel(swfs, Configuration.tagTreeShowEmptyFolders.get()));
        }

        if (dumpTree.getModel() != null) {
            View.refreshTree(dumpTree, new DumpTreeModel(swfs));
        }

        if (treeItem != null) {
            setTagTreeSelectedNode(treeItem);
        }
        if (dumpInfo != null) {
            setDumpTreeSelectedNode(dumpInfo);
        }

        reload(true);
    }

    public void refreshDecompiled() {
        clearAllScriptCache();
        if (abcPanel != null) {
            abcPanel.reload();
        }
        reload(true);
        updateClassesList();
    }

    private MissingCharacterHandler getMissingCharacterHandler() {
        return new MissingCharacterHandler() {

            // "configuration items" for the current replace only
            private final ConfigurationItem<Boolean> showAgainIgnoreMissingCharacters = new ConfigurationItem<>("showAgainIgnoreMissingCharacters", true, true);

            private boolean ignoreMissingCharacters = false;

            @Override
            public boolean getIgnoreMissingCharacters() {
                return ignoreMissingCharacters;
            }

            @Override
            public boolean handle(TextTag textTag, final FontTag font, final char character) {
                String fontName = font.getSwf().sourceFontNamesMap.get(font.getFontId());
                if (fontName == null) {
                    fontName = font.getFontName();
                }
                final Font f = FontTag.installedFontsByName.get(fontName);
                if (f == null || !f.canDisplay(character)) {
                    String msg = translate("error.font.nocharacter").replace("%char%", "" + character);
                    logger.log(Level.SEVERE, msg + " FontId: " + font.getCharacterId() + " TextId: " + textTag.getCharacterId());
                    ignoreMissingCharacters = View.showConfirmDialog(null, msg, translate("error"),
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE,
                            showAgainIgnoreMissingCharacters,
                            ignoreMissingCharacters ? JOptionPane.OK_OPTION : JOptionPane.CANCEL_OPTION) == JOptionPane.OK_OPTION;
                    return false;
                }
                View.execInEventDispatch(new Runnable() {

                    @Override
                    public void run() {
                        font.addCharacter(character, f);
                    }
                });

                return true;
            }
        };
    }

    public boolean saveText(TextTag textTag, String formattedText, String[] texts) {
        try {
            if (textTag.setFormattedText(getMissingCharacterHandler(), formattedText, texts)) {
                return true;
            }
        } catch (TextParseException ex) {
            View.showMessageDialog(null, translate("error.text.invalid").replace("%text%", ex.text).replace("%line%", Long.toString(ex.line)), translate("error"), JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    public boolean alignText(TextTag textTag, TextAlign textAlign) {
        if (textTag.alignText(textAlign)) {
            return true;
        }

        return false;
    }

    public boolean translateText(TextTag textTag, int diff) {
        if (textTag.translateText(diff)) {
            return true;
        }

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ACTION_SELECT_BKCOLOR:
                Color newColor = JColorChooser.showDialog(null, AppStrings.translate("dialog.selectbkcolor.title"), View.swfBackgroundColor);
                if (newColor != null) {
                    View.swfBackgroundColor = newColor;
                    reload(true);
                }
                break;
            case ACTION_REPLACE: {
                TreeItem item = tagTree.getCurrentTreeItem();
                if (item == null) {
                    return;
                }

                if (item instanceof DefineSoundTag) {
                    File selectedFile = showImportFileChooser("filter.sounds|*.mp3;*.wav|filter.sounds.mp3|*.mp3|filter.sounds.wav|*.wav");
                    if (selectedFile != null) {
                        File selfile = Helper.fixDialogFile(selectedFile);
                        DefineSoundTag ds = (DefineSoundTag) item;
                        int soundFormat = SoundFormat.FORMAT_UNCOMPRESSED_LITTLE_ENDIAN;
                        if (selfile.getName().toLowerCase().endsWith(".mp3")) {
                            soundFormat = SoundFormat.FORMAT_MP3;
                        }
                        boolean ok = false;
                        try {
                            ok = ds.setSound(new FileInputStream(selfile), soundFormat);
                        } catch (IOException ex) {
                            //ignore
                        }
                        if (!ok) {
                            View.showMessageDialog(null, translate("error.sound.invalid"), translate("error"), JOptionPane.ERROR_MESSAGE);
                        } else {
                            reload(true);
                        }
                    }
                }
                if (item instanceof ImageTag) {
                    ImageTag it = (ImageTag) item;
                    if (it.importSupported()) {
                        File selectedFile = showImportFileChooser("filter.images|*.jpg;*.jpeg;*.gif;*.png;*.bmp");
                        if (selectedFile != null) {
                            File selfile = Helper.fixDialogFile(selectedFile);
                            byte[] data = Helper.readFile(selfile.getAbsolutePath());
                            try {
                                Tag newTag = new ImageImporter().importImage(it, data);
                                if (newTag != null) {
                                    refreshTree();
                                    setTagTreeSelectedNode(newTag);
                                }
                                it.getSwf().clearImageCache();
                            } catch (IOException ex) {
                                logger.log(Level.SEVERE, "Invalid image", ex);
                                View.showMessageDialog(null, translate("error.image.invalid"), translate("error"), JOptionPane.ERROR_MESSAGE);
                            }
                            reload(true);
                        }
                    }
                }
                if (item instanceof ShapeTag) {
                    ShapeTag st = (ShapeTag) item;
                    File selectedFile = showImportFileChooser("filter.images|*.jpg;*.jpeg;*.gif;*.png;*.bmp");
                    if (selectedFile != null) {
                        File selfile = Helper.fixDialogFile(selectedFile);
                        byte[] data = Helper.readFile(selfile.getAbsolutePath());
                        try {
                            Tag newTag = new ShapeImporter().importImage(st, data);
                            if (newTag != null) {
                                refreshTree();
                                setTagTreeSelectedNode(newTag);
                            }
                            st.getSwf().clearImageCache();
                        } catch (IOException ex) {
                            logger.log(Level.SEVERE, "Invalid image", ex);
                            View.showMessageDialog(null, translate("error.image.invalid"), translate("error"), JOptionPane.ERROR_MESSAGE);
                        }
                        reload(true);
                    }
                }
                if (item instanceof DefineBinaryDataTag) {
                    DefineBinaryDataTag bt = (DefineBinaryDataTag) item;
                    File selectedFile = showImportFileChooser("");
                    if (selectedFile != null) {
                        File selfile = Helper.fixDialogFile(selectedFile);
                        byte[] data = Helper.readFile(selfile.getAbsolutePath());
                        new BinaryDataImporter().importData(bt, data);
                        reload(true);
                    }
                }
            }
            break;
        }
        if (Main.isWorking()) {
            return;
        }

        switch (e.getActionCommand()) {

            case MainFrameRibbonMenu.ACTION_EXPORT_JAVA_SOURCE:
                exportJavaSource();
                break;
            case MainFrameRibbonMenu.ACTION_EXPORT_SWF_XML:
                exportSwfXml();
                break;
            case MainFrameRibbonMenu.ACTION_IMPORT_SWF_XML:
                importSwfXml();
                break;
            case MainFrameRibbonMenu.ACTION_EXPORT_SEL:
                export(true);
                break;
        }
    }

    private File showImportFileChooser(String filter) {
        String[] filterArray = filter.split("\\|");

        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(Configuration.lastOpenDir.get()));
        boolean first = true;
        for (int i = 0; i < filterArray.length; i += 2) {
            final String filterName = filterArray[i];
            final String[] extensions = filterArray[i + 1].split(";");
            for (int j = 0; j < extensions.length; j++) {
                if (extensions[j].startsWith("*.")) {
                    extensions[j] = extensions[j].substring(1);
                }
            }
            FileFilter ff = new FileFilter() {

                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    String fileName = f.getName().toLowerCase();
                    for (String ext : extensions) {
                        if (fileName.endsWith(ext)) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public String getDescription() {
                    return translate(filterName);
                }
            };
            if (first) {
                fc.setFileFilter(ff);
            } else {
                fc.addChoosableFileFilter(ff);
            }
            first = false;
        }

        JFrame f = new JFrame();
        View.setWindowIcon(f);
        if (fc.showOpenDialog(f) == JFileChooser.APPROVE_OPTION) {
            File result = fc.getSelectedFile();
            Configuration.lastOpenDir.set(Helper.fixDialogFile(result).getParentFile().getAbsolutePath());
            return result;
        }

        return null;
    }

    private int splitPos = 0;

    public void showDetail(String card) {
        CardLayout cl = (CardLayout) (detailPanel.getLayout());
        cl.show(detailPanel, card);
        if (card.equals(DETAILCARDEMPTYPANEL)) {
            if (detailPanel.isVisible()) {
                splitPos = splitPane2.getDividerLocation();
                detailPanel.setVisible(false);
            }
        } else {
            if (!detailPanel.isVisible()) {
                detailPanel.setVisible(true);
                splitPane2.setDividerLocation(splitPos);
            }
        }

    }

    public void showCard(String card) {
        CardLayout cl = (CardLayout) (displayPanel.getLayout());
        cl.show(displayPanel, card);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        Object source = e.getSource();
        if (source == dumpTree) {
            reload(false);
            return;
        }

        TreeItem treeItem = (TreeItem) e.getPath().getLastPathComponent();
        if (!(treeItem instanceof SWFList)) {
            SWF swf = treeItem.getSwf();
            if (swfs.isEmpty()) {
                // show welcome panel after closing swfs
                updateUi();
            } else {
                updateUi(swf);
            }
        } else {
            updateUi();
        }

        reload(false);
    }

    public void unloadFlashPlayer() {
        if (flashPanel != null) {
            try {
                flashPanel.close();
            } catch (IOException ex) {
                // ignore
            }
        }
    }

    private void stopFlashPlayer() {
        if (flashPanel != null) {
            if (!flashPanel.isStopped()) {
                flashPanel.stopSWF();
            }
        }
    }

    public boolean isInternalFlashViewerSelected() {
        return mainMenu.isInternalFlashViewerSelected();
    }

    public static final int VIEW_RESOURCES = 0;

    public static final int VIEW_DUMP = 1;

    public static final int VIEW_TIMELINE = 2;

    private int getCurrentView() {
        return Configuration.dumpView.get() ? VIEW_DUMP : VIEW_RESOURCES;
    }

    public void setTreeModel(int view) {
        switch (view) {
            case VIEW_DUMP:
                if (dumpTree.getModel() == null) {
                    dumpTree.setModel(new DumpTreeModel(swfs));
                }
                break;
            case VIEW_RESOURCES:
                if (tagTree.getModel() == null) {
                    tagTree.setModel(new TagTreeModel(swfs, Configuration.tagTreeShowEmptyFolders.get()));
                }
                break;
        }
    }

    public boolean showView(int view) {

        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        setTreeModel(view);
        switch (view) {
            case VIEW_DUMP:
                if (!isWelcomeScreen) {
                    cl.show(contentPanel, SPLIT_PANE1);
                }
                treePanel.removeAll();
                treePanel.add(new JScrollPane(dumpTree), BorderLayout.CENTER);
                treePanelMode = TreePanelMode.DUMP_TREE;
                showDetail(DETAILCARDEMPTYPANEL);
                reload(true);
                treePanel.revalidate();
                return true;
            case VIEW_RESOURCES:
                if (!isWelcomeScreen) {
                    cl.show(contentPanel, SPLIT_PANE1);
                }
                treePanel.removeAll();
                treePanel.add(new JScrollPane(tagTree), BorderLayout.CENTER);
                treePanel.add(searchPanel, BorderLayout.SOUTH);
                treePanelMode = TreePanelMode.TAG_TREE;
                reload(true);
                treePanel.revalidate();
                return true;
            case VIEW_TIMELINE:
                final SWF swf = getCurrentSwf();
                if (swf != null) {
                    TreeItem item = tagTree.getCurrentTreeItem();
                    if (item instanceof DefineSpriteTag) {
                        timelineViewPanel.setTimelined((DefineSpriteTag) item);
                    } else {
                        timelineViewPanel.setTimelined(swf);
                    }
                    cl.show(contentPanel, TIMELINE_PANEL);
                    return true;
                }
                return false;
        }
        return false;

    }

    private void dumpViewReload(boolean forceReload) {
        showDetail(DETAILCARDEMPTYPANEL);

        DumpInfo dumpInfo = (DumpInfo) dumpTree.getLastSelectedPathComponent();
        if (dumpInfo == null) {
            showCard(CARDEMPTYPANEL);
            return;
        }

        dumpViewPanel.revalidate();
        dumpViewPanel.setSelectedNode(dumpInfo);
        showCard(CARDDUMPVIEW);
    }

    public void loadFromBinaryTag(final DefineBinaryDataTag binaryDataTag) {
        loadFromBinaryTag(Arrays.asList(binaryDataTag));
    }

    public void loadFromBinaryTag(final List<DefineBinaryDataTag> binaryDataTags) {

        Main.loadingDialog.setVisible(true);
        Main.startWork(AppStrings.translate("work.reading.swf") + "...");
        new Thread() {

            @Override
            public void run() {
                try {
                    for (DefineBinaryDataTag binaryDataTag : binaryDataTags) {
                        try {
                            InputStream is = new ByteArrayInputStream(binaryDataTag.binaryData.getRangeData());
                            SWF bswf = new SWF(is, null, "(SWF Data)", new ProgressListener() {

                                @Override
                                public void progress(int p) {
                                    Main.loadingDialog.setPercent(p);
                                }
                            }, Configuration.parallelSpeedUp.get());
                            binaryDataTag.innerSwf = bswf;
                            bswf.binaryData = binaryDataTag;
                        } catch (IOException ex) {
                            //ignore
                        }
                    }
                } catch (InterruptedException ex) {
                    //ignore
                }

                Main.loadingDialog.setVisible(false);
                Main.stopWork();
            }

        }.start();

    }

    private void closeTag() {
        previewPanel.closeTag();
    }

    public void reload(boolean forceReload) {
        if (Configuration.dumpView.get()) {
            dumpViewReload(forceReload);
            return;
        }

        TreeItem treeItem = (TreeItem) tagTree.getLastSelectedPathComponent();
        if (treeItem == null) {
            return;
        }

        if (!forceReload && (treeItem == oldItem)) {
            return;
        }

        if (oldItem != treeItem) {
            closeTag();
        }

        oldItem = treeItem;

        // show the preview of the tag when the user clicks to the tagname inside the scripts node, too
        // this is a little bit inconsistent, beacuse the frames (FrameScript) are not shown
        boolean preferScript = false;
        if (treeItem instanceof TagScript) {
            treeItem = ((TagScript) treeItem).getTag();
            preferScript = true;
        }

        //if (flashPanel != null) {
        //    flashPanel.specialPlayback = false;
        //}
        folderPreviewPanel.setItems(new ArrayList<>());
        previewPanel.clear();
        stopFlashPlayer();
        if (treeItem instanceof ScriptPack) {
            final ScriptPack scriptLeaf = (ScriptPack) treeItem;
            if (setSourceWorker != null) {
                setSourceWorker.cancel(true);
                setSourceWorker = null;
            }
            if (!Main.isWorking()) {
                Main.startWork(AppStrings.translate("work.decompiling") + "...");
                CancellableWorker worker = new CancellableWorker() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        getABCPanel().detailPanel.methodTraitPanel.methodCodePanel.clear();
                        getABCPanel().navigator.setAbc(scriptLeaf.abc);
                        getABCPanel().setAbc(scriptLeaf.abc);
                        getABCPanel().decompiledTextArea.setScript(scriptLeaf);
                        getABCPanel().decompiledTextArea.setNoTrait();
                        return null;
                    }

                    @Override
                    protected void done() {
                        Main.stopWork();

                        View.execInEventDispatch(new Runnable() {

                            @Override
                            public void run() {
                                setSourceWorker = null;
                                try {
                                    get();
                                } catch (CancellationException ex) {
                                    getABCPanel().decompiledTextArea.setText("// " + AppStrings.translate("work.canceled"));
                                } catch (Exception ex) {
                                    getABCPanel().decompiledTextArea.setText("// " + AppStrings.translate("decompilationError") + ": " + ex);
                                }
                            }
                        });
                    }
                };
                worker.execute();
                setSourceWorker = worker;
                Main.startWork(translate("work.decompiling") + "...", worker);
            }

            showDetail(DETAILCARDAS3NAVIGATOR);
            showCard(CARDACTIONSCRIPT3PANEL);
            return;
        } else {
            showDetail(DETAILCARDEMPTYPANEL);
        }

        previewPanel.setImageReplaceButtonVisible(false);

        boolean internalViewer = isInternalFlashViewerSelected();

        if (treeItem instanceof HeaderItem) {
            showCard(CARDHEADER);
            headerPanel.load(((HeaderItem) treeItem).getSwf());
        } else if (treeItem instanceof FolderItem) {
            showCard(CARDFOLDERPREVIEWPANEL);
            showFolderPreview(treeItem);
        } else if (treeItem instanceof SWF) {
            SWF swf = (SWF) treeItem;
            showCard(CARDPREVIEWPANEL);
            if (internalViewer) {
                previewPanel.showImagePanel(swf, swf, -1);
            } else {
                previewPanel.setParametersPanelVisible(false);
                if (flashPanel != null) {
                    previewPanel.showFlashViewerPanel();
                    previewPanel.showSwf(swf);
                }
            }
        } else if (treeItem instanceof DefineBinaryDataTag) {
            DefineBinaryDataTag binaryTag = (DefineBinaryDataTag) treeItem;
            showCard(CARDPREVIEWPANEL);
            previewPanel.showBinaryPanel(binaryTag);
        } else if (treeItem instanceof ASMSource && (!(treeItem instanceof DrawableTag) || preferScript)) {
            ensureActionPanel();
            showCard(CARDACTIONSCRIPTPANEL);
            getActionPanel().setSource((ASMSource) treeItem, !forceReload);
        } else if (treeItem instanceof ImageTag) {
            ImageTag imageTag = (ImageTag) treeItem;
            previewPanel.setImageReplaceButtonVisible(imageTag.importSupported());
            showCard(CARDPREVIEWPANEL);
            previewPanel.showImagePanel(imageTag.getImage());
        } else if ((treeItem instanceof DrawableTag) && (!(treeItem instanceof TextTag)) && (!(treeItem instanceof FontTag)) && internalViewer) {
            final Tag tag = (Tag) treeItem;
            showCard(CARDPREVIEWPANEL);
            DrawableTag d = (DrawableTag) tag;
            Timelined timelined;
            if (treeItem instanceof Timelined && !(treeItem instanceof ButtonTag)) {
                timelined = (Timelined) tag;
            } else {
                timelined = makeTimelined(tag);
            }

            previewPanel.setParametersPanelVisible(false);
            previewPanel.showImagePanel(timelined, tag.getSwf(), -1);
        } else if ((treeItem instanceof FontTag) && internalViewer) {
            FontTag fontTag = (FontTag) treeItem;
            showCard(CARDPREVIEWPANEL);
            showFontTag(fontTag);
        } else if ((treeItem instanceof TextTag) && internalViewer) {
            TextTag textTag = (TextTag) treeItem;
            showCard(CARDPREVIEWPANEL);
            showTextTag(textTag);
        } else if (treeItem instanceof Frame && internalViewer) {
            showCard(CARDPREVIEWPANEL);
            Frame fn = (Frame) treeItem;
            SWF swf = fn.getSwf();
            List<Tag> controlTags = swf.tags;
            int containerId = 0;
            RECT rect = swf.displayRect;
            int totalFrameCount = swf.frameCount;
            Timelined timelined = swf;
            if (fn.timeline.timelined instanceof DefineSpriteTag) {
                DefineSpriteTag parentSprite = (DefineSpriteTag) fn.timeline.timelined;
                controlTags = parentSprite.subTags;
                containerId = parentSprite.spriteId;
                rect = parentSprite.getRect();
                totalFrameCount = parentSprite.frameCount;
                timelined = parentSprite;
            }
            previewPanel.showImagePanel(timelined, swf, fn.frame);
        } else if ((treeItem instanceof SoundTag)) { //&& isInternalFlashViewerSelected() && (Arrays.asList("mp3", "wav").contains(((SoundTag) tagObj).getExportFormat())))) {
            showCard(CARDPREVIEWPANEL);
            previewPanel.showImagePanel(new SerializableImage(View.loadImage("sound32")));
            previewPanel.setImageReplaceButtonVisible(treeItem instanceof DefineSoundTag);
            try {
                SoundTagPlayer soundThread = new SoundTagPlayer((SoundTag) treeItem, Integer.MAX_VALUE, true);
                previewPanel.setMedia(soundThread);
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        } else if ((treeItem instanceof Frame) || ((treeItem instanceof CharacterTag) || (treeItem instanceof FontTag)) && (treeItem instanceof Tag) || (treeItem instanceof SoundStreamHeadTypeTag)) {
            showCard(CARDPREVIEWPANEL);
            previewPanel.createAndShowTempSwf(treeItem);

            if (treeItem instanceof TextTag) {
                showTextTag((TextTag) treeItem);
            } else if (treeItem instanceof FontTag) {
                showFontTag((FontTag) treeItem);
            } else {
                previewPanel.setParametersPanelVisible(false);
            }
        } else if (treeItem instanceof Tag) {
            showGenericTag((Tag) treeItem);
        } else {
            showCard(CARDEMPTYPANEL);
        }
    }

    public void showGenericTag(Tag tag) {

        showCard(CARDPREVIEWPANEL);
        previewPanel.showGenericTagPanel(tag);
    }

    private void showFontTag(FontTag ft) {

        previewPanel.showFontPanel(ft);
    }

    private void showTextTag(TextTag textTag) {

        previewPanel.showTextPanel(textTag);
    }

    public void showTextTagWithNewValue(TextTag textTag, TextTag newTextTag) {

        previewPanel.showTextComparePanel(textTag, newTextTag);
    }

    private void showFolderPreview(TreeItem treeNode) {
        List<TreeItem> folderPreviewItems = new ArrayList<>();
        FolderItem item = (FolderItem) treeNode;
        String folderName = item.getName();
        SWF swf = item.swf;
        switch (folderName) {
            case TagTreeModel.FOLDER_SHAPES:
                for (Tag tag : swf.tags) {
                    if (tag instanceof ShapeTag) {
                        folderPreviewItems.add(tag);
                    }
                }
                break;
            case TagTreeModel.FOLDER_MORPHSHAPES:
                for (Tag tag : swf.tags) {
                    if (tag instanceof MorphShapeTag) {
                        folderPreviewItems.add(tag);
                    }
                }
                break;
            case TagTreeModel.FOLDER_SPRITES:
                for (Tag tag : swf.tags) {
                    if (tag instanceof DefineSpriteTag) {
                        folderPreviewItems.add(tag);
                    }
                }
                break;
            case TagTreeModel.FOLDER_BUTTONS:
                for (Tag tag : swf.tags) {
                    if (tag instanceof ButtonTag) {
                        folderPreviewItems.add(tag);
                    }
                }
                break;
            case TagTreeModel.FOLDER_FONTS:
                for (Tag tag : swf.tags) {
                    if (tag instanceof FontTag) {
                        folderPreviewItems.add(tag);
                    }
                }
                break;
            case TagTreeModel.FOLDER_FRAMES:
                for (Frame frame : swf.getTimeline().getFrames()) {
                    folderPreviewItems.add(frame);
                }
                break;
            case TagTreeModel.FOLDER_IMAGES:
                for (Tag tag : swf.tags) {
                    if (tag instanceof ImageTag) {
                        folderPreviewItems.add(tag);
                    }
                }
                break;
            case TagTreeModel.FOLDER_TEXTS:
                for (Tag tag : swf.tags) {
                    if (tag instanceof TextTag) {
                        folderPreviewItems.add(tag);
                    }
                }
                break;
        }
        folderPreviewPanel.setItems(folderPreviewItems);
    }

    private boolean isFreeing;

    @Override
    public boolean isFreeing() {
        return isFreeing;
    }

    @Override
    public void free() {
        isFreeing = true;
        Helper.emptyObject(mainMenu);
        Helper.emptyObject(statusPanel);
        Helper.emptyObject(this);
    }

    public void setErrorState(ErrorState errorState) {
        statusPanel.setErrorState(errorState);
    }

    public static Timelined makeTimelined(final Tag tag) {

        return new Timelined() {

            private Timeline tim;

            @Override
            public Timeline getTimeline() {
                if (tim != null) {
                    return tim;
                }
                tim = new Timeline(tag.getSwf(), null, new ArrayList<Tag>(), ((CharacterTag) tag).getCharacterId(), getRect());
                if (tag instanceof MorphShapeTag) {
                    tim.frameRate = MORPH_SHAPE_ANIMATION_FRAME_RATE;
                    int framesCnt = tim.frameRate * MORPH_SHAPE_ANIMATION_LENGTH;
                    for (int i = 0; i < framesCnt; i++) {
                        Frame f = new Frame(tim, i);
                        DepthState ds = new DepthState(tag.getSwf(), f);
                        ds.characterId = ((CharacterTag) tag).getCharacterId();
                        ds.matrix = new MATRIX();
                        ds.ratio = i * 65535 / framesCnt;
                        f.layers.put(1, ds);
                        tim.addFrame(f);
                    }
                } else if (tag instanceof FontTag) {
                    int pageCount = PreviewPanel.getFontPageCount((FontTag) tag);
                    for (int i = 0; i < pageCount; i++) {
                        Frame f = new Frame(tim, i);
                        DepthState ds = new DepthState(tag.getSwf(), f);
                        ds.characterId = ((CharacterTag) tag).getCharacterId();
                        ds.matrix = new MATRIX();
                        ds.time = i;
                        f.layers.put(1, ds);
                        tim.addFrame(f);
                    }
                } else {
                    Frame f = new Frame(tim, 0);
                    DepthState ds = new DepthState(tag.getSwf(), f);
                    ds.characterId = ((CharacterTag) tag).getCharacterId();
                    ds.matrix = new MATRIX();
                    f.layers.put(1, ds);
                    tim.addFrame(f);
                }
                tim.displayRect = getRect();
                return tim;
            }

            @Override
            public RECT getRect() {
                return getRect(new HashSet<BoundedTag>());
            }

            @Override
            public RECT getRect(Set<BoundedTag> added) {
                BoundedTag bt = (BoundedTag) tag;
                if (!added.contains(bt)) {
                    return bt.getRect(added);
                }
                return new RECT(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
            }

            @Override
            public int hashCode() {
                return tag.hashCode();
            }
        };
    }
}
