package org.tzi.use.kodkod.plugin.gui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.tzi.use.kodkod.plugin.gui.ConfigurationTerms;
import org.tzi.use.kodkod.plugin.gui.model.data.AttributeSettings;
import org.tzi.use.kodkod.plugin.gui.model.data.ClassSettings;
import org.tzi.use.util.StringUtil;

public class TableModelAttribute extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
	
	private List<AttributeSettings> attributesSettings;
	
	private static String[] columnNames = new String[] {
			ConfigurationTerms.ATTRIBUTES,
			ConfigurationTerms.ATTRIBUTES_MIN,
			ConfigurationTerms.ATTRIBUTES_MAX,
			ConfigurationTerms.ATTRIBUTES_MINSIZE,
			ConfigurationTerms.ATTRIBUTES_MAXSIZE,
			ConfigurationTerms.ATTRIBUTES_VALUES 
	};
	
	public TableModelAttribute(List<AttributeSettings> settings) {
		super();
		this.attributesSettings = settings;
	}

	@Override
	public int getRowCount() {
		if (attributesSettings != null) {
			return this.attributesSettings.size(); 
		} else {
			return 0;
		}
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		AttributeSettings set = attributesSettings.get(row); 
		if ((set.getClassSettings().isAssociationClass()) && (column == 3 || column == 4)) {
			return false;
		} else if (set.isInherited()) {
			return false;
		} else if (column > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Object getValueAt(int row, int col) {
		AttributeSettings set = attributesSettings.get(row);
		
		switch(col) {
		case 0: 
			return set.getAttribute().name();
		case 1:
			if (set.getBounds().getLower() != null) {
				return set.getBounds().getLower();
			} else {
				return "";
			}
		case 2:
			if (set.getBounds().getUpper() != null) {
				return set.getBounds().getUpper();
			} else {
				return "";
			}
		case 3:
			if (set.getCollectionSize().getLower() != null) {
				return set.getCollectionSize().getLower();
			} else {
				return "";
			}
		case 4:
			if (set.getCollectionSize().getUpper() != null) {
				return set.getCollectionSize().getUpper();
			} else {
				return "";
			}
		case 5:
			return StringUtil.fmtSeq(set.getValues(), ",");
		default:
			return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		AttributeSettings set = this.attributesSettings.get(row);
		
		switch (column) {
		case 1:
			set.getBounds().setLower(aValue);
			break;
		case 2:
			set.getBounds().setUpper(aValue);
			break;
		case 3:
			set.getCollectionSize().setLower(aValue);
			break;
		case 4:
			set.getCollectionSize().setUpper(aValue);
			break;
		case 5:
			set.setValues((String) aValue);
			break;
		default:
			break;
		}
	}

	public void setClass(ClassSettings classSettings) {
		this.attributesSettings = new ArrayList<>(classSettings.getAttributeSettings().values());
		this.fireTableDataChanged();
	}

	public List<AttributeSettings> getAttributesSettings() {
		return attributesSettings;
	}
	
}
