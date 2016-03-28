package com.ifc.jyg;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.concurrent.Exchanger;

public class test {

	public static void main(String[] args) throws IOException {
		NewFrame frame1 = new NewFrame();
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//一定要设置关闭
		frame1.setVisible(true);
		JButton button = frame1.getButton();

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						frame1.getLabel().setText("Extrating!");
						try {
							File exeFile = frame1.exeFileChooser.getSelectedFile();
							File ifcFile = frame1.inputFileChooser.getSelectedFile();
							IfcExtrator ifcExtrator = new IfcExtrator();

							if(ifcFile!=null) {
								ifcExtrator.setInputIfcFileName(ifcFile.getAbsolutePath());
							}
							if(exeFile!=null) {
								ifcExtrator.setIfcConvertExeName(exeFile.getAbsolutePath());
							}
							ifcExtrator.extract();
							ifcExtrator.printResultToFile();
							frame1.getLabel().setText("Extrated"+ifcExtrator.successMessage);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
				thread.start();
			}
		});
	}
}
