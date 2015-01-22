package org.tzi.use.kodkod.plugin.gui.view;

import static org.tzi.use.kodkod.plugin.gui.util.ChangeString.bold;
import static org.tzi.use.kodkod.plugin.gui.util.ChangeString.html;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RendererNameAssociationClass extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	public RendererNameAssociationClass() {
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object color,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		if (isSelected) {
			setBackground(new Color(204,204,255));
		} else {
			setBackground(table.getBackground());
		}
		
		this.setText(html((String) table.getValueAt(row, column)
				+bold(" (AC)")));
		
		return this;
	}
}