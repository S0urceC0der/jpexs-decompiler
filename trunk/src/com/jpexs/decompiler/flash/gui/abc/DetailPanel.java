/*
 *  Copyright (C) 2011-2013 JPEXS
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
package com.jpexs.decompiler.flash.gui.abc;

import com.jpexs.decompiler.flash.abc.types.traits.Trait;
import static com.jpexs.decompiler.flash.gui.AppStrings.translate;
import com.jpexs.decompiler.flash.gui.View;
import com.jpexs.decompiler.flash.helpers.Helper;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 *
 * @author JPEXS
 */
public class DetailPanel extends JPanel implements ActionListener {

    public MethodTraitDetailPanel methodTraitPanel;
    public JPanel unsupportedTraitPanel;
    public SlotConstTraitDetailPanel slotConstTraitPanel;
    public static final String METHOD_TRAIT_CARD = translate("abc.detail.methodtrait");
    public static final String UNSUPPORTED_TRAIT_CARD = translate("abc.detail.unsupported");
    public static final String SLOT_CONST_TRAIT_CARD = translate("abc.detail.slotconsttrait");
    private JPanel innerPanel;
    public JButton saveButton = new JButton(translate("button.save"), View.getIcon("save16"));
    public JButton editButton = new JButton(translate("button.edit"), View.getIcon("edit16"));
    public JButton cancelButton = new JButton(translate("button.cancel"), View.getIcon("cancel16"));
    private HashMap<String, JComponent> cardMap = new HashMap<>();
    private String selectedCard;
    private JLabel selectedLabel;
    private boolean editMode = false;
    private JPanel buttonsPanel;
    private ABCPanel abcPanel;
    private JLabel traitNameLabel;

    public DetailPanel(ABCPanel abcPanel) {
        this.abcPanel = abcPanel;
        innerPanel = new JPanel();
        CardLayout layout = new CardLayout();
        innerPanel.setLayout(layout);
        methodTraitPanel = new MethodTraitDetailPanel(abcPanel);
        cardMap.put(METHOD_TRAIT_CARD, methodTraitPanel);

        unsupportedTraitPanel = new JPanel(new BorderLayout());
        JLabel unsup = new JLabel(translate("info.selecttrait"), SwingConstants.CENTER);
        unsupportedTraitPanel.add(unsup, BorderLayout.CENTER);

        cardMap.put(UNSUPPORTED_TRAIT_CARD, unsupportedTraitPanel);

        slotConstTraitPanel = new SlotConstTraitDetailPanel();
        cardMap.put(SLOT_CONST_TRAIT_CARD, slotConstTraitPanel);

        for (String key : cardMap.keySet()) {
            innerPanel.add(cardMap.get(key), key);
        }

        setLayout(new BorderLayout());
        add(innerPanel, BorderLayout.CENTER);

        editButton.setMargin(new Insets(3, 3, 3, 10));
        saveButton.setMargin(new Insets(3, 3, 3, 10));
        cancelButton.setMargin(new Insets(3, 3, 3, 10));

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        saveButton.setActionCommand("SAVEDETAIL");
        saveButton.addActionListener(this);
        editButton.setActionCommand("EDITDETAIL");
        editButton.addActionListener(this);
        cancelButton.setActionCommand("CANCELDETAIL");
        cancelButton.addActionListener(this);
        buttonsPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
        buttonsPanel.add(editButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        add(buttonsPanel, BorderLayout.SOUTH);
        selectedCard = UNSUPPORTED_TRAIT_CARD;
        layout.show(innerPanel, UNSUPPORTED_TRAIT_CARD);
        buttonsPanel.setVisible(false);
        selectedLabel = new JLabel("");
        selectedLabel.setText(selectedCard);
        selectedLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
        selectedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(selectedLabel, BorderLayout.NORTH);
        traitNameLabel = new JLabel("");
        JPanel traitInfoPanel = new JPanel();
        traitInfoPanel.setLayout(new BoxLayout(traitInfoPanel, BoxLayout.LINE_AXIS));
        traitInfoPanel.add(new JLabel("  " + translate("abc.detail.traitname")));
        traitInfoPanel.add(traitNameLabel);
        topPanel.add(traitInfoPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
    }

    public void setEditMode(boolean val) {
        slotConstTraitPanel.setEditMode(val);
        methodTraitPanel.setEditMode(val);
        saveButton.setVisible(val);
        editButton.setVisible(!val);
        cancelButton.setVisible(val);
        editMode = val;
        if (val) {
            selectedLabel.setIcon(View.getIcon("editing16"));
        } else {
            selectedLabel.setIcon(null);
        }
    }

    public void showCard(String name, Trait trait) {
        CardLayout layout = (CardLayout) innerPanel.getLayout();
        layout.show(innerPanel, name);
        boolean b = cardMap.get(name) instanceof TraitDetail;
        buttonsPanel.setVisible(b);
        selectedCard = name;
        selectedLabel.setText(selectedCard);
        if (trait == null) {
            traitNameLabel.setText("-");
        } else {
            traitNameLabel.setText(" m[" + trait.name_index + "]\"" + Helper.escapeString(trait.getName(abcPanel.abc).toString(abcPanel.abc.constants, new ArrayList<String>())) + "\"");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("EDITDETAIL")) {
            setEditMode(true);
            methodTraitPanel.methodCodePanel.focusEditor();
        }
        if (e.getActionCommand().equals("CANCELDETAIL")) {
            setEditMode(false);
            abcPanel.decompiledTextArea.resetEditing();
        }
        if (e.getActionCommand().equals("SAVEDETAIL")) {
            if (cardMap.get(selectedCard) instanceof TraitDetail) {
                if (((TraitDetail) cardMap.get(selectedCard)).save()) {
                    int lasttrait = abcPanel.decompiledTextArea.lastTraitIndex;
                    abcPanel.decompiledTextArea.reloadClass();
                    abcPanel.decompiledTextArea.gotoTrait(lasttrait);
                    JOptionPane.showMessageDialog(this, translate("message.trait.saved"));
                }
            }
        }
    }
}