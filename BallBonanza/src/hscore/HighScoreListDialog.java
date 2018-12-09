package hscore;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JTable;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.Component;

public class HighScoreListDialog extends JDialog {
	private JTable table;
	private HighScoreTableModel tableModel;

	/**
	 * Create the dialog.
	 */
	public HighScoreListDialog(HighScoreTableModel model) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		tableModel = model;
		setResizable(false);
		setTitle("High Score");
		setBounds(100, 100, 750, 400);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) 
					{
						hideDialog();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			{
				table = new JTable(tableModel);
				
				DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer()
				{
					//Make the text bold
					Font newHeaderFont = table.getTableHeader().getFont().deriveFont(Font.BOLD);
					
				    // override renderer preparation
				    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				                                                   boolean hasFocus,
				                                                   int row, int column)
				    {
				        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				        cell.setFont(newHeaderFont);
				        return cell;
				    }
				};
				
				//Left align the table headers
				headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
				//Set the color of the text of the table header
				headerRenderer.setForeground(Color.WHITE);
				//Set the background color of the table header
				headerRenderer.setBackground(Color.DARK_GRAY);
				//Use the custom cell renderer for the headers
				for(int i = 0; i < table.getColumnCount(); i++)
				{
					table.getColumn(table.getColumnName(i)).setHeaderRenderer(headerRenderer);
				}
				
				table.setShowGrid(false);
				table.setEnabled(false);
				table.setFillsViewportHeight(true);
				scrollPane.setViewportView(table);
			}
		}
		
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	public void setHighScoreList(ArrayList<Object[]> highScoreList)
	{
		tableModel.setRows(highScoreList);
	}
	
	private void hideDialog()
	{
		this.setVisible(false);
	}

}
