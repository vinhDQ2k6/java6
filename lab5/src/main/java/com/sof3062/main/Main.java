package com.sof3062.main;

import com.formdev.flatlaf.FlatLightLaf;
import com.sof3062.view.StudentFrame;
import javax.swing.*;

public class Main {

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(new FlatLightLaf());
    } catch (Exception ex) {
      System.err.println("Failed to initialize LaF");
    }

    SwingUtilities.invokeLater(() -> {
      new StudentFrame().setVisible(true);
    });
  }
}
